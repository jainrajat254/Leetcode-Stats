package com.example.leetcode.repository

import android.util.Log
import com.example.leetcode.data.LoginCredentials
import com.example.leetcode.data.UserData
import com.example.leetcode.data.LoginResponse
import com.example.leetcode.data.LeetcodeStats
import com.example.leetcode.data.UserStats
import com.example.leetcode.retrofit.UserService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val userService: UserService
) {
    suspend fun registerUser(user: UserData): UserData {
        val response = userService.registerUser(user)
        if (response.isSuccessful) {
            return response.body() ?: throw Exception("Failed to create user: No body in response")
        } else {
            throw Exception("Error creating user: ${response.code()}")
        }
    }

    // Repository method
    suspend fun loginUser(user: LoginCredentials): LoginResponse {
        try {
            // Make the API call
            val response = userService.loginUser(user)

            // Check if the response is successful
            if (response.isSuccessful) {
                val userResponse = response.body()
                    ?: throw Exception("Failed to load user - Response body is null")
                return userResponse  // Returning UserResponse
            } else {
                throw Exception("Login failed: ${response.code()} - ${response.message()}")
            }
        } catch (e: Exception) {
            throw Exception("Login failed due to error: ${e.message}")
        }
    }



    suspend fun getUserStats(username: String): LeetcodeStats? {
        return withContext(Dispatchers.IO) {
            val response = userService.getUserStats(username)
            if (response.isSuccessful) {
                response.body() // Return the UserStats if the response is successful
            } else {
                null // Handle failure (e.g., user not found)
            }
        }
    }

    suspend fun fetchUserStats(username: String): UserStats? {
        return withContext(Dispatchers.IO) {
            val response = userService.fetchUserStats(username)
            if (response.isSuccessful) {
                response.body() // Return the UserStats if the response is successful
            } else {
                null // Handle failure (e.g., user not found)
            }
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

    suspend fun getUserByLanguage(selectedLanguage: String): List<String> {
        return try {
            val response = userService.getUserByLanguage(selectedLanguage)
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

    suspend fun getUsers(): List<String> {
        return try {
            val response = userService.getUsers()
            if (response.isSuccessful) {
                response.body() ?: throw Exception("Failed to load users - Response body is null")
            } else {
                throw Exception("Failed to fetch users: ${response.code()} - ${response.message()}")
            }
        } catch (e: Exception) {
            throw Exception("An error occurred while fetching users: ${e.localizedMessage}", e)
        }
    }

        suspend fun lastSevenDays(username: String): List<Boolean> {
            return try {
                val response = userService.lastSevenDays(username)
                Log.d("API Response", response.toString()) // Log the raw response
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


}