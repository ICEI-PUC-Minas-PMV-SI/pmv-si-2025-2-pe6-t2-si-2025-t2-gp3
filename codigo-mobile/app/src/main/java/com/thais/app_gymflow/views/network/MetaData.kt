package com.thais.app_gymflow.views.network

data class MetaListResponse(
    val metas: List<MetaResponse>
)

data class MetaRequest(
    val idUsuario: Long,
    val tipo: String,
    val atual: Double,
    val desejado: Double,
    val inicio: String,
    val prazo: String,
    val unidadeDeMedida: String
)

data class MetaResponse(
    val id: Long,
    val idUsuario: Long,
    val tipo: String,
    val atual: Double,
    val desejado: Double,
    val inicio: String,
    val prazo: String,
    val unidadeDeMedida: String
)

object TipoMeta {
    const val PESO = "PESO"
    const val MEDIDA_CORPORAL = "MEDIDA_CORPORAL"
    const val PERFORMANCE = "PERFORMANCE"
    const val HIDRATACAO = "HIDRATACAO"
    const val FREQUENCIA = "FREQUENCIA"
    const val OUTRO = "OUTRO"

    val todos = listOf(PESO, MEDIDA_CORPORAL, PERFORMANCE, HIDRATACAO, FREQUENCIA, OUTRO)
}

object UnidadeMedida {
    const val GRAMAS = "GRAMAS"
    const val QUILOS = "QUILOS"
    const val LITROS = "LITROS"
    const val MILILITROS = "MILILITROS"
    const val REPETICOES = "REPETICOES"
    const val PORCENTAGEM = "PORCENTAGEM"
    const val DIAS = "DIAS"
    const val SEMANAS = "SEMANAS"
    const val MESES = "MESES"
    const val MINUTOS = "MINUTOS"
    const val SEGUNDOS = "SEGUNDOS"
    const val HORAS = "HORAS"

    val todas = listOf(
        GRAMAS, QUILOS, LITROS, MILILITROS, REPETICOES,
        PORCENTAGEM, DIAS, SEMANAS, MESES, MINUTOS,
        SEGUNDOS, HORAS
    )
}