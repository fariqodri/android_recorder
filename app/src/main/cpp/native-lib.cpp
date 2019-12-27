//
// Created by Fari Qodri Andana on 2019-12-09.
//

#include <jni.h>
#include <math.h>
#include <ctype.h>
#include <string>

using namespace std;

string encrypt(string text, int s)
{
    string result = "";

    // traverse text
    for (int i=0;i<text.length();i++)
    {
        // apply transformation to each character
        // Encrypt Uppercase letters
        if (isupper(text[i]))
            result += char(int(text[i]+s-65)%26 +65);

            // Encrypt Lowercase letters
        else
            result += char(int(text[i]+s-97)%26 +97);
    }

    // Return the resulting string
    return result;
}

string decrypt(string *text, int s) {
    return encrypt(*text, 26 - s);
}

string jstring2string(JNIEnv *env, jstring jStr) {
    if (!jStr)
        return "";

    const jclass stringClass = env->GetObjectClass(jStr);
    const jmethodID getBytes = env->GetMethodID(stringClass, "getBytes", "(Ljava/lang/String;)[B");
    const jbyteArray stringJbytes = (jbyteArray) env->CallObjectMethod(jStr, getBytes, env->NewStringUTF("UTF-8"));

    size_t length = (size_t) env->GetArrayLength(stringJbytes);
    jbyte* pBytes = env->GetByteArrayElements(stringJbytes, NULL);

    std::string ret = std::string((char *)pBytes, length);
    env->ReleaseByteArrayElements(stringJbytes, pBytes, JNI_ABORT);

    env->DeleteLocalRef(stringJbytes);
    env->DeleteLocalRef(stringClass);
    return ret;
}

extern "C" JNIEXPORT jstring JNICALL
Java_id_ac_ui_cs_mobileprogramming_fariqodriandana_recorder_jni_CaesarEncryption_decrypt(
        JNIEnv *env,
        jobject /* this */,
        jstring encrypted,
        jint shift) {
    string text = jstring2string(env, encrypted);
    string result = decrypt(&text, shift);
    const char *c = result.c_str();
    return (*env).NewStringUTF(c);
}

extern "C" JNIEXPORT jstring JNICALL
Java_id_ac_ui_cs_mobileprogramming_fariqodriandana_recorder_jni_CaesarEncryption_encrypt(
        JNIEnv *env,
        jobject /* this */,
        jstring plain,
        jint shift) {
    string text = jstring2string(env, plain);
    string result = encrypt(text, shift);
    const char *c = result.c_str();
    return (*env).NewStringUTF(c);
}
