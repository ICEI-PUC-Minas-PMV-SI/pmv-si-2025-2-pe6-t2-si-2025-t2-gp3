package com.thais.app_gymflow.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.thais.app_gymflow.views.network.RetrofitClient
import com.thais.app_gymflow.views.network.ExercicioCadastroRequest
import com.thais.app_gymflow.views.network.ExercicioCadastroResponse
import android.util.Log
import android.content.Context
import com.thais.app_gymflow.views.utils.SessionManager

sealed class ExercicioUiState {
    object Idle : ExercicioUiState()
    object Loading : ExercicioUiState()
    data class Success(val message: String) : ExercicioUiState()
    data class Error(val message: String) : ExercicioUiState()
}

class ExercicioViewModel : ViewModel() {

    private val _exercicios = MutableStateFlow<List<ExercicioCadastroResponse>>(emptyList())
    val exercicios: StateFlow<List<ExercicioCadastroResponse>> = _exercicios.asStateFlow()

    private val _uiState = MutableStateFlow<ExercicioUiState>(ExercicioUiState.Idle)
    val uiState: StateFlow<ExercicioUiState> = _uiState.asStateFlow()

    fun carregarExercicios(fichaId: Long, context: Context) {
        viewModelScope.launch {
            _uiState.value = ExercicioUiState.Loading

            try {
                val sessionManager = SessionManager(context)
                val token = sessionManager.getAuthToken()

                if (token.isNullOrEmpty()) {
                    _uiState.value = ExercicioUiState.Error("❌ Token não encontrado")
                    return@launch
                }

                val apiService = RetrofitClient.getApiWithToken(token)
                val response = apiService.getExerciciosPorFicha(fichaId)

                if (response.isSuccessful) {
                    _exercicios.value = response.body() ?: emptyList()
                    _uiState.value = ExercicioUiState.Success("✅ Exercícios carregados")
                } else {
                    val errorBody = response.errorBody()?.string()
                    _uiState.value = ExercicioUiState.Error("❌ Erro ao carregar exercícios: ${response.code()}")
                }
            } catch (e: Exception) {
                _uiState.value = ExercicioUiState.Error("🔌 Erro de conexão: ${e.message}")
            }
        }
    }

    fun criarExercicio(
        nome: String,
        grupoMuscular: String,
        equipamento: String,
        context: Context
    ) {
        viewModelScope.launch {
            _uiState.value = ExercicioUiState.Loading


            try {
                val sessionManager = SessionManager(context)
                val token = sessionManager.getAuthToken()

                if (token.isNullOrEmpty()) {
                    _uiState.value = ExercicioUiState.Error("❌ Token não encontrado")
                    return@launch
                }

                val apiService = RetrofitClient.getApiWithToken(token)


                val exercicioRequest = ExercicioCadastroRequest(
                    nome = nome,
                    grupoMuscular = grupoMuscular,
                    equipamento = equipamento
                )

                val response = apiService.criarExercicio(exercicioRequest)

                if (response.isSuccessful) {
                    val novoExercicio = response.body()
                    if (novoExercicio != null) {
                        _exercicios.value = _exercicios.value + novoExercicio
                        _uiState.value = ExercicioUiState.Success("✅ Exercício criado com sucesso")
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    _uiState.value = ExercicioUiState.Error("❌ Erro ao criar exercício: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("EXERCICIO_DEBUG", "💥 Exceção: ${e.message}", e)
                _uiState.value = ExercicioUiState.Error("🔌 Erro de conexão: ${e.message}")
            }
        }
    }

    fun resetState() {
        _uiState.value = ExercicioUiState.Idle
    }
}
