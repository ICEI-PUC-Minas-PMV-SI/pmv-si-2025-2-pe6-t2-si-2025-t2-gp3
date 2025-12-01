package com.thais.app_gymflow.viewmodels

sealed class PerfilUiState {
    object Idle : PerfilUiState()
    object Loading : PerfilUiState()
    data class Success(val message: String) : PerfilUiState()
    data class Error(val message: String) : PerfilUiState()
}