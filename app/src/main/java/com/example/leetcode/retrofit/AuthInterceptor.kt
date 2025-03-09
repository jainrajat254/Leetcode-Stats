package com.example.leetcode.retrofit

import android.content.Context
import com.example.leetcode.sharedPreferences.SharedPreferencesManager
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(@ApplicationContext private val context: Context) :
    Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = SharedPreferencesManager.getJwtToken()

        println(token)

        val request = if (!token.isNullOrEmpty()) {
            chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
        } else {
            chain.request()
        }

        return chain.proceed(request)
    }
}
