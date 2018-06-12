//
// Created by wujingchao on 18/5/17.
//

#include <unistd.h>
#include "EPollSocketServer.h"
#include "AbstractSocketServer.h"
#include "util/Log.h"
#include "util/Tool.h"

#define EPOLLEVENTS 1024
#define LOG_TAG "EPollSocketServer"

void EPollSocketServer::processClientConnect(int serverFD) {
    epoll_event events[EPOLLEVENTS];

    int epollfd = epoll_create(EPOLLEVENTS);
    addEvent(epollfd, serverFD, EPOLLIN);
    while (mRunning) {
        int num = epoll_wait(epollfd, events, EPOLLEVENTS, -1);
        handleEvent(epollfd, events, num, serverFD);
    }
    close(epollfd);

}

void EPollSocketServer::handleEvent(int epollfd, epoll_event *events, int num, int serverFd) {
    int i;
    int fd;
    for (i = 0; i < num; i++) {
        fd = events[i].data.fd;
        if (fd == serverFd && events[i].events & EPOLLIN) {
            handleAccept(epollfd, serverFd);
        } else if (events[i].events & EPOLLIN) {
            receiveClientMsg(epollfd, fd);
        } else if (events[i].events & EPOLLOUT) {
            //....
        }
    }
}

void EPollSocketServer::handleAccept(int epollfd, int serverFD) {
    acceptClient(serverFD);
    for(int fd : mClientFds) {
        addEvent(epollfd, fd, EPOLLIN|EPOLLONESHOT);
    }
}

void EPollSocketServer::receiveClientMsg(int epollfd, int clientFD) {
    uint8_t buf[4] = {0};
    if (readn(clientFD, buf, sizeof(buf))) {

        write(clientFD, buf, sizeof(buf));//write back

        int len = Tool::swap_endian(*reinterpret_cast<int*>(buf));
        LOGD("receive len : %d", len);
        uint8_t *msg = static_cast<uint8_t *>(malloc(len + 1));//可以加到epoll的监听可写的fd集合里
        *(msg + len) = '\0';
        if (readn(clientFD, msg, len)) {

            write(clientFD, msg, len);//write back TODO writen...

            LOGD("msg : %s", msg);
        } else {
            LOGE("read msg fail");
        }
    } else{
        LOGE("read len fail...");
    }
    close(clientFD);
    LOGD("close clientFD : %d", clientFD);
    deleteEvent(epollfd, clientFD, EPOLLIN);
}

void EPollSocketServer::addEvent(int epollfd, int fd, int state) {
    epoll_event ev;
    ev.events = static_cast<uint32_t>(state);
    ev.data.fd = fd;
    epoll_ctl(epollfd, EPOLL_CTL_ADD, fd, &ev);
}


void EPollSocketServer::deleteEvent(int epollfd, int fd, int state) {
    epoll_event ev;
    ev.events = static_cast<uint32_t>(state);
    ev.data.fd = fd;
    epoll_ctl(epollfd, EPOLL_CTL_DEL, fd, &ev);
}
