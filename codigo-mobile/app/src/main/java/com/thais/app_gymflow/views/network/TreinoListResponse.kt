package com.thais.app_gymflow.views.network

data class TreinoListResponse(
    val treinos: List<TreinoResponse>,
    val total: Int? = null,
    val pagina: Int? = null
)

data class TreinoSingleResponse(
    val treino: TreinoResponse
)