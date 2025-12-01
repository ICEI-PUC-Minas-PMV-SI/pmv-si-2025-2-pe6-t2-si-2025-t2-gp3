package com.thais.app_gymflow.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.thais.app_gymflow.views.network.RetrofitClient
import com.thais.app_gymflow.views.network.Usuario
import android.util.Log

class CadastroViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<CadastroUiState>(CadastroUiState.Idle)
    val uiState: StateFlow<CadastroUiState> = _uiState.asStateFlow()

    fun cadastrarUsuario(nome: String, email: String, senha: String) {
        viewModelScope.launch {
            _uiState.value = CadastroUiState.Loading


            try {
                // ✅ CORREÇÃO: Criar usuário com TODOS os parâmetros
                val usuario = Usuario(
                    nome = nome,
                    email = email,
                    senha = senha  // ✅ AGORA INCLUINDO A SENHA
                )


                val response = RetrofitClient.api.criarUsuario(usuario)

                when (response.code()) {
                    201 -> {
                        _uiState.value = CadastroUiState.Success("Usuário cadastrado com sucesso!")
                    }
                    409 -> {
                        _uiState.value = CadastroUiState.Error("Este usuário já está cadastrado")
                    }
                    else -> {
                        val errorBody = response.errorBody()?.string() ?: "Erro desconhecido"
                        _uiState.value = CadastroUiState.Error("Erro ${response.code()}: $errorBody")
                    }
                }

            } catch (e: Exception) {
                Log.e("Cadastro", "Erro: ${e.message}", e)
                _uiState.value = CadastroUiState.Error("Erro de conexão: ${e.message}")
            }
        }
    }

    fun resetState() {
        _uiState.value = CadastroUiState.Idle
    }
}

sealed class CadastroUiState {
    object Idle : CadastroUiState()
    object Loading : CadastroUiState()
    data class Success(val message: String) : CadastroUiState()
    data class Error(val message: String) : CadastroUiState()
}