//
// Created by Fari Qodri Andana on 2019-12-09.
//

#include <jni.h>
#include <math.h>

extern "C" JNIEXPORT jdouble JNICALL

Java_id_ac_ui_cs_mobileprogramming_fariqodriandana_recorder_activities_MainActivity_power(
        JNIEnv *env,
        jobject /* this */,
        jdouble base,
        jint raise) {
    return pow(base, raise);
}
