package com.example.leetcode.sharedPreferences

import android.content.Context
import android.content.SharedPreferences
import com.example.leetcode.data.EditDetails
import com.example.leetcode.data.LoginResponse

object SharedPreferencesManager {

    private const val PREFS_NAME = "UserPrefs"
    private const val USER_ID = "id"
    private const val USER_NAME_KEY = "user_name"
    private const val USER_LANGUAGE_KEY = "user_language"
    private const val USER_PASSWORD = "user_password"
    private const val USER_YEAR = "user_year"
    private const val USER_USERNAME_KEY = "user_username"
    private const val IS_LOGGED_IN_KEY = "IS_LOGGED_IN"
    private const val JWT_TOKEN_KEY = "jwt_token"

    private lateinit var sharedPreferences: SharedPreferences

    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun saveUser(loginResponse: LoginResponse) {
        sharedPreferences.edit().apply {
            putString(USER_NAME_KEY, loginResponse.name)
            putString(USER_ID, loginResponse.id)
            putString(USER_LANGUAGE_KEY, loginResponse.selectedLanguage)
            putString(USER_USERNAME_KEY, loginResponse.username)
            putString(USER_YEAR, loginResponse.year)
            putString(JWT_TOKEN_KEY, loginResponse.token)
            putBoolean(IS_LOGGED_IN_KEY, true)
            apply()
        }
    }

    fun getJwtToken(): String? = sharedPreferences.getString(JWT_TOKEN_KEY, null)

    fun getUser(): LoginResponse? {
        val name = sharedPreferences.getString(USER_NAME_KEY, null)
        val id = sharedPreferences.getString(USER_ID, null)
        val language = sharedPreferences.getString(USER_LANGUAGE_KEY, null)
        val year = sharedPreferences.getString(USER_YEAR, null)
        val username = sharedPreferences.getString(USER_USERNAME_KEY, null)
        val token = sharedPreferences.getString(JWT_TOKEN_KEY, null)

        return if (name != null && id != null && language != null && year != null && username != null && token != null) {
            LoginResponse(name, id, language, year, username, token)
        } else null
    }

    fun getUsername(): String? {
        val username = sharedPreferences.getString(USER_USERNAME_KEY, null)
        return username
    }

    fun saveUserDetails(details: EditDetails) {
        sharedPreferences.edit().apply {
            putString(USER_NAME_KEY, details.name)
            putString(USER_USERNAME_KEY, details.username)
            putString(USER_YEAR, details.year)
            putString(USER_LANGUAGE_KEY, details.selectedLanguage)
            apply()
        }
    }

    fun saveUserPassword(password: String) {
        sharedPreferences.edit().apply {
            putString(USER_PASSWORD, password)
            apply()
        }
    }

    fun isLoggedIn(): Boolean = sharedPreferences.getBoolean(IS_LOGGED_IN_KEY, false)

    fun clearUserData() {
        sharedPreferences.edit().clear().apply()
    }
}
