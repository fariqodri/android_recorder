package id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.repositories

import android.util.Log
import id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.models.SpeechToTextModel
import id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.networking.RetrofitService
import id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.networking.api.SpeechToTextApi
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class SpeechToTextRepo {
    private val speechToTextApi = RetrofitService.createService(SpeechToTextApi::class.java)

    companion object {
        private lateinit var speechToTextRepo: SpeechToTextRepo

        fun getInstance(): SpeechToTextRepo {
            if (!::speechToTextRepo.isInitialized) {
                speechToTextRepo = SpeechToTextRepo()
            }
            return speechToTextRepo
        }
    }

    fun recognizeSpeech(fileLocation: String, fileName: String, onSuccess: (recognizedText: String?) -> Unit, onFailure: () -> Unit) {
        val file = File(fileLocation)
        val requestBody = RequestBody.create(MediaType.parse("audio/mpeg"), file)
        val fileUpload = MultipartBody.Part.createFormData("audio", fileName, requestBody)
        val channelId = MultipartBody.Part.createFormData("channel_id", "00c05f52-0261-4e44-819f-549e38d529d7")
        speechToTextApi.recognizeSpeech(fileUpload, channelId).enqueue(object : Callback<SpeechToTextModel> {
            override fun onFailure(call: Call<SpeechToTextModel>, t: Throwable) {
                Log.e("Failure", t.message)
                onFailure()
//                result.value = null
//                isLoading.value = false
            }

            override fun onResponse(
                call: Call<SpeechToTextModel>,
                response: Response<SpeechToTextModel>
            ) {
                Log.d("Response", response.body().toString())
                onSuccess(response.body()?.recognizedText)
//                result.value = response.body()?.recognizedText
//                isLoading.value = false
            }
        })
    }
}