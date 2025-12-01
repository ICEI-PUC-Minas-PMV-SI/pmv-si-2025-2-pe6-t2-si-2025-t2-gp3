package com.thais.app_gymflow.views.utils

import android.util.Base64
import android.util.Log
import org.json.JSONObject

object TokenUtils {

    fun extractUserIdFromToken(token: String): Long {
        return try {

            val parts = token.split(".")
            if (parts.size != 3) {
                return -1L
            }

            val payload = String(
                Base64.decode(parts[1], Base64.URL_SAFE),
                Charsets.UTF_8
            )

            val jsonObject = JSONObject(payload)

            val userId = when {
                jsonObject.has("idUser") -> {
                    jsonObject.getLong("idUser")
                }
                jsonObject.has("userId") -> {
                    jsonObject.getLong("userId")
                }
                jsonObject.has("user_id") -> {
                    jsonObject.getLong("user_id")
                }
                jsonObject.has("sub") -> {
                    val sub = jsonObject.getString("sub")
                    sub.toLongOrNull() ?: -1L
                }
                jsonObject.has("id") -> {
                    jsonObject.getLong("id")
                }
                jsonObject.has("nameid") -> {
                    jsonObject.getLong("nameid")
                }
                else -> {
                    -1L
                }
            }
            userId

        } catch (e: Exception) {
            -1L
        }
    }

    fun isTokenExpired(token: String): Boolean {
        return try {
            val parts = token.split(".")
            if (parts.size != 3) return true

            val payload = String(Base64.decode(parts[1], Base64.URL_SAFE), Charsets.UTF_8)
            val jsonObject = JSONObject(payload)

            if (jsonObject.has("exp")) {
                val exp = jsonObject.getLong("exp") * 1000
                val currentTime = System.currentTimeMillis()
                val isExpired = currentTime > exp

                if (isExpired) {
                } else {
                    val minutesLeft = (exp - currentTime) / 1000 / 60
                }

                return isExpired
            }
            false
        } catch (e: Exception) {
            true
        }
    }

    fun isTokenValid(token: String): Boolean {
        return try {
            val parts = token.split(".")
            if (parts.size != 3) {
                return false
            }

            val userId = extractUserIdFromToken(token)
            val isExpired = isTokenExpired(token)
            val isValid = userId != -1L && !isExpired

            isValid

        } catch (e: Exception) {
            false
        }
    }

    fun debugToken(token: String) {
        try {
            val parts = token.split(".")

            if (parts.size >= 2) {
                val payload = String(Base64.decode(parts[1], Base64.URL_SAFE), Charsets.UTF_8)

                val jsonObject = JSONObject(payload)
                jsonObject.keys().forEach { key ->
                }


                if (jsonObject.has("exp")) {
                    val exp = jsonObject.getLong("exp") * 1000
                    val currentTime = System.currentTimeMillis()
                    val isExpired = currentTime > exp
                    val minutesLeft = if (!isExpired) (exp - currentTime) / 1000 / 60 else 0

                }
            }

            val userId = extractUserIdFromToken(token)

        } catch (e: Exception) {

        }
    }

    fun analyzeTokenFormat(token: String) {
        try {

            val parts = token.split(".")

            val header = String(Base64.decode(parts[0], Base64.URL_SAFE), Charsets.UTF_8)

            val payload = String(Base64.decode(parts[1], Base64.URL_SAFE), Charsets.UTF_8)

            val headerJson = JSONObject(header)

        } catch (e: Exception) {

        }
    }

}