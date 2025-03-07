package com.example.leetcode.models

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.leetcode.data.LeaderBoard
import com.example.leetcode.data.LoginCredentials
import com.example.leetcode.data.LoginResponse
import com.example.leetcode.data.Socials
import com.example.leetcode.data.UserData
import com.example.leetcode.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.isActive
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

    suspend fun clubLeaderBoard(): List<LeaderBoard> {
        try {
            return userRepository.clubLeaderBoard()
        } catch (e: Exception) {
            throw Exception("Error fetching Club Leaderboard: ${e.message}", e)
        }
    }


    suspend fun languageLeaderBoard(selectedLanguage: String): List<LeaderBoard> {
        try {
            return userRepository.languageLeaderBoard(selectedLanguage)
        } catch (e: Exception) {
            throw Exception("Error fetching Language Leaderboard: ${e.message}", e)
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

    fun updateUser(username: String) {
        viewModelScope.launch {
            try {
                Log.d("ViewModel", "Updating user: $username")
                userRepository.updateUser(username)
                Log.d("ViewModel", "User update successful: $username")
            } catch (e: Exception) {
                Log.e("ViewModel", "Error updating user", e)
            }
        }
    }


    suspend fun nameAndLanguage(username: String): List<String> {
        try {
            return userRepository.nameAndLanguage(username)
        } catch (e: Exception) {
            throw Exception("Error fetching name and language for $username: ${e.message}", e)
        }
    }

    suspend fun getUserSocials(username: String): Socials {
        return coroutineScope { // Ensures we are inside a coroutine scope
            try {
                if (!isActive) throw CancellationException("Coroutine cancelled")
                userRepository.getUserSocials(username) // Example API Call
            } catch (e: CancellationException) {
                throw e // Let coroutine cancellation propagate
            } catch (e: Exception) {
                Log.e("ViewModel", "Error fetching socials: ${e.message}")
                Socials("", "", "", "")
            }
        }
    }

    suspend fun getUserProfile(username: String): Socials = coroutineScope {
        val deferredSocials = async { userRepository.getUserProfile(username) } // Runs in parallel
        try {
            deferredSocials.await() // Wait for the result
        } catch (e: Exception) {
            throw Exception("Error fetching profile for $username: ${e.message}", e)
        }
    }

}
