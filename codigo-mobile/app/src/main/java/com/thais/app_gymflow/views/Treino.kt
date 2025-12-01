package com.thais.app_gymflow.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import android.util.Log
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.thais.app_gymflow.viewmodels.TreinoViewModel
import com.thais.app_gymflow.viewmodels.TreinoUiState
import com.thais.app_gymflow.views.network.TreinoRequest
import com.thais.app_gymflow.views.utils.SessionManager
import kotlinx.coroutines.launch
import com.thais.app_gymflow.views.utils.TokenUtils
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun TreinoScreen(navController: NavController) {
    val viewModel: TreinoViewModel = viewModel()
    val treinos by viewModel.treinos.collectAsState()
    val uiState by viewModel.uiState.collectAsState()

    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }
    var showDialog by remember { mutableStateOf(false) }
    var usuarioId by remember { mutableStateOf<Long?>(null) }

            LaunchedEffect(Unit) {
                val token = sessionManager.getAuthToken()
                usuarioId = sessionManager.getUserId()

                if (usuarioId == 1L && !token.isNullOrEmpty()) {
                    val extractedId = TokenUtils.extractUserIdFromToken(token)
                    if (extractedId != -1L) {
                        usuarioId = extractedId
                        sessionManager.saveUserId(extractedId)

                    }
                }


                if (token != null && usuarioId != null && usuarioId != -1L) {
                    viewModel.carregarTreinos(usuarioId!!, context)
                } else {

                }
            }

    LaunchedEffect(uiState) {
        when (uiState) {
            is TreinoUiState.Success -> {

                showDialog = false

            }
            else -> {}
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF2F5FA))
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF1E355E))
                .padding(16.dp)
        ) {

            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Voltar",
                tint = Color.White,
                modifier = Modifier
                    .size(24.dp)
                    .clickable {
                        navController.popBackStack()
                    }
                    .align(Alignment.CenterStart)
            )


            Text(
                text = "GYMFLOW",
                modifier = Modifier.align(Alignment.Center),
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        }


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Treinos",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E355E),
                modifier = Modifier.padding(bottom = 24.dp)
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        showDialog = true
                    },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E355E)),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "Criar Treino",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Text(
                            text = "Adicionar nova rotina de exercícios",
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 14.sp
                        )
                    }
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Criar Treino",
                        tint = Color(0xFF00A0FF),
                        modifier = Modifier.size(32.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (uiState is TreinoUiState.Loading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFF1E355E))
                }
            }


            if (treinos.isNotEmpty()) {
                Text(
                    text = "Meus Treinos",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E355E),
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(treinos) { treino ->
                        TreinoCard(
                            treino = treino,
                            onCardClick = {
                                navController.navigate("fichas/${treino.id}")
                            }
                        )
                    }
                }
            } else if (uiState !is TreinoUiState.Loading) {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.FitnessCenter,
                        contentDescription = "Sem treinos",
                        tint = Color(0xFF1E355E).copy(alpha = 0.5f),
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Nenhum treino criado ainda",
                        color = Color(0xFF1E355E).copy(alpha = 0.7f),
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "Clique em 'Criar Treino' para começar",
                        color = Color(0xFF1E355E).copy(alpha = 0.5f),
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }

    if (showDialog) {
        CriarTreinoDialog(
            onDismiss = { showDialog = false },
            onConfirm = { nomeTreino, dataInicio, dataFim ->
                val token = sessionManager.getAuthToken()
                if (token != null && usuarioId != null) {
                    val treinoRequest = TreinoRequest(
                        usuarioId = usuarioId!!,
                        nome = nomeTreino,
                        dataInicio = dataInicio,
                        dataFim = dataFim
                    )
                    viewModel.criarTreino(treinoRequest, context)
                }
            },
            isLoading = uiState is TreinoUiState.Loading
        )
    }
}

@Composable
fun TreinoCard(
    treino: com.thais.app_gymflow.views.network.TreinoResponse,
    onCardClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCardClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    imageVector = Icons.Default.FitnessCenter,
                    contentDescription = "Treino",
                    tint = Color(0xFF00A0FF),
                    modifier = Modifier
                        .size(40.dp)
                        .padding(end = 16.dp)
                )

                Column {
                    Text(
                        text = treino.nome,
                        color = Color(0xFF1E355E),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    Text(
                        text = "Início: ${treino.dataInicio} - Fim: ${treino.dataFim}",
                        color = Color(0xFF1E355E).copy(alpha = 0.7f),
                        fontSize = 12.sp
                    )
                }
            }

            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "Abrir treino",
                tint = Color(0xFF1E355E)
            )
        }
    }
}


private fun extractUserIdFromToken(token: String): Long {
    return try {
        val parts = token.split(".")
        if (parts.size == 3) {
            val payload = String(android.util.Base64.decode(parts[1], android.util.Base64.URL_SAFE))
            val jsonObject = org.json.JSONObject(payload)
            jsonObject.getLong("idUser")
        } else {
            -1L
        }
    } catch (e: Exception) {
        Log.e("TOKEN_PARSER", "Erro ao extrair ID do token: ${e.message}")
        -1L
    }
}

@Composable
fun CriarTreinoDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String, String) -> Unit,
    isLoading: Boolean
) {
    var nomeTreino by remember { mutableStateOf("") }
    var dataInicio by remember { mutableStateOf("") }
    var dataFim by remember { mutableStateOf("") }

    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val dataAtual = dateFormat.format(Date())

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                Text(
                    text = "Criar Novo Treino",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E355E),
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                OutlinedTextField(
                    value = nomeTreino,
                    onValueChange = { nomeTreino = it },
                    label = { Text("Nome do Treino") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    shape = RoundedCornerShape(8.dp)
                )

                OutlinedTextField(
                    value = dataInicio.ifEmpty { dataAtual },
                    onValueChange = { dataInicio = it },
                    label = { Text("Data de Início (yyyy-MM-dd)") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    shape = RoundedCornerShape(8.dp),
                    placeholder = { Text(dataAtual) }
                )

                OutlinedTextField(
                    value = dataFim.ifEmpty { dataAtual },
                    onValueChange = { dataFim = it },
                    label = { Text("Data de Fim (yyyy-MM-dd)") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp),
                    shape = RoundedCornerShape(8.dp),
                    placeholder = { Text(dataAtual) }
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Text("Cancelar")
                    }
                    Button(
                        onClick = {
                            val dataInicioFinal = if (dataInicio.isEmpty()) dataAtual else dataInicio
                            val dataFimFinal = if (dataFim.isEmpty()) dataAtual else dataFim
                            onConfirm(nomeTreino, dataInicioFinal, dataFimFinal)
                        },
                        enabled = nomeTreino.isNotEmpty() && !isLoading,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E355E))
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                color = Color.White
                            )
                        } else {
                            Text("Criar")
                        }
                    }
                }
            }
        }
    }
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true)
@Composable
fun PreviewTreinoScreen() {
    TreinoScreen(navController = androidx.navigation.compose.rememberNavController())
}