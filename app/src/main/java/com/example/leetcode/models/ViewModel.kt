package com.example.leetcode.models

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.leetcode.data.Contest
import com.example.leetcode.data.EditDetails
import com.example.leetcode.data.EditPassword
import com.example.leetcode.data.LeaderBoard
import com.example.leetcode.data.LoginCredentials
import com.example.leetcode.data.LoginResponse
import com.example.leetcode.data.Socials
import com.example.leetcode.data.Stats
import com.example.leetcode.data.StreakContent
import com.example.leetcode.data.UserData
import com.example.leetcode.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> get() = _errorMessage

    fun registerUser(user: UserData, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                userRepository.registerUser(user)
                onSuccess()
            } catch (e: Exception) {
                val message = extractErrorMessage(e)
                _errorMessage.value = message
                onError(message)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loginUser(
        user: LoginCredentials,
        onSuccess: (LoginResponse) -> Unit,
        onError: (String) -> Unit,
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val userResponse = userRepository.loginUser(user)
                onSuccess(userResponse)
            } catch (e: Exception) {
                val message = extractErrorMessage(e)
                _errorMessage.value = message
                onError(message)
            } finally {
                _isLoading.value = false
            }
        }
    }

    suspend fun clubLeaderBoard(): List<LeaderBoard> =
        handleApiCall { userRepository.clubLeaderBoard() }

    suspend fun languageLeaderBoard(lang: String): List<LeaderBoard> =
        handleApiCall { userRepository.languageLeaderBoard(lang) }

    suspend fun hasAttemptedToday(lang: String): List<StreakContent> =
        handleApiCall { userRepository.hasAttemptedToday(lang) }

    suspend fun questionsCount(lang: String): List<Stats> =
        handleApiCall { userRepository.questionsCount(lang) }

    suspend fun lastThirtyDays(username: String): List<Boolean> =
        handleApiCall { userRepository.lastThirtyDays(username) }

    suspend fun questionsSolved(username: String): List<String> =
        handleApiCall { userRepository.questionsSolved(username) }

    suspend fun nameAndLanguage(username: String): List<String> =
        handleApiCall { userRepository.nameAndLanguage(username) }

    suspend fun getUserSocials(username: String): Socials =
        handleApiCall { userRepository.getUserSocials(username) }

    suspend fun getContestInfo(username: String): Contest =
        handleApiCall { userRepository.getContestInfo(username) }

    suspend fun getUserProfile(username: String): Socials =
        handleApiCall { userRepository.getUserProfile(username) }

    fun isValidUser(username: String, onResult: (Map<String, String>) -> Unit) {
        viewModelScope.launch {
            val response = userRepository.isValidUser(username)
            onResult(response) // Return response directly (message or error)
        }
    }

    fun updateUser(username: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                userRepository.updateUser(username)
            } catch (e: Exception) {
                _errorMessage.value = extractErrorMessage(e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateAll() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                userRepository.updateAll()
            } catch (e: Exception) {
                _errorMessage.value = extractErrorMessage(e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    private suspend fun <T> handleApiCall(apiCall: suspend () -> T): T {
        return withContext(Dispatchers.IO) {
            try {
                _isLoading.value = true
                apiCall()
            } catch (e: Exception) {
                _errorMessage.value = extractErrorMessage(e)
                throw e
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun extractErrorMessage(e: Exception): String {
        return when (e) {
            is HttpException -> e.response()?.errorBody()?.string() ?: "Network error occurred"
            is CancellationException -> "Request was cancelled"
            else -> e.localizedMessage ?: "An unexpected error occurred"
        }
    }

    fun editPassword(
        data: EditPassword,
        userId: String,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit,
    ) {
        viewModelScope.launch {
            try {
                val response: Result<String> = userRepository.editPassword(data, userId)

                Log.d("editPassword", "Response: $response")  // Debug log

                response.onSuccess { message ->
                    Log.d("editPassword", "Success: $message")
                    onSuccess(message)
                }.onFailure { exception ->
                    Log.d("editPassword", "Failure: ${exception.message}") // Debug log
                    onError(exception.message ?: "Error occurred during password update")
                }
            } catch (e: Exception) {
                Log.e("editPassword", "Unexpected error", e)
                onError("Unexpected error: ${e.message}")
            }
        }
    }

    fun editDetails(
        data: EditDetails,
        userId: String,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit,
    ) {
        viewModelScope.launch {
            try {
                userRepository.editDetails(data = data, userId = userId)
                onSuccess("Profile updated successfully")
            } catch (e: Exception) {
                Log.e("editPassword", "Unexpected error", e)
                onError(e.message ?: "Unexpected error occurred")
            } finally {
            }
        }
    }

}
