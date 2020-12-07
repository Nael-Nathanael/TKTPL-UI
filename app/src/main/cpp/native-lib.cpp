#include <jni.h>
#include <string>

// https://stackoverflow.com/questions/11558899/passing-a-string-to-c-code-in-android-ndk
std::string ConvertJString(JNIEnv* env, jstring str)
{
   if ( !str ) std::string();

   const jsize len = env->GetStringUTFLength(str);
   const char* strChars = env->GetStringUTFChars(str, (jboolean *)0);

   std::string Result(strChars, len);

   env->ReleaseStringUTFChars(str, strChars);

   return Result;
}

extern "C" JNIEXPORT jstring JNICALL
Java_id_ac_ui_cs_mobileprogramming_nathanael_helloworld_MainActivity_greetingNameFromJNI(
        JNIEnv* env,
        jobject thiz,
        jstring jName) {

    std::string normalstring = ConvertJString( env, jName );

    std::string hello = "Native said: Hello " + bmpath;
    return env->NewStringUTF(hello.c_str());
}

