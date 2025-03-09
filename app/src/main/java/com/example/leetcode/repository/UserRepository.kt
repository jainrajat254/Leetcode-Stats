package com.example.leetcode.repository

import com.example.leetcode.data.*
import com.example.leetcode.retrofit.UserService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val userService: UserService,
) {

    private suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>): T {
        return withContext(Dispatchers.IO) {
            val response = apiCall()
            if (response.isSuccessful) {
                response.body() ?: throw Exception("API response is empty")
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Unknown error"
                throw Exception(errorMessage)
            }
        }
    }

    suspend fun registerUser(user: UserData): UserData =
        safeApiCall { userService.registerUser(user) }

    suspend fun loginUser(user: LoginCredentials): LoginResponse =
        safeApiCall { userService.loginUser(user) }

    suspend fun clubLeaderBoard(): List<LeaderBoard> = safeApiCall { userService.clubLeaderBoard() }
    suspend fun questionsCount(selectedLanguage: String): List<Stats> =
        safeApiCall { userService.questionsCount(selectedLanguage) }

    suspend fun languageLeaderBoard(selectedLanguage: String): List<LeaderBoard> =
        safeApiCall { userService.languageLeaderBoard(selectedLanguage) }

    suspend fun hasAttemptedToday(selectedLanguage: String): List<StreakContent> =
        safeApiCall { userService.hasAttemptedToday(selectedLanguage) }

    suspend fun lastThirtyDays(username: String): List<Boolean> =
        safeApiCall { userService.lastThirtyDays(username) }

    suspend fun questionsSolved(username: String): List<String> =
        safeApiCall { userService.questionsSolved(username) }

    suspend fun updateAll() = safeApiCall { userService.updateAll() }
    suspend fun updateUser(username: String) = safeApiCall { userService.updateUser(username) }
    suspend fun nameAndLanguage(username: String): List<String> =
        safeApiCall { userService.nameAndLanguage(username) }

    suspend fun getUserSocials(username: String): Socials =
        safeApiCall { userService.getUserSocials(username) }

    suspend fun getUserProfile(username: String): Socials =
        safeApiCall { userService.getUserProfile(username) }

    suspend fun editPassword(data: EditPassword, userId: String): Result<String> {
        return withContext(Dispatchers.IO) {
            try {
                val response = userService.editPassword(userId, data)
                if (response.isSuccessful) {
                    Result.success(response.body() ?: "Password updated successfully")
                } else {
                    Result.failure(
                        Exception(
                            response.errorBody()?.string() ?: "Password update failed"
                        )
                    )
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun userDetails(data: EditDetails, userId: String): Result<String> {
        return withContext(Dispatchers.IO) {
            try {
                val response = userService.userDetails(userId, data)
                if (response.isSuccessful) {
                    Result.success(response.body() ?: "Details updated successfully")
                } else {
                    Result.failure(
                        Exception(
                            response.errorBody()?.string() ?: "Update failed"
                        )
                    )
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

}
