package com.akutani.network.Api

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.google.gson.GsonBuilder
import java.util.concurrent.TimeUnit


object ArticleApiClient {

    // Pastikan base URL ini benar & diakhiri "/api/"
    private const val BASE_URL = "http://10.148.190.180:8000/api/"

    // Token akan diisi setelah login user
    var token: String? = null

    // Logging untuk debugging API
    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // Interceptor untuk menambahkan Authorization header jika token tersedia
    private val authInterceptor = Interceptor { chain ->
        val original = chain.request()
        val builder = original.newBuilder()

        token?.let { t ->
            builder.addHeader("Authorization", "Bearer $t")
        }

        chain.proceed(builder.build())
    }

    // OkHttpClient dengan auth + logging
    private val client = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .addInterceptor(logging)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .retryOnConnectionFailure(true)
        .build()

    // Gson konfigurasi long date / format fleksibel
    private val gson = GsonBuilder()
        .setLenient()
        .create()

    // Retrofit instance
    val api: ArticleApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ArticleApi::class.java)
    }
}
