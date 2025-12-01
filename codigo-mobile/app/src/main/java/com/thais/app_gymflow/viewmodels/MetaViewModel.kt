package com.thais.app_gymflow.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.thais.app_gymflow.views.network.RetrofitClient
import com.thais.app_gymflow.views.network.MetaRequest
import com.thais.app_gymflow.views.network.MetaResponse
import com.thais.app_gymflow.views.utils.SessionManager
import android.util.Log
import android.content.Context

class MetaViewModel : ViewModel() {

    private val _metas = MutableStateFlow<List<MetaResponse>>(emptyList())
    val metas: StateFlow<List<MetaResponse>> = _metas.asStateFlow()

    private val _uiState = MutableStateFlow<MetaUiState>(MetaUiState.Idle)
    val uiState: StateFlow<MetaUiState> = _uiState.asStateFlow()

    private val _metaSelecionada = MutableStateFlow<MetaResponse?>(null)
    val metaSelecionada: StateFlow<MetaResponse?> = _metaSelecionada.asStateFlow()

    fun carregarMetas(usuarioId: Long, context: Context) {
        viewModelScope.launch {
            _uiState.value = MetaUiState.Loading

            if (usuarioId <= 0) {
                _uiState.value = MetaUiState.Error("❌ ID de usuário inválido: $usuarioId")
                return@launch
            }

            try {
                val sessionManager = SessionManager(context)

                if (!sessionManager.validateToken()) {
                    _uiState.value = MetaUiState.Error("🚫 Token inválido ou expirado")
                    return@launch
                }

                val apiService = RetrofitClient.getAuthenticatedApi(context)
                val response = apiService.getMetasPorUsuario(usuarioId)

                if (response.isSuccessful) {
                    val metaListResponse = response.body()
                    val metasList: List<MetaResponse> = metaListResponse?.metas ?: emptyList()
                    _metas.value = metasList
                    _uiState.value = MetaUiState.Success("Metas carregadas com sucesso")
                } else {
                    when (response.code()) {
                        401 -> {
                            _uiState.value = MetaUiState.Error("🚫 Token expirado ou inválido")
                            sessionManager.logout()
                        }
                        404 -> {
                            _uiState.value = MetaUiState.Success("Nenhuma meta encontrada")
                            _metas.value = emptyList()
                        }
                        else -> {
                            _uiState.value = MetaUiState.Error("❌ Erro ${response.code()} ao carregar metas")
                        }
                    }
                }
            } catch (e: Exception) {
                _uiState.value = MetaUiState.Error("🔌 Erro de conexão: ${e.message}")
            }
        }
    }

    fun criarMeta(metaRequest: MetaRequest, context: Context) {
        viewModelScope.launch {
            _uiState.value = MetaUiState.Loading

            try {
                val apiService = RetrofitClient.getAuthenticatedApi(context)

                val responses: List<Pair<String, retrofit2.Response<MetaResponse>?>> = listOf(
                    "api/usuarios/{id}/metas" to try {
                        apiService.criarMeta(metaRequest.idUsuario, metaRequest)
                    } catch (e: Exception) {
                        null
                    },
                    "api/metas" to try {
                        apiService.criarMeta2(metaRequest)
                    } catch (e: Exception) {
                        null
                    },
                    "metas" to try {
                        apiService.criarMeta3(metaRequest)
                    } catch (e: Exception) {
                        null
                    },
                    "api/usuarios/metas" to try {
                        apiService.criarMeta4(metaRequest)
                    } catch (e: Exception) {
                        null
                    }
                )

                var success = false
                var creationError: String? = null

                for ((endpointName, response) in responses) {
                    if (response != null) {

                        when (response.code()) {
                            200, 201 -> {
                                val novaMeta = response.body()
                                if (novaMeta != null) {
                                    // ✅ CORREÇÃO: Usar toList() para evitar ambiguidade
                                    _metas.value = _metas.value.toList() + novaMeta
                                    _uiState.value = MetaUiState.Success("✅ Meta '${novaMeta.tipo}' criada com sucesso!")
                                    success = true
                                    break
                                }
                            }
                            401 -> {
                                creationError = "🚫 Acesso não autorizado no endpoint $endpointName"
                            }
                            400 -> {
                                creationError = "📝 Dados inválidos no endpoint $endpointName"
                            }
                            else -> {
                                Log.e("META_ERROR", "Erro ${response.code()} no endpoint $endpointName")
                            }
                        }
                    }
                }

                if (!success) {
                    _uiState.value = MetaUiState.Error(creationError ?: "❌ Nenhum endpoint de criação funcionou")
                }

            } catch (e: Exception) {
                _uiState.value = MetaUiState.Error("🔌 Erro de conexão: ${e.message}")
            }
        }
    }

    fun atualizarMeta(metaId: Long, metaRequest: MetaRequest, context: Context) {
        viewModelScope.launch {
            _uiState.value = MetaUiState.Loading

            try {
                val apiService = RetrofitClient.getAuthenticatedApi(context)
                val response = apiService.atualizarMeta(metaId, metaRequest)

                if (response.isSuccessful) {
                    val metaAtualizada = response.body()
                    if (metaAtualizada != null) {
                        _metas.value = _metas.value.map { if (it.id == metaId) metaAtualizada else it }
                        _uiState.value = MetaUiState.Success("✅ Meta atualizada com sucesso!")
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    _uiState.value = MetaUiState.Error("❌ Erro ${response.code()} ao atualizar meta")
                }
            } catch (e: Exception) {
                _uiState.value = MetaUiState.Error("🔌 Erro de conexão: ${e.message}")
            }
        }
    }

    fun selecionarMeta(meta: MetaResponse) {
        _metaSelecionada.value = meta
    }

    fun limparMetaSelecionada() {
        _metaSelecionada.value = null
    }

    fun resetState() {
        _uiState.value = MetaUiState.Idle
    }
}

private fun forceLogout(context: Context) {
    val sessionManager = SessionManager(context)
    sessionManager.logout()

}

sealed class MetaUiState {
    object Idle : MetaUiState()
    object Loading : MetaUiState()
    data class Success(val message: String) : MetaUiState()
    data class Error(val message: String) : MetaUiState()
}