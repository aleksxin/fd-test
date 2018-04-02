#include <jni.h>
#include <string>
#include <opencv2/core.hpp>

extern "C" JNIEXPORT jstring

JNICALL
Java_ocv_keit_bg_opencvapp_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}

extern "C" JNIEXPORT jstring
JNICALL
Java_ocv_keit_bg_opencvapp_MainActivity_validate(
        JNIEnv *env,
jobject /* this */, jlong addrGrey, jlong addrRgba) {
    cv::Rect();
    cv::Mat();
std::string hello2 = "Hello from validate";
return env->NewStringUTF(hello2.c_str());
}
