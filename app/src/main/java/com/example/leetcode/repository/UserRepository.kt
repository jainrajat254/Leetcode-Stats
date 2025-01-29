package com.example.leetcode.repository

import android.util.Log
import com.example.leetcode.data.LoginCredentials
import com.example.leetcode.data.LoginResponse
import com.example.leetcode.data.UserData
import com.example.leetcode.retrofit.UserService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val userService: UserService,
) {

    suspend fun registerUser(user: UserData): UserData {
        val response = userService.registerUser(user)
        if (response.isSuccessful) {
            return response.body() ?: throw Exception("Failed to create user: No body in response")
        } else {
            throw Exception("Error creating user: ${response.code()}")
        }
    }

    suspend fun loginUser(user: LoginCredentials): LoginResponse {
        try {
            val response = userService.loginUser(user)
            if (response.isSuccessful) {
                val userResponse = response.body()
                    ?: throw Exception("Failed to load user - Response body is null")
                return userResponse
            } else {
                throw Exception("Login failed: ${response.code()} - ${response.message()}")
            }
        } catch (e: Exception) {
            throw Exception("Login failed due to error: ${e.message}")
        }
    }

    suspend fun clubLeaderBoard(): List<String> {
        return try {
            val response = userService.clubLeaderBoard()
            if (response.isSuccessful) {
                response.body() ?: throw Exception("Failed to load users - Response body is null")
            } else {
                throw Exception("Failed to fetch users: ${response.code()} - ${response.message()}")
            }
        } catch (e: Exception) {
            throw Exception("An error occurred while fetching users: ${e.localizedMessage}", e)
        }
    }

    suspend fun languageLeaderBoard(selectedLanguage: String): List<String> {
        return try {
            val response = userService.languageLeaderBoard(selectedLanguage = selectedLanguage)
            if (response.isSuccessful) {
                response.body() ?: throw Exception("Failed to load users - Response body is null")
            } else {
                throw Exception("Failed to fetch users: ${response.code()} - ${response.message()}")
            }
        } catch (e: Exception) {
            throw Exception("An error occurred while fetching users: ${e.localizedMessage}", e)
        }
    }

    suspend fun hasAttemptedToday(selectedLanguage: String): Map<String, Boolean> {
        return try {
            val response = userService.hasAttemptedToday(selectedLanguage)
            if (response.isSuccessful) {
                response.body() ?: throw Exception("Failed to load data - Response body is null")
            } else {
                throw Exception("Failed to fetch data: ${response.code()} - ${response.message()}")
            }
        } catch (e: Exception) {
            throw Exception("An error occurred while fetching data: ${e.localizedMessage}", e)
        }
    }

    suspend fun questionsCount(selectedLanguage: String): Map<String, Int> {
        return try {
            val response = userService.questionsCount(selectedLanguage)
            if (response.isSuccessful) {
                response.body() ?: throw Exception("Failed to load data - Response body is null")
            } else {
                throw Exception("Failed to fetch data: ${response.code()} - ${response.message()}")
            }
        } catch (e: Exception) {
            throw Exception("An error occurred while fetching data: ${e.localizedMessage}", e)
        }
    }

    suspend fun lastSevenDays(username: String): List<Boolean> {
        return try {
            val response = userService.lastSevenDays(username)
            Log.d("API Response", response.toString())
            if (response.isSuccessful) {
                response.body() ?: throw Exception("Failed to load data - Response body is null")
            } else {
                throw Exception("Failed to fetch data: ${response.code()} - ${response.message()}")
            }
        } catch (e: Exception) {
            throw Exception("An error occurred while fetching data: ${e.localizedMessage}", e)
        }
    }

    suspend fun questionsSolved(username: String): List<String> {
        return try {
            val response = userService.questionsSolved(username)
            if (response.isSuccessful) {
                response.body() ?: throw Exception("Failed to load data - Response body is null")
            } else {
                throw Exception("Failed to fetch data: ${response.code()} - ${response.message()}")
            }
        } catch (e: Exception) {
            throw Exception("An error occurred while fetching data: ${e.localizedMessage}", e)
        }
    }

    suspend fun updateAll() {
        return try {
            val response = userService.updateAll()
            if (response.isSuccessful) {
                response.body() ?: throw Exception("Failed to load data - Response body is null")
            } else {
                throw Exception("Failed to fetch data: ${response.code()} - ${response.message()}")
            }
        } catch (e: Exception) {
            throw Exception("An error occurred while fetching data: ${e.localizedMessage}", e)
        }
    }

    suspend fun nameAndLanguage(username: String): List<String> {
        return try {
            val response = userService.nameAndLanguage(username)
            if (response.isSuccessful) {
                response.body() ?: throw Exception("Failed to load data - Response body is null")
            } else {
                throw Exception("Failed to fetch data: ${response.code()} - ${response.message()}")
            }
        } catch (e: Exception) {
            throw Exception("An error occurred while fetching data: ${e.localizedMessage}", e)
        }
    }
}