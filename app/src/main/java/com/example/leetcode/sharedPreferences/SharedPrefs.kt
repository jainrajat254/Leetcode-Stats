package com.example.leetcode.sharedPreferences

import android.content.Context
import com.example.leetcode.data.LoginResponse

object SharedPreferencesManager {

    private const val PREFS_NAME = "UserPrefs"
    private const val USER_NAME_KEY = "user_name"
    private const val USER_LANGUAGE_KEY = "user_language"
    private const val USER_USERNAME_KEY = "user_username"
    private const val IS_LOGGED_IN_KEY = "IS_LOGGED_IN"
    private const val JWT_TOKEN_KEY = "jwt_token"

    fun saveUserToPreferences(context: Context, loginResponse: LoginResponse) {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit().apply {
            putString(USER_NAME_KEY, loginResponse.name)
            putString(USER_LANGUAGE_KEY, loginResponse.selectedLanguage)
            putString(USER_USERNAME_KEY, loginResponse.username)
            putString(JWT_TOKEN_KEY, loginResponse.token)
            putBoolean(IS_LOGGED_IN_KEY, true)
            apply()
        }
    }

    fun getJwtToken(context: Context): String? {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString(JWT_TOKEN_KEY, null)
    }

    fun getUserFromPreferences(context: Context): LoginResponse? {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val userName = sharedPreferences.getString(USER_NAME_KEY, null)
        val userLanguage = sharedPreferences.getString(USER_LANGUAGE_KEY, null)
        val userUsername = sharedPreferences.getString(USER_USERNAME_KEY, null)
        val userToken = sharedPreferences.getString(JWT_TOKEN_KEY, null)

        return if (userName != null && userLanguage != null && userUsername != null && userToken != null) {
            LoginResponse(userName, userLanguage, userUsername, userToken)
        } else {
            null
        }
    }

    fun clearUserData(context: Context) {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit().apply {
            clear()
            apply()
        }
    }
}
