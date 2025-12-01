package com.thais.app_gymflow.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.thais.app_gymflow.views.network.RetrofitClient
import com.thais.app_gymflow.views.network.FichaCadastroRequest
import com.thais.app_gymflow.views.network.FichaCadastroResponse
import android.util.Log
import android.content.Context
import com.thais.app_gymflow.views.utils.SessionManager

sealed class FichaUiState {
    object Idle : FichaUiState()
    object Loading : FichaUiState()
    data class Success(val message: String) : FichaUiState()
    data class Error(val message: String) : FichaUiState()
}

class FichaViewModel : ViewModel() {

    private val _fichas = MutableStateFlow<List<FichaCadastroResponse>>(emptyList())
    val fichas: StateFlow<List<FichaCadastroResponse>> = _fichas.asStateFlow()

    private val _uiState = MutableStateFlow<FichaUiState>(FichaUiState.Idle)
    val uiState: StateFlow<FichaUiState> = _uiState.asStateFlow()

    fun carregarFichas(treinoId: Long, context: Context) {
        viewModelScope.launch {
            _uiState.value = FichaUiState.Loading


            try {
                val sessionManager = SessionManager(context)
                val token = sessionManager.getAuthToken()

                if (token.isNullOrEmpty()) {
                    _uiState.value = FichaUiState.Error("❌ Token não encontrado")
                    return@launch
                }

                val apiService = RetrofitClient.getApiWithToken(token)
                val response = apiService.getFichasPorTreino(treinoId)

                if (response.isSuccessful) {
                    _fichas.value = response.body() ?: emptyList()
                    _uiState.value = FichaUiState.Success("✅ Fichas carregadas")
                } else {
                    val errorBody = response.errorBody()?.string()

                    _uiState.value = FichaUiState.Error("❌ Erro ${response.code()} ao carregar fichas")
                }
            } catch (e: Exception) {
                _uiState.value = FichaUiState.Error("🔌 Erro de conexão: ${e.message}")
            }
        }
    }

    fun criarFicha(fichaRequest: FichaCadastroRequest, context: Context) {
        viewModelScope.launch {
            _uiState.value = FichaUiState.Loading


            try {
                val sessionManager = SessionManager(context)
                val token = sessionManager.getAuthToken()

                if (token.isNullOrEmpty()) {
                    _uiState.value = FichaUiState.Error("❌ Token não encontrado")
                    return@launch
                }

                val apiService = RetrofitClient.getApiWithToken(token)
                val response = apiService.criarFicha(fichaRequest)

                if (response.isSuccessful) {
                    val novaFicha = response.body()
                    if (novaFicha != null) {
                        _fichas.value = _fichas.value + novaFicha
                        _uiState.value = FichaUiState.Success("✅ Ficha '${novaFicha.diaSemana}' criada com sucesso!")
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    _uiState.value = FichaUiState.Error("❌ Erro ${response.code()} ao criar ficha")
                }
            } catch (e: Exception) {
                _uiState.value = FichaUiState.Error("🔌 Erro de conexão: ${e.message}")
            }
        }
    }

    fun resetState() {
        _uiState.value = FichaUiState.Idle
    }
}
