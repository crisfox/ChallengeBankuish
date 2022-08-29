package com.example.challengebankuish.common.networking

import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Configuraciones de retrofit y llamadas http con timeout.
 */
object Client {

    private const val BASE_URL = "https://api.github.com/"

    private const val TIME_OUT: Long = 120

    val okHttpClient: OkHttpClient = OkHttpClient
        .Builder()
        .readTimeout(TIME_OUT, TimeUnit.SECONDS)
        .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
        .addInterceptor { chain ->
            val resp = chain.proceed(chain.request())
            if (resp.code() == 200) {
                try {
                    val myJson = resp
                        .peekBody(2048)
                        .string()
                    println(myJson)
                } catch (e: Exception) {
                    println("Error parse json from intercept..............")
                }
            } else {
                println(resp)
            }
            resp
        }
        .build()

    fun provideRetrofit(factory: Gson, client: OkHttpClient): Retrofit = Retrofit
        .Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(factory))
        .client(client)
        .build()

    val provideGson: Gson =
        GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
            .create()
}
