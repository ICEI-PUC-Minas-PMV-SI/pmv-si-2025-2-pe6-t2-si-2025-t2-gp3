package com.thais.app_gymflow.views.network

data class ExercicioCadastroRequest(
    val nome: String,
    val grupoMuscular: String,
    val equipamento: String
)

data class ExercicioCadastroResponse(
    val id: Long,
    val nome: String,
    val grupoMuscular: String,
    val equipamento: String
)

object GrupoMuscular {
    const val PEITO = "PEITO"
    const val COSTAS = "COSTAS"
    const val OMBROS = "OMBROS"
    const val BICEPS = "BICEPS"
    const val TRICEPS = "TRICEPS"
    const val QUADRICEPS = "QUADRICEPS"
    const val POSTERIOR_COXA = "POSTERIOR_COXA"
    const val GLUTEOS = "GLUTEOS"
    const val PANTURRILHAS = "PANTURRILHAS"
    const val ABDOMEN = "ABDOMEN"
    const val LOMBAR = "LOMBAR"

    val todos = listOf(
        PEITO, COSTAS, OMBROS, BICEPS, TRICEPS,
        QUADRICEPS, POSTERIOR_COXA, GLUTEOS, PANTURRILHAS,
        ABDOMEN, LOMBAR
    )
}

object Equipamento {
    const val BARRA = "BARRA"
    const val HALTER = "HALTER"
    const val KETTLEBELL = "KETTLEBELL"
    const val ANILHA = "ANILHA"
    const val MEDICINE_BALL = "MEDICINE_BALL"
    const val MAQUINA = "MAQUINA"
    const val CABO = "CABO"
    const val SMITH_MACHINE = "SMITH_MACHINE"
    const val POLIA = "POLIA"
    const val MULTI_STATION = "MULTI_STATION"
    const val PESO_CORPORAL = "PESO_CORPORAL"
    const val BARRA_FIXA = "BARRA_FIXA"
    const val PARALELAS = "PARALELAS"
    const val TRX = "TRX"
    const val ELASTICO = "ELASTICO"
    const val ROPE_BATTLE = "ROPE_BATTLE"
    const val SANDBAG = "SANDBAG"
    const val BOLA_SUICA = "BOLA_SUIÇA"
    const val BOSU = "BOSU"
    const val STEP = "STEP"
    const val CAIXA_PLIOMETRICA = "CAIXA_PLIOMETRICA"
    const val ESTEIRA = "ESTEIRA"
    const val BICICLETA_ERGOMETRICA = "BICICLETA_ERGOMETRICA"
    const val REMO_INDOOR = "REMO_INDOOR"
    const val ESCALADOR = "ESCALADOR"
    const val ESCADA = "ESCADA"
    const val OUTRO = "OUTRO"

    val todos = listOf(
        BARRA, HALTER, KETTLEBELL, ANILHA, MEDICINE_BALL,
        MAQUINA, CABO, SMITH_MACHINE, POLIA, MULTI_STATION,
        PESO_CORPORAL, BARRA_FIXA, PARALELAS, TRX, ELASTICO,
        ROPE_BATTLE, SANDBAG, BOLA_SUICA, BOSU, STEP,
        CAIXA_PLIOMETRICA, ESTEIRA, BICICLETA_ERGOMETRICA, REMO_INDOOR,
        ESCALADOR, ESCADA, OUTRO
    )
}