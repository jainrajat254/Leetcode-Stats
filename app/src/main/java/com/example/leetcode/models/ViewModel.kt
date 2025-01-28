package com.example.leetcode.models

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.leetcode.data.LeetcodeStats
import com.example.leetcode.data.LoginCredentials
import com.example.leetcode.data.LoginResponse
import com.example.leetcode.data.UserData
import com.example.leetcode.data.UserStats
import com.example.leetcode.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class ViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : ViewModel() {

    fun registerUser(
        user: UserData,
        onSuccess: () -> Unit,
        onError: (String) -> Unit,
    ) {

        viewModelScope.launch {
            try {
                val newUser = userRepository.registerUser(user)
                onSuccess()
            } catch (e: HttpException) {
                Log.d("ViewModel", e.toString())
                onError("HTTP Exception: ${e.message()}")
            } catch (e: Exception) {
                Log.d("ViewModel", e.toString())
                onError("Unexpected Error: ${e.message}")
            }
        }
    }

    fun loginUser(
        user: LoginCredentials,
        onSuccess: (LoginResponse) -> Unit,  // Expecting a UserResponse
        onError: (String) -> Unit,
    ) {
        viewModelScope.launch {
            try {
                // Call the repository method that returns a UserResponse
                val userResponse = userRepository.loginUser(user)
                onSuccess(userResponse)  // Pass the UserResponse to onSuccess callback
            } catch (e: HttpException) {
                Log.d("ViewModel", e.toString())
                onError("HTTP Exception: ${e.message()}")
            } catch (e: Exception) {
                Log.d("ViewModel", e.toString())
                onError("Unexpected Error: ${e.message}")
            }
        }
    }

    suspend fun getUsers(
    ): List<String> {
        try {
            return userRepository.getUsers()
        } catch (e: Exception) {
            throw Exception("Error loading users by language: ${e.message}", e)
        }
    }

    suspend fun clubLeaderBoard(
    ): List<String> {
        try {
            return userRepository.clubLeaderBoard()
        } catch (e: Exception) {
            throw Exception("Error loading users by language: ${e.message}", e)
        }
    }

    suspend fun languageLeaderBoard(selectedLanguage: String): List<String> {
        try {
            return userRepository.languageLeaderBoard(selectedLanguage)
        } catch (e: Exception) {
            throw Exception("Error loading users by language: ${e.message}", e)
        }
    }

    suspend fun getUserStats(username: String): LeetcodeStats? {
        try {
            val stats = userRepository.getUserStats(username)
            return stats  // Return the LeetcodeStats object
        } catch (e: HttpException) {
            Log.e("ViewModel", "HTTP Exception: ${e.message}")
            throw e
        } catch (e: Exception) {
            Log.e("ViewModel", "Exception: ${e.message}")
            throw e
        }
    }

    suspend fun fetchUserStats(username: String): UserStats? {
        try {
            val stats = userRepository.fetchUserStats(username)
            return stats
        } catch (e: HttpException) {
            Log.e("ViewModel", "HTTP Exception: ${e.message}")
            throw e
        } catch (e: Exception) {
            Log.e("ViewModel", "Exception: ${e.message}")
            throw e
        }
    }


    suspend fun getUserByLanguage(selectedLanguage: String): List<String> {
        try {
            return userRepository.getUserByLanguage(selectedLanguage)
        } catch (e: Exception) {
            throw Exception("Error loading users by language: ${e.message}", e)
        }
    }

    suspend fun hasAttemptedToday(
        selectedLanguage: String,
    ): Map<String, Boolean> {
        try {
            return userRepository.hasAttemptedToday(selectedLanguage)
        } catch (e: Exception) {
            throw Exception("Error loading users by language: ${e.message}", e)
        }
    }

    suspend fun questionsCount(
        selectedLanguage: String,
    ): Map<String, Int> {
        try {
            return userRepository.questionsCount(selectedLanguage)
        } catch (e: Exception) {
            throw Exception("Error loading users by language: ${e.message}", e)
        }
    }

    suspend fun lastSevenDays(username: String): List<Boolean> {
        return try {
            userRepository.lastSevenDays(username)
        } catch (e: Exception) {
            Log.e("UserViewModel", "Error fetching last seven days data: ${e.message}")
            emptyList() // Return an empty list in case of error
        }
    }

    suspend fun questionsSolved(
        username: String,
    ): List<String> {
        try {
            return userRepository.questionsSolved(username)
        } catch (e: Exception) {
            throw Exception("Error loading users by language: ${e.message}", e)
        }
    }

    suspend fun updateAll() {
        try {
            userRepository.updateAll()
        } catch (e: Exception) {
            throw Exception("Error loading users by language: ${e.message}", e)
        }
    }
}
