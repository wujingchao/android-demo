//
// Created by wujingchao on 18/5/17.
//

#ifndef ANDROID_DEMO_POLLSOCKETSERVER_H
#define ANDROID_DEMO_POLLSOCKETSERVER_H

#include <sys/epoll.h>
#include "AbstractSocketServer.h"

class EPollSocketServer : public AbstractSocketServer{


public:
    EPollSocketServer(const char *ip, int port):AbstractSocketServer(ip, port) {

    }

    void addEvent(int epollfd, int fd, int i);

    void handleEvent(int epollfd, epoll_event *events, int ret, int fd);

    void handleAccept(int epollfd, int fd);

    void receiveClientMsg(int epollfd, int fd);

    void deleteEvent(int epollfd, int fd, int i);

protected:
    virtual void processClientConnect(int serverFD);

};


#endif //ANDROID_DEMO_POLLSOCKETSERVER_H
