package com.thais.app_gymflow.views.network

data class TreinoRequest(
    val usuarioId: Long,
    val nome: String,
    val dataInicio: String,
    val dataFim: String
)

data class TreinoResponse(
    val id: Long,
    val usuarioId: Long,
    val nome: String,
    val dataInicio: String,
    val dataFim: String
)
