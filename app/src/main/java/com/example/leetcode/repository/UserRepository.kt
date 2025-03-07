package com.example.leetcode.repository

import android.util.Log
import com.example.leetcode.data.LeaderBoard
import com.example.leetcode.data.LoginCredentials
import com.example.leetcode.data.LoginResponse
import com.example.leetcode.data.Socials
import com.example.leetcode.data.UserData
import com.example.leetcode.retrofit.UserService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val userService: UserService
) {

    suspend fun registerUser(user: UserData): UserData = withContext(Dispatchers.IO) {
        val response = userService.registerUser(user)
        if (response.isSuccessful) {
            response.body() ?: throw Exception("Failed to create user: No body in response")
        } else {
            throw Exception("Error creating user: ${response.code()}")
        }
    }

    suspend fun loginUser(user: LoginCredentials): LoginResponse = withContext(Dispatchers.IO) {
        val response = userService.loginUser(user)
        if (response.isSuccessful) {
            response.body() ?: throw Exception("Failed to load user - Response body is null")
        } else {
            throw Exception("Login failed: ${response.code()} - ${response.message()}")
        }
    }

    suspend fun clubLeaderBoard(): List<LeaderBoard> = withContext(Dispatchers.IO) {
        val response = userService.clubLeaderBoard()
        Log.d("Thread Name", Thread.currentThread().name)
        if (response.isSuccessful) {
            response.body() ?: throw Exception("Failed to load users - Response body is null")
        } else {
            throw Exception("Failed to fetch users: ${response.code()} - ${response.message()}")
        }
    }

    suspend fun questionsCount(selectedLanguage: String): Map<String, Int> = withContext(Dispatchers.IO) {
        val response = userService.questionsCount(selectedLanguage)
        if (response.isSuccessful) {
            response.body() ?: throw Exception("Failed to load data - Response body is null")
        } else {
            throw Exception("Failed to fetch data: ${response.code()} - ${response.message()}")
        }
    }

    suspend fun languageLeaderBoard(selectedLanguage: String): List<LeaderBoard> = withContext(Dispatchers.IO) {
        val response = userService.languageLeaderBoard(selectedLanguage)
        if (response.isSuccessful) {
            response.body() ?: throw Exception("Failed to load users - Response body is null")
        } else {
            throw Exception("Failed to fetch users: ${response.code()} - ${response.message()}")
        }
    }

    suspend fun hasAttemptedToday(selectedLanguage: String): Map<String, Boolean> = withContext(Dispatchers.IO) {
        val response = userService.hasAttemptedToday(selectedLanguage)
        if (response.isSuccessful) {
            response.body() ?: throw Exception("Failed to load data - Response body is null")
        } else {
            throw Exception("Failed to fetch data: ${response.code()} - ${response.message()}")
        }
    }

    suspend fun lastSevenDays(username: String): List<Boolean> = withContext(Dispatchers.IO) {
        val response = userService.lastSevenDays(username)
        if (response.isSuccessful) {
            response.body() ?: throw Exception("Failed to load data - Response body is null")
        } else {
            throw Exception("Failed to fetch data: ${response.code()} - ${response.message()}")
        }
    }

    suspend fun questionsSolved(username: String): List<String> = withContext(Dispatchers.IO) {
        val response = userService.questionsSolved(username)
        if (response.isSuccessful) {
            response.body() ?: throw Exception("Failed to load data - Response body is null")
        } else {
            throw Exception("Failed to fetch data: ${response.code()} - ${response.message()}")
        }
    }

    suspend fun updateAll() = withContext(Dispatchers.IO) {
        val response = userService.updateAll()
        if (!response.isSuccessful) {
            throw Exception("Failed to update: ${response.code()} - ${response.message()}")
        }
    }

    suspend fun updateUser(username: String) = withContext(Dispatchers.IO) {
        val response = userService.updateUser(username)
        if (!response.isSuccessful) {
            throw Exception("Failed to update user: ${response.code()} - ${response.message()}")
        }
    }

    suspend fun nameAndLanguage(username: String): List<String> = withContext(Dispatchers.IO) {
        val response = userService.nameAndLanguage(username)
        if (response.isSuccessful) {
            response.body() ?: throw Exception("Failed to load data - Response body is null")
        } else {
            throw Exception("Failed to fetch data: ${response.code()} - ${response.message()}")
        }
    }

    suspend fun getUserSocials(username: String): Socials = withContext(Dispatchers.IO) {
        val response = userService.getUserSocials(username)
        if (response.isSuccessful) {
            response.body() ?: throw Exception("Failed to load data - Response body is null")
        } else {
            throw Exception("Failed to fetch data: ${response.code()} - ${response.message()}")
        }
    }

    suspend fun getUserProfile(username: String): Socials = withContext(Dispatchers.IO) {
        val response = userService.getUserProfile(username)
        if (response.isSuccessful) {
            response.body() ?: throw Exception("Failed to load data - Response body is null")
        } else {
            throw Exception("Failed to fetch data: ${response.code()} - ${response.message()}")
        }
    }
}
