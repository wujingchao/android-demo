//
// Created by wujingchao on 18/5/14.
//

#ifndef ANDROID_DEMO_SELECTSOCKETSERVER_H
#define ANDROID_DEMO_SELECTSOCKETSERVER_H

#include "AbstractSocketServer.h"

#include <sys/select.h>
#include <list>

class SelectSocketServer : public AbstractSocketServer{

public:
    SelectSocketServer(const char* ip, int port):AbstractSocketServer(ip, port) {

    }

protected:

    virtual void processClientConnect(int serverFD);


private:

    fd_set mAllFds;

    int mMaxFd = 0;

    void receiveClientMsg(fd_set *fds);

};


#endif //ANDROID_DEMO_SELECTSOCKETSERVER_H
