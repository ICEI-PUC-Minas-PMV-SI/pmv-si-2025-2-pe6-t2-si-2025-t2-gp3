package com.thais.app_gymflow.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.thais.app_gymflow.views.network.RetrofitClient
import com.thais.app_gymflow.views.network.UsuarioPerfilResponse
import com.thais.app_gymflow.views.network.UsuarioUpdateRequest
import com.thais.app_gymflow.views.utils.SessionManager
import android.util.Log
import android.content.Context

class PerfilViewModel : ViewModel() {

    private val _usuario = MutableStateFlow<UsuarioPerfilResponse?>(null)
    val usuario: StateFlow<UsuarioPerfilResponse?> = _usuario.asStateFlow()

    private val _uiState = MutableStateFlow<PerfilUiState>(PerfilUiState.Idle)
    val uiState: StateFlow<PerfilUiState> = _uiState.asStateFlow()

    private val _isEditing = MutableStateFlow(false)
    val isEditing: StateFlow<Boolean> = _isEditing.asStateFlow()

    fun carregarPerfil(usuarioId: Long, context: Context) {
        viewModelScope.launch {
            _uiState.value = PerfilUiState.Loading

            if (usuarioId <= 0) {
                _uiState.value = PerfilUiState.Error("ID de usuário inválido")
                return@launch
            }

            try {
                val sessionManager = SessionManager(context)

                // ✅ VERIFICA TOKEN
                if (!sessionManager.validateToken()) {
                    _uiState.value = PerfilUiState.Error("Token inválido")
                    return@launch
                }

                val apiService = RetrofitClient.getAuthenticatedApi(context)
                val response = apiService.getUsuarioPorId(usuarioId)

                if (response.isSuccessful) {
                    _usuario.value = response.body()
                    _uiState.value = PerfilUiState.Success("Perfil carregado com sucesso")
                } else {
                    when (response.code()) {
                        401 -> {
                            _uiState.value = PerfilUiState.Error("Token expirado")
                            sessionManager.logout()
                        }
                        404 -> _uiState.value = PerfilUiState.Error("Usuário não encontrado")
                        else -> _uiState.value = PerfilUiState.Error("Erro: ${response.code()}")
                    }
                }
            } catch (e: Exception) {
                _uiState.value = PerfilUiState.Error("Erro de conexão: ${e.message}")
            }
        }
    }
    fun atualizarPerfil(
        usuarioId: Long,
        usuarioRequest: UsuarioUpdateRequest,
        context: Context
    ) {
        viewModelScope.launch {
            _uiState.value = PerfilUiState.Loading

            try {
                val apiService = RetrofitClient.getAuthenticatedApi(context)
                val response = apiService.atualizarUsuario(usuarioId, usuarioRequest)

                if (response.isSuccessful) {
                    _usuario.value = response.body()
                    _isEditing.value = false
                    _uiState.value = PerfilUiState.Success("Perfil atualizado com sucesso")
                } else {
                    val errorBody = response.errorBody()?.string()
                    _uiState.value = PerfilUiState.Error("Erro ao atualizar perfil: ${response.code()}")
                }
            } catch (e: Exception) {
                _uiState.value = PerfilUiState.Error("Erro de conexão: ${e.message}")
            }
        }
    }

    fun setEditing(editing: Boolean) {
        _isEditing.value = editing
    }

    fun resetState() {
        _uiState.value = PerfilUiState.Idle
    }
}