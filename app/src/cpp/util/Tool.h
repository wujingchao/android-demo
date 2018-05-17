//
// Created by wujingchao on 18/5/17.
//

#ifndef ANDROID_DEMO_TOOL_H
#define ANDROID_DEMO_TOOL_H
#include <cstdint>

struct Tool {

template <typename T>
static T swap_endian(T);

};

template <typename T>
T Tool::swap_endian(T t) {
    T ret = 0;
    uint8_t *in = reinterpret_cast<uint8_t *>(&t);
    uint8_t *tmp = reinterpret_cast<uint8_t*>(&ret);
    for (int i = sizeof(T) - 1; i >=0; i--) {
        *(tmp + i) = *in++;
    }
    return ret;
}


#endif //ANDROID_DEMO_TOOL_H
