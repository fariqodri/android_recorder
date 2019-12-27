package id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.jni

class CaesarEncryption {
    external fun decrypt(encryptedText: String, shift: Int): String
    external fun encrypt(encryptedText: String, shift: Int): String

    companion object {

        // Used to load the 'native-lib' library on application startup.
        init {
            System.loadLibrary("native-lib")
        }
    }
}