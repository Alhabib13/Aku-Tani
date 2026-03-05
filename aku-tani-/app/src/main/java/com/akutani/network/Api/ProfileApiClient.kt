package com.akutani.network.Api

import android.content.Context
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ProfileApiClient {

    private const val BASE_URL = "http://10.148.190.180:8000/api/"

    fun create(context: Context): ProfileApi {

        val prefs = context.getSharedPreferences("aku_tani_prefs", Context.MODE_PRIVATE)
        val token = prefs.getString("token", null)

        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .addInterceptor { chain ->
                val reqBuilder = chain.request().newBuilder()
                token?.let {
                    reqBuilder.addHeader("Authorization", "Bearer $it")
                }
                chain.proceed(reqBuilder.build())
            }
            .addInterceptor(logging)
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ProfileApi::class.java)
    }
}
