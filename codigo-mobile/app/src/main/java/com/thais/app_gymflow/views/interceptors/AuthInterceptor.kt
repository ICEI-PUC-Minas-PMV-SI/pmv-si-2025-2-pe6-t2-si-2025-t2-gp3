package com.thais.app_gymflow.views.interceptors

import okhttp3.Interceptor
import okhttp3.Response
import android.util.Log
import com.thais.app_gymflow.views.utils.SessionManager
import com.thais.app_gymflow.views.utils.TokenUtils

import android.content.Context

class AuthInterceptor(private val context: Context) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val sessionManager = SessionManager(context)
        val token = sessionManager.getAuthToken()

        val requestBuilder = originalRequest.newBuilder()
            .header("Content-Type", "application/json")
            .header("Accept", "application/json")


        if (!token.isNullOrEmpty()) {
            if (TokenUtils.isTokenValid(token)) {
                requestBuilder.header("Authorization", "Bearer $token")

                try {
                    val parts = token.split(".")
                    if (parts.size == 3) {
                        val payload = String(
                            android.util.Base64.decode(
                                parts[1],
                                android.util.Base64.URL_SAFE
                            )
                        )

                    }
                } catch (e: Exception) {

                }
            } else {
                sessionManager.logout()
            }
        } else {
        }

        val request = requestBuilder.build()
        Log.d("AUTH_INTERCEPTOR", "📤 Headers da requisição:")
        request.headers.forEach { header ->
            Log.d("AUTH_INTERCEPTOR", "   ${header.first}: ${header.second}")
        }

        val response = chain.proceed(request)

        if (!response.isSuccessful) {
            Log.e(
                "AUTH_INTERCEPTOR",
                "ERRO ${response.code} - Corpo: ${response.peekBody(1024).string()}"
            )
        }
        return response
    }
}