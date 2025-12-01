package com.thais.app_gymflow.views.utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

class SessionManager(context: Context) {

    private var prefs: SharedPreferences = context.getSharedPreferences("app_gymflow", Context.MODE_PRIVATE)
    private var appContext: Context = context

    companion object {
        const val USER_TOKEN = "user_token"
        const val USER_EMAIL = "user_email"
        const val USER_NAME = "user_name"
        const val IS_LOGGED_IN = "is_logged_in"
        const val USER_ID = "user_id"
    }
    fun getContext(): Context {
        return appContext
    }

    fun saveAuthToken(token: String) {
        val editor = prefs.edit()
        editor.putString(USER_TOKEN, token)
        editor.putBoolean(IS_LOGGED_IN, true)
        editor.apply()
    }

    fun saveUserEmail(email: String) {
        val editor = prefs.edit()
        editor.putString(USER_EMAIL, email)
        editor.apply()
    }

    fun getUserEmail(): String? {
        return prefs.getString(USER_EMAIL, null)
    }


    fun saveUserName(name: String) {
        val editor = prefs.edit()
        editor.putString(USER_NAME, name)
        editor.apply()
    }


    fun getUserName(): String? {
        return prefs.getString(USER_NAME, null)
    }


    fun isLoggedIn(): Boolean {
        return prefs.getBoolean(IS_LOGGED_IN, false) && !getAuthToken().isNullOrEmpty()
    }


    fun logout() {
        val editor = prefs.edit()
        editor.remove(USER_TOKEN)
        editor.remove(USER_EMAIL)
        editor.remove(USER_NAME)
        editor.putBoolean(IS_LOGGED_IN, false)
        editor.apply()
    }


    fun hasValidToken(): Boolean {
        val token = getAuthToken()
        return !token.isNullOrEmpty()
    }

    fun clearAllData() {
        val editor = prefs.edit()
        editor.clear()
        editor.apply()
    }


    fun saveUserId(userId: Long) {
        val editor = prefs.edit()
        editor.putLong(USER_ID, userId)
        editor.apply()
    }


    fun getUserId(): Long {
        return prefs.getLong(USER_ID, -1L)
    }


    fun saveUserData(token: String, email: String, name: String? = null, userId: Long? = null) {

        val editor = prefs.edit()
        editor.putString(USER_TOKEN, token)
        editor.putString(USER_EMAIL, email)
        name?.let { editor.putString(USER_NAME, it) }
        userId?.let { editor.putLong(USER_ID, it) }
        editor.putBoolean(IS_LOGGED_IN, true)

        val success = editor.commit()
    }


    fun getAuthToken(): String? {
        val token = prefs.getString(USER_TOKEN, null)
        return token
    }

    fun isTokenValid(): Boolean {
        val token = getAuthToken()
        if (token.isNullOrEmpty()) {
            return false
        }


        val parts = token.split(".")
        if (parts.size != 3) {
            return false
        }

        return !isTokenExpired(token)
    }


    private fun isTokenExpired(token: String): Boolean {
        return try {
            val parts = token.split(".")
            val payload = String(android.util.Base64.decode(parts[1], android.util.Base64.URL_SAFE))
            val jsonObject = org.json.JSONObject(payload)
            val exp = jsonObject.getLong("exp") * 1000
            val currentTime = System.currentTimeMillis()

            val isExpired = currentTime > exp
            if (isExpired) {
            }
            isExpired
        } catch (e: Exception) {
            true
        }
    }


    fun getValidUserId(): Long {
        val token = getAuthToken()
        if (token.isNullOrEmpty()) {
            return -1L
        }


        val userIdFromToken = TokenUtils.extractUserIdFromToken(token)
        if (userIdFromToken != -1L) {
            saveUserId(userIdFromToken)
            return userIdFromToken
        }
        val savedUserId = getUserId()
        return if (savedUserId != -1L) savedUserId else -1L
    }

    fun validateToken(): Boolean {
        val token = getAuthToken()
        return !token.isNullOrEmpty() && TokenUtils.isTokenValid(token)
    }

    fun debugTokenInfo() {
        val token = getAuthToken()

        if (!token.isNullOrEmpty()) {
            Log.d("SESSION_DEBUG", "Token length: ${token.length}")
            TokenUtils.debugToken(token)
        }
    }
}
