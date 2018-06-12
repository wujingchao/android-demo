//
// Created by wujingchao on 18/5/27.
//

#include <unistd.h>
#include <sys/socket.h>
#include <endian.h>
#include <linux/in.h>
#include <arpa/inet.h>


#include "AbstractSocketServer.h"
#include "util/Log.h"

#define LOG_TAG "AbstractSocketServer"

bool AbstractSocketServer::startServer() {
    int serverFd = createServer(mIp, mPort);
    if (serverFd == -1) {
        return false;
    }

    processClientConnect(serverFd);

    close(serverFd);

    return false;
}

bool AbstractSocketServer::readn(int socketFd, unsigned char *buf, size_t len) {
    size_t remainLen = len;
    int tmpLen;
    int retryCount = 0;
    while (remainLen > 0) {
        tmpLen = read(socketFd, buf, remainLen);
        if (tmpLen == 0) {
            sleep(100);//这里可以做的更好一点，当读到0的时候，继续放到select里面去，保留当前的状态
                       //或者使用Socket的高级IO函数
            if (++retryCount > 10) {
                break;
            }
        }
        if (tmpLen == -1) {
            if (errno == EINTR) {
                LOGW("internal error, try again...");
                break;
            } else {
                return false;
            }
        }
        buf += tmpLen;
        remainLen -= tmpLen;
    }
    return remainLen == 0;
}

/**
 *
 * @param ip
 * @param port
 * @return -1 create fail
 */
int AbstractSocketServer::createServer(const char *ip, int port) {
    int fd = socket(AF_INET, SOCK_STREAM, 0);
    if (fd == -1) {
        LOGE("create socket fail, reason : %s", strerror(errno));
        return -1;
    }

    int reuse = 1;
    if (setsockopt(fd, SOL_SOCKET, SO_REUSEADDR, &reuse, sizeof(reuse)) == -1) {
        LOGE("set socket reuse addr fail, reason : %s", strerror(errno));
        return -1;
    }

    struct sockaddr_in serverAddr;
    memset(&serverAddr, 0, sizeof(serverAddr));
    serverAddr.sin_family = AF_INET;
    if (ip == nullptr) {
        serverAddr.sin_addr.s_addr = htonl(INADDR_ANY);
    } else {
        inet_pton(AF_INET, ip, &serverAddr.sin_addr);
    }
    serverAddr.sin_port = htons(port);
    if (bind(fd, reinterpret_cast<const sockaddr *>(&serverAddr), sizeof(serverAddr)) == -1) {
        LOGE("bind server socket error : %s", strerror(errno));
        return -1;
    }

    listen(fd, 10/*backlog*/);
    return fd;
}

void AbstractSocketServer::acceptClient(int serverFd) {

    LOGV("accept client");

    ACCEPT:
    struct sockaddr_in clientAddr;
    socklen_t clientAddrLen = sizeof(clientAddr);
    int clientFD = accept(serverFd, (sockaddr *)&clientAddr, &clientAddrLen);
    if (clientFD == -1) {
        if (errno == EINTR) {
            LOGW("internal error, try again...");
            goto ACCEPT;
        } else {
            LOGE("accept fail : %s", strerror(errno));
            return;
        }
    }

    LOGD("accept a new client : %s:%d\n", inet_ntoa(clientAddr.sin_addr), clientAddr.sin_port);
    mClientFds.push_back(clientFD);
}
