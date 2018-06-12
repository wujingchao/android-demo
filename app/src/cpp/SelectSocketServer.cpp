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