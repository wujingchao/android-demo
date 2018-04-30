//
// Created by wujingchao on 17/12/11.
//

#include "util/Log.h"

#include <jni.h>
#include <sys/types.h>
#include <unistd.h>
#include <string.h>
#include <wait.h>
#include <stdlib.h>

#include <string>
#include <sstream>
#include <memory>
#include <dlfcn.h>//TODO

#define LOG_TAG "native-lib"

//extern "C" 加上extern C是防止C++编译器对方法名进行重整(mangled)

extern "C"
jstring Java_com_wujingchao_android_demo_os_PipeDemo_readFromChildPipe(JNIEnv *env, jobject instance) {
    int pipe_fd[2] = {0};//first for read, second for write
    if (pipe(pipe_fd) == -1) {
        const char* msg = "create pipe error";
        LOGE(msg);
        return env->NewStringUTF(msg);
    }
    pid_t childPid = fork();
    if (childPid == -1) {
        const char* msg = "fork error";
        LOGE(msg);
        close(pipe_fd[0]);
        close(pipe_fd[1]);
    }

    if (childPid == 0) {//child process
        close(pipe_fd[0]);//close  read
        write(pipe_fd[1], "PIPE", 4);
        close(pipe_fd[1]);
        exit(0);
    } else {
        std::shared_ptr<std::string> ptr =  std::make_shared<std::string>("shared_ptr str");
        LOGD(ptr->c_str());
        //just test c++...
        std::ostringstream ostream;
        ostream << childPid;
        std::string tmp("child process : " + ostream.str());
        LOGD(tmp.c_str());

        char buff[5] = {0};
        close(pipe_fd[1]);
        while (read(pipe_fd[0], buff, sizeof(buff) - 1) > 0) {
            LOGE(buff);
        }
        close(pipe_fd[0]);
        waitpid(childPid, NULL, 0);//reap child process
        return env->NewStringUTF(buff);

    }
}

extern "C"
void Java_com_wujingchao_android_demo_os_PipeDemo_allocManyLocalRef(JNIEnv *env, jobject instance) {
    for (int i = 0; i < 1024; i++) {
        jclass jclass1 = env->FindClass("android/os/Looper");//local reference的数量是有限制的
    }
}