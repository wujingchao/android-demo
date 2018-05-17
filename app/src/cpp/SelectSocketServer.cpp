//
// Created by wujingchao on 18/5/14.
//

#define LOG_TAG "SelectSocketServer"
#include "SelectSocketServer.h"


#include "util/Log.h"
#include "util/Tool.h"

#include <netinet/ip.h>
#include <arpa/inet.h>
#include <time.h>
#include <algorithm>
#include <unistd.h>

bool SelectSocketServer::startServer() {
    int serverFd = createServer(mIp, mPort);
    if (serverFd == -1) {
        return false;
    }

    processClientConnect(serverFd);

    close(serverFd);

    return false;
}

/**
 *
 * @param ip
 * @param port
 * @return -1 create fail
 */
int SelectSocketServer::createServer(const char *ip, int port) {
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

void SelectSocketServer::processClientConnect(int serverFd) {
    fd_set *readFds = &mAllFds;
    while (mRunning) {
        FD_ZERO(readFds);
        FD_SET(serverFd, readFds);
        mMaxFd = serverFd;

        for(int clientFD : mClientFds) {
            FD_SET(clientFD, readFds);
            mMaxFd = std::max(clientFD, mMaxFd);
        }

        if (mMaxFd == FD_SETSIZE) {
            LOGE("too many client");
        }

        int retVal = select(mMaxFd + 1, readFds, nullptr, nullptr, nullptr);
        if (retVal == -1) {
            LOGE("select error, %s", strerror(errno));
            return;
        }

        if (FD_ISSET(serverFd, readFds)) {
            acceptClient(serverFd);
        } else {
            receiveClientMsg(readFds);
        }
    }
}

void SelectSocketServer::acceptClient(int serverFd) {

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

bool SelectSocketServer::readn(int socketFd, unsigned char *buf, size_t len) {
    size_t remainLen = len;
    int tmpLen;
    int retryCount = 0;
    while (remainLen > 0) {
        tmpLen = read(socketFd, buf, remainLen);
        if (tmpLen == 0) {
            sleep(100);//这里可以做的更好一点，当读到0的时候，继续放到select里面去，保留当前的状态
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


void SelectSocketServer::receiveClientMsg(fd_set *readFds) {
    for (auto iter = mClientFds.begin(); iter != mClientFds.end();) {
        int clientFD = *iter;
        if (FD_ISSET(clientFD, readFds)) {
            uint8_t buf[4] = {0};
            if (readn(clientFD, buf, sizeof(buf))) {

                write(clientFD, buf, sizeof(buf));//write back

                int len = Tool::swap_endian(*reinterpret_cast<int*>(buf));
                LOGD("receive len : %d", len);
                uint8_t *msg = static_cast<uint8_t *>(malloc(len + 1));
                *(msg + len) = '\0';
                if (readn(clientFD, msg, len)) {

                    write(clientFD, msg, len);//write back

                    LOGD("msg : %s", msg);
                } else {
                    LOGE("read msg fail");
                }

            } else{
                LOGE("read len fail...");
            }

            FD_CLR(clientFD, readFds);
            iter = mClientFds.erase(iter);
            close(clientFD);
            LOGD("close clientFD : %d", clientFD);

        } else {
            ++iter;
        }
    }
}