package com.example.leetcode.retrofit

import com.example.leetcode.data.Contest
import com.example.leetcode.data.EditDetails
import com.example.leetcode.data.EditPassword
import com.example.leetcode.data.LeaderBoard
import com.example.leetcode.data.LoginCredentials
import com.example.leetcode.data.LoginResponse
import com.example.leetcode.data.Socials
import com.example.leetcode.data.UserData
import com.example.leetcode.data.Stats
import com.example.leetcode.data.StreakContent
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface UserService {

    @POST("/register")
    suspend fun registerUser(@Body user: UserData): Response<UserData>

    @POST("/login")
    suspend fun loginUser(@Body user: LoginCredentials): Response<LoginResponse>

    @GET("/clubLeaderBoard")
    suspend fun clubLeaderBoard(): Response<List<LeaderBoard>>

    @GET("/languageLeaderBoard/{selectedLanguage}")
    suspend fun languageLeaderBoard(@Path("selectedLanguage") selectedLanguage: String): Response<List<LeaderBoard>>

    @GET("/hasAttemptedToday/{selectedLanguage}")
    suspend fun hasAttemptedToday(@Path("selectedLanguage") selectedLanguage: String): Response<List<StreakContent>>

    @GET("/lastThirtyDays/{username}")
    suspend fun lastThirtyDays(@Path("username") username: String): Response<List<Boolean>>

    @GET("/data/updateAll")
    suspend fun updateAll(): Response<Unit>

    @GET("/data/updateUser/{username}")
    suspend fun updateUser(@Path("username") username: String): Response<Unit>

    @GET("/questionsSolved/{username}")
    suspend fun questionsSolved(@Path("username") username: String): Response<List<String>>

    @GET("/nameAndLanguage/{username}")
    suspend fun nameAndLanguage(@Path("username") username: String): Response<List<String>>

    @GET("/questionsCount/{selectedLanguage}")
    suspend fun questionsCount(@Path("selectedLanguage") selectedLanguage: String): Response<List<Stats>>

    @GET("/getUserSocials/{username}")
    suspend fun getUserSocials(@Path("username") username: String): Response<Socials>

    @GET("/getUserProfile/{username}")
    suspend fun getUserProfile(@Path("username") username: String): Response<Socials>

    @GET("/getContestInfo/{username}")
    suspend fun getContestInfo(@Path("username") username: String): Response<Contest>

    @GET("/isValidUser/{username}")
    suspend fun isValidUser(@Path("username") username: String): Response<Map<String, String>>

    @PUT("/editPassword/{id}")
    suspend fun editPassword(@Path("id") id: String, @Body data: EditPassword): Response<String>

    @PUT("/editDetails/{id}")
    suspend fun editDetails(@Path("id") id: String, @Body data: EditDetails): Response<LoginResponse>
}