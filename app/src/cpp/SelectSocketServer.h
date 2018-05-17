//
// Created by wujingchao on 18/5/14.
//

#ifndef ANDROID_DEMO_SELECTSOCKETSERVER_H
#define ANDROID_DEMO_SELECTSOCKETSERVER_H


#include <sys/select.h>
#include <list>

class SelectSocketServer {

public:
    SelectSocketServer(const char* ip, int port):
            mIp(ip),
            mPort(port),
            mRunning(true) {}

    bool startServer();

private:

    const char* mIp;

    int mPort;

    std::list<int> mClientFds;

    fd_set mAllFds;

    int mMaxFd = 0;

    bool mRunning;

    int createServer(const char* ip, int port);

    void processClientConnect(int serverFD);

    void acceptClient(int serverFD);

    void receiveClientMsg(fd_set *fds);

    bool readn(int socketFd, unsigned char *buf, size_t len);
};


#endif //ANDROID_DEMO_SELECTSOCKETSERVER_H
