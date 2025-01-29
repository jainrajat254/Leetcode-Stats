package com.example.leetcode.models

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.leetcode.data.LoginCredentials
import com.example.leetcode.data.LoginResponse
import com.example.leetcode.data.UserData
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
                userRepository.registerUser(user)
                onSuccess()
            } catch (e: HttpException) {
                Log.d("ViewModel", e.toString())
                onError("HTTP Error during registration: ${e.message()}")
            } catch (e: Exception) {
                Log.d("ViewModel", e.toString())
                onError("Unexpected error during registration: ${e.message}")
            }
        }
    }

    fun loginUser(
        user: LoginCredentials,
        onSuccess: (LoginResponse) -> Unit,
        onError: (String) -> Unit,
    ) {
        viewModelScope.launch {
            try {
                val userResponse = userRepository.loginUser(user)
                onSuccess(userResponse)
            } catch (e: HttpException) {
                Log.d("ViewModel", e.toString())
                onError("HTTP Error during login: ${e.message()}")
            } catch (e: Exception) {
                Log.d("ViewModel", e.toString())
                onError("Unexpected error during login: ${e.message}")
            }
        }
    }

    suspend fun clubLeaderBoard(): List<String> {
        try {
            return userRepository.clubLeaderBoard()
        } catch (e: Exception) {
            throw Exception("Error fetching club leaderboard: ${e.message}", e)
        }
    }

    suspend fun languageLeaderBoard(selectedLanguage: String): List<String> {
        try {
            return userRepository.languageLeaderBoard(selectedLanguage)
        } catch (e: Exception) {
            throw Exception(
                "Error fetching language leaderboard for $selectedLanguage: ${e.message}",
                e
            )
        }
    }

    suspend fun hasAttemptedToday(selectedLanguage: String): Map<String, Boolean> {
        try {
            return userRepository.hasAttemptedToday(selectedLanguage)
        } catch (e: Exception) {
            throw Exception(
                "Error checking today's attempts for $selectedLanguage: ${e.message}",
                e
            )
        }
    }

    suspend fun questionsCount(selectedLanguage: String): Map<String, Int> {
        try {
            return userRepository.questionsCount(selectedLanguage)
        } catch (e: Exception) {
            throw Exception("Error fetching question count for $selectedLanguage: ${e.message}", e)
        }
    }

    suspend fun lastSevenDays(username: String): List<Boolean> {
        return try {
            userRepository.lastSevenDays(username)
        } catch (e: Exception) {
            Log.e(
                "UserViewModel",
                "Error fetching last seven days data for $username: ${e.message}"
            )
            emptyList()
        }
    }

    suspend fun questionsSolved(username: String): List<String> {
        try {
            return userRepository.questionsSolved(username)
        } catch (e: Exception) {
            throw Exception("Error fetching solved questions for $username: ${e.message}", e)
        }
    }

    suspend fun updateAll() {
        try {
            userRepository.updateAll()
        } catch (e: Exception) {
            throw Exception("Error updating all data: ${e.message}", e)
        }
    }

    suspend fun nameAndLanguage(username: String): List<String> {
        try {
            return userRepository.nameAndLanguage(username)
        } catch (e: Exception) {
            throw Exception("Error fetching name and language for $username: ${e.message}", e)
        }
    }
}
