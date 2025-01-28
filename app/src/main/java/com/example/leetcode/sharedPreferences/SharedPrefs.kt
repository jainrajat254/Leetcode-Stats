package com.example.leetcode.sharedPreferences

import android.content.Context
import com.example.leetcode.data.LoginResponse

object SharedPreferencesManager {

    private const val PREFS_NAME = "UserPrefs"
    private const val USER_NAME_KEY = "user_name"
    private const val USER_LANGUAGE_KEY = "user_language"
    private const val USER_USERNAME_KEY = "user_username"
    private const val IS_LOGGED_IN_KEY = "IS_LOGGED_IN"

    // Save user data and login status to SharedPreferences
    fun saveUserToPreferences(context: Context, loginResponse: LoginResponse) {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putString(USER_NAME_KEY, loginResponse.name)
            putString(USER_LANGUAGE_KEY, loginResponse.selectedLanguage)
            putString(USER_USERNAME_KEY, loginResponse.username)
            putBoolean(IS_LOGGED_IN_KEY, true)  // Mark user as logged in
            apply()
        }
    }

    // Get user data from SharedPreferences
    fun getUserFromPreferences(context: Context): LoginResponse? {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val userName = sharedPreferences.getString(USER_NAME_KEY, null)
        val userLanguage = sharedPreferences.getString(USER_LANGUAGE_KEY, null)
        val userUsername = sharedPreferences.getString(USER_USERNAME_KEY, null)

        return if (userName != null && userLanguage != null && userUsername != null) {
            LoginResponse(userName, userLanguage, userUsername)
        } else {
            null
        }
    }

    // Save login status (whether the user is logged in or not)
    fun saveLoginStatus(context: Context, isLoggedIn: Boolean) {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putBoolean(IS_LOGGED_IN_KEY, isLoggedIn)
            apply()
        }
    }

    // Get login status from SharedPreferences
    fun getLoginStatus(context: Context): Boolean {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(IS_LOGGED_IN_KEY, false)
    }

    // Clear all stored data (e.g., on logout)
    fun clearUserData(context: Context) {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            clear()  // Clears all stored preferences
            apply()
        }
    }
}
