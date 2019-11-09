package id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SpeechToTextModel(
    @SerializedName("message")
    @Expose
    val recognizedText: String
)