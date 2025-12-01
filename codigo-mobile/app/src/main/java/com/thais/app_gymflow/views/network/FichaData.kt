package com.thais.app_gymflow.views.network

data class FichaCadastroRequest(
    val treinoId: Long,
    val diaSemana: String
)

data class FichaCadastroResponse(
    val id: Long,
    val diaSemana: String
)
data class FichaResponse(
    val id: Long,
    val diaSemana: String,
    val treinoId: Long
)

object DiaSemana {
    const val SEGUNDA = "SEGUNDA"
    const val TERCA = "TERCA"
    const val QUARTA = "QUARTA"
    const val QUINTA = "QUINTA"
    const val SEXTA = "SEXTA"
    const val SABADO = "SABADO"
    const val DOMINGO = "DOMINGO"

    val todos = listOf(SEGUNDA, TERCA, QUARTA, QUINTA, SEXTA, SABADO, DOMINGO)
}