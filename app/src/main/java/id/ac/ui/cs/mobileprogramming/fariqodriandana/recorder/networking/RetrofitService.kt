package id.ac.ui.cs.mobileprogramming.fariqodriandana.recorder.networking

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitService {
    private val client = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://speech-to-text-api.katalabs.io")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun <S> createService(serviceClass: Class<S>): S {
        return retrofit.create(serviceClass)
    }
}