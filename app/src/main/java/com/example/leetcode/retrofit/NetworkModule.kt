package com.example.leetcode.retrofit

import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS) // Set the connection timeout
        .writeTimeout(30, TimeUnit.SECONDS) // Set the write timeout
        .readTimeout(30, TimeUnit.SECONDS) // Set the read timeout
        .build()

    private val gson = GsonBuilder()
        .setLenient()
        .create()

    private const val BASE_URL = "https://6dea-2409-40d2-5e-38cf-dd8e-4d35-ace-e0cd.ngrok-free.app"

    @Provides
    @Singleton
    @Named("default")
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(@Named("default") retrofit: Retrofit): UserService {
        return retrofit.create(UserService::class.java)
    }
}