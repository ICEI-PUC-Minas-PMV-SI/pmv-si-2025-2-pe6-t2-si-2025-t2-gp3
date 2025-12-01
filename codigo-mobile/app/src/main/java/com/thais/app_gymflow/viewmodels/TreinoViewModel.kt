package com.thais.app_gymflow.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.thais.app_gymflow.views.network.RetrofitClient
import com.thais.app_gymflow.views.network.TreinoRequest
import com.thais.app_gymflow.views.network.TreinoResponse
import android.util.Log
import android.content.Context

class TreinoViewModel : ViewModel() {

    private val _treinos = MutableStateFlow<List<TreinoResponse>>(emptyList())
    val treinos: StateFlow<List<TreinoResponse>> = _treinos.asStateFlow()

    private val _uiState = MutableStateFlow<TreinoUiState>(TreinoUiState.Idle)
    val uiState: StateFlow<TreinoUiState> = _uiState.asStateFlow()


    fun carregarTreinos(usuarioId: Long, context: Context) {
        viewModelScope.launch {
            _uiState.value = TreinoUiState.Loading

            try {
                val apiService = RetrofitClient.getAuthenticatedApi(context)
                val response = apiService.getTreinosPorUsuario(usuarioId)



                if (response.isSuccessful()) {

                    val treinoListResponse = response.body()
                    val treinosList = treinoListResponse?.treinos ?: emptyList()

                    _treinos.value = treinosList
                    _uiState.value = TreinoUiState.Success("Treinos carregados com sucesso")


                } else {

                }
            } catch (e: Exception) {

                _uiState.value = TreinoUiState.Error("🔌 Erro de conexão: ${e.message ?: "Verifique sua internet"}")
            }
        }
    }

    fun criarTreino(treinoRequest: TreinoRequest, context: Context) {
        viewModelScope.launch {
            _uiState.value = TreinoUiState.Loading

            try {

                val apiService = RetrofitClient.getAuthenticatedApi(context)
                val response = apiService.criarTreino(treinoRequest)



                if (response.isSuccessful) {
                    val novoTreino = response.body()
                    if (novoTreino != null) {

                        _treinos.value = _treinos.value + novoTreino
                        _uiState.value = TreinoUiState.Success(" Treino '${novoTreino.nome}' criado com sucesso!")

                    } else {
                        _uiState.value = TreinoUiState.Error(" Resposta vazia do servidor")

                    }
                } else {
                    val errorBody = response.errorBody()?.string()


                    when (response.code()) {
                        401 -> {
                            _uiState.value = TreinoUiState.Error("Acesso não autorizado. Faça login novamente.")
                        }
                        400 -> {
                            _uiState.value = TreinoUiState.Error("Dados inválidos. Verifique as informações do treino.")
                        }
                        409 -> {
                            _uiState.value = TreinoUiState.Error("⚠Já existe um treino com este nome")
                        }
                        else -> {
                            _uiState.value = TreinoUiState.Error("Erro ${response.code()} ao criar treino: $errorBody")
                        }
                    }
                }
            } catch (e: Exception) {

                _uiState.value = TreinoUiState.Error("🔌 Erro de conexão: ${e.message ?: "Verifique sua internet"}")
            }
        }
    }


    fun deletarTreino(treinoId: Long, context: Context) {
        viewModelScope.launch {
            _uiState.value = TreinoUiState.Loading

            try {

                val apiService = RetrofitClient.getAuthenticatedApi(context)
                val response = apiService.deletarTreino(treinoId)

                if (response.isSuccessful) {

                    _treinos.value = _treinos.value.filter { it.id != treinoId }
                    _uiState.value = TreinoUiState.Success("✅ Treino deletado com sucesso")

                } else {
                    val errorBody = response.errorBody()?.string()

                    _uiState.value = TreinoUiState.Error("Erro ao deletar treino: ${response.code()}")
                }
            } catch (e: Exception) {
                _uiState.value = TreinoUiState.Error("🔌 Erro de conexão: ${e.message}")
            }
        }
    }


    fun atualizarTreinos(usuarioId: Long, context: Context) {
        carregarTreinos(usuarioId, context)
    }

    fun resetState() {
        _uiState.value = TreinoUiState.Idle
    }

    fun limparTreinos() {
        _treinos.value = emptyList()
    }
}

sealed class TreinoUiState {
    object Idle : TreinoUiState()
    object Loading : TreinoUiState()
    data class Success(val message: String) : TreinoUiState()
    data class Error(val message: String) : TreinoUiState()
}