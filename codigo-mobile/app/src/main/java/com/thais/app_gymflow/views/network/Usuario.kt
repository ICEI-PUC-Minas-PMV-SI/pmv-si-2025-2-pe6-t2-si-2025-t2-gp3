package com.thais.app_gymflow.views.network

data class Usuario(
    val id: Long? = null,
    val nome: String,
    val email: String,
    val senha: String
)

data class LoginRequest(
    val email: String,
    val senha: String
)

data class LoginResponse(
    val token: String
)

fun LoginResponse.hasUserId(): Boolean {
    return try {
        this::class.members.any { it.name == "userId" }
    } catch (e: Exception) {
        false
    }
}

data class UsuarioPerfilResponse(
    val id: Long,
    val nome: String,
    val email: String,
    val CPF: String,
    val dataNascimento: String,
    val idade: Int,
    val peso: Double,
    val altura: Double
)

data class UsuarioUpdateRequest(
    val nome: String,
    val cpf: String,
    val peso: Double,
    val altura: Double,
    val dataNascimento: String
)