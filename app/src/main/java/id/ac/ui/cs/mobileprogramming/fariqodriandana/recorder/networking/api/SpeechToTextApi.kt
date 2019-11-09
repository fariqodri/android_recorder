package id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.networking.api

import id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.models.SpeechToTextModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface SpeechToTextApi {
    @Multipart
    @POST("jobs")
    fun recognizeSpeech(@Part file: MultipartBody.Part, @Part channelId: MultipartBody.Part): Call<SpeechToTextModel>
}