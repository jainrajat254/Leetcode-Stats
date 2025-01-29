package com.example.leetcode.retrofit

import com.example.leetcode.data.LoginCredentials
import com.example.leetcode.data.LoginResponse
import com.example.leetcode.data.UserData
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface UserService {

    @POST("/register")
    suspend fun registerUser(@Body user: UserData): Response<UserData>

    @POST("/login")
    suspend fun loginUser(@Body user: LoginCredentials): Response<LoginResponse>

    @GET("/clubLeaderBoard")
    suspend fun clubLeaderBoard(): Response<List<String>>

    @GET("/languageLeaderBoard/{selectedLanguage}")
    suspend fun languageLeaderBoard(@Path("selectedLanguage") selectedLanguage: String): Response<List<String>>

    @GET("/hasAttemptedToday/{selectedLanguage}")
    suspend fun hasAttemptedToday(@Path("selectedLanguage") selectedLanguage: String): Response<Map<String, Boolean>>

    @GET("/lastSevenDays/{username}")
    suspend fun lastSevenDays(@Path("username") username: String): Response<List<Boolean>>

    @GET("/data/updateAll")
    suspend fun updateAll(): Response<Unit>

    @GET("/questionsSolved/{username}")
    suspend fun questionsSolved(@Path("username") username: String): Response<List<String>>

    @GET("/nameAndLanguage/{username}")
    suspend fun nameAndLanguage(@Path("username") username: String): Response<List<String>>

    @GET("/questionsCount/{selectedLanguage}")
    suspend fun questionsCount(@Path("selectedLanguage") selectedLanguage: String): Response<Map<String, Int>>
}