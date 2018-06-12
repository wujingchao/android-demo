//
// Created by wujingchao on 18/5/27.
//

#ifndef ANDROID_DEMO_ABSTRACTSOCKETSERVER_H
#define ANDROID_DEMO_ABSTRACTSOCKETSERVER_H

/**
 * 参考:http://www.cnblogs.com/Anker/p/3265058.html
 */


#include <list>
#include <cstdlib>

class AbstractSocketServer {

public:
    AbstractSocketServer(const char* ip, int port):
            mIp(ip),
            mPort(port),
            mRunning(true) {}

    bool startServer();

    virtual void processClientConnect(int serverFD) = 0;


protected:

    const char* mIp;

    int mPort;

    bool mRunning;

    std::list<int> mClientFds;

    int createServer(const char* ip, int port);

    void acceptClient(int serverFD);

    bool readn(int socketFd, unsigned char *buf, size_t len);

};


#endif //ANDROID_DEMO_ABSTRACTSOCKETSERVER_H
