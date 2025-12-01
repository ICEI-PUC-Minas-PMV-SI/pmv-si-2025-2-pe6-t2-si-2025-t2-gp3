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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.thais.app_gymflow.viewmodels.FichaViewModel
import com.thais.app_gymflow.viewmodels.FichaUiState
import com.thais.app_gymflow.views.network.FichaCadastroRequest
import com.thais.app_gymflow.views.network.FichaCadastroResponse
import com.thais.app_gymflow.views.utils.SessionManager
import android.util.Log

@Composable
fun FichaScreen(navController: NavController, treinoId: Long?) {
    val viewModel: FichaViewModel = viewModel()
    val fichas by viewModel.fichas.collectAsState()
    val uiState by viewModel.uiState.collectAsState()

    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }
    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        if (treinoId != null) {
            viewModel.carregarFichas(treinoId, context)
        }
    }

    LaunchedEffect(uiState) {
        when (uiState) {
            is FichaUiState.Success -> {
                showDialog = false
            }
            is FichaUiState.Error -> {

            }
            else -> {}
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF2F5FA))
    ) {
        // Header
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
                        Log.d("FICHA_SCREEN", "⬅️ Voltando para tela anterior")
                        navController.popBackStack()
                    }
                    .align(Alignment.CenterStart)
            )


            Text(
                text = "Fichas do Treino",
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
                text = "Fichas de Treino",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E355E),
                modifier = Modifier.padding(bottom = 24.dp)
            )


            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        Log.d("FICHA_SCREEN", "➕ Abrindo diálogo para criar ficha")
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
                            text = "Criar Ficha",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Text(
                            text = "Adicionar nova ficha de exercícios",
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 14.sp
                        )
                    }
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Criar Ficha",
                        tint = Color(0xFF00A0FF),
                        modifier = Modifier.size(32.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            if (uiState is FichaUiState.Loading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(color = Color(0xFF1E355E))
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Carregando fichas...",
                            color = Color(0xFF1E355E),
                            fontSize = 16.sp
                        )
                    }
                }
            }
            I
            if (uiState is FichaUiState.Error) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFE6E6)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = "Erro",
                            tint = Color.Red,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = (uiState as FichaUiState.Error).message,
                            color = Color.Red,
                            fontSize = 14.sp
                        )
                    }
                }
            }

            if (fichas.isNotEmpty()) {
                Text(
                    text = "Minhas Fichas (${fichas.size})",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E355E),
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(fichas) { ficha ->
                        FichaCard(
                            ficha = ficha,
                            onCardClick = {
                                navController.navigate("exercicios/${ficha.id}")
                            }
                        )
                    }
                }
            } else if (uiState !is FichaUiState.Loading && uiState !is FichaUiState.Error) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.List,
                        contentDescription = "Sem fichas",
                        tint = Color(0xFF1E355E).copy(alpha = 0.5f),
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Nenhuma ficha criada ainda",
                        color = Color(0xFF1E355E).copy(alpha = 0.7f),
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "Clique em 'Criar Ficha' para começar",
                        color = Color(0xFF1E355E).copy(alpha = 0.5f),
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }

    if (showDialog) {
        CriarFichaDialogEstavel(
            onDismiss = { showDialog = false },
            onConfirm = { diaSemana ->
                if (treinoId != null) {
                    val fichaRequest = FichaCadastroRequest(
                        treinoId = treinoId,
                        diaSemana = diaSemana
                    )
                    viewModel.criarFicha(fichaRequest, context)
                }
            },
            isLoading = uiState is FichaUiState.Loading
        )
    }
}

@Composable
fun FichaCard(
    ficha: FichaCadastroResponse,
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
                // Ícone da ficha
                Icon(
                    imageVector = Icons.Default.List,
                    contentDescription = "Ficha",
                    tint = Color(0xFF00A0FF),
                    modifier = Modifier
                        .size(40.dp)
                        .padding(end = 16.dp)
                )

                Column {
                    Text(
                        text = "Ficha - ${ficha.diaSemana}",
                        color = Color(0xFF1E355E),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    Text(
                        text = "Dia da semana: ${ficha.diaSemana}",
                        color = Color(0xFF1E355E).copy(alpha = 0.7f),
                        fontSize = 14.sp
                    )
                }
            }

            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "Abrir ficha",
                tint = Color(0xFF1E355E)
            )
        }
    }
}

@Composable
fun CriarFichaDialogEstavel(
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit,
    isLoading: Boolean
) {
    var diaSemana by remember { mutableStateOf("SEGUNDA") }
    val diasSemana = listOf("SEGUNDA", "TERÇA", "QUARTA", "QUINTA", "SEXTA", "SÁBADO", "DOMINGO")

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
                    text = "Criar Nova Ficha",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E355E),
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Text(
                    text = "Dia da Semana:",
                    color = Color(0xFF1E355E),
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Text(
                    text = diaSemana,
                    color = Color(0xFF1E355E),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                        .background(Color(0xFFF2F5FA), RoundedCornerShape(8.dp))
                        .padding(16.dp),
                    textAlign = TextAlign.Center
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = {
                            val currentIndex = diasSemana.indexOf(diaSemana)
                            val newIndex = if (currentIndex > 0) currentIndex - 1 else diasSemana.size - 1
                            diaSemana = diasSemana[newIndex]
                            Log.d("FICHA_DIALOG", "⬅️ Dia anterior: $diaSemana")
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E355E)),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("← Anterior")
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = {
                            val currentIndex = diasSemana.indexOf(diaSemana)
                            val newIndex = (currentIndex + 1) % diasSemana.size
                            diaSemana = diasSemana[newIndex]
                            Log.d("FICHA_DIALOG", "➡️ Próximo dia: $diaSemana")
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E355E)),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Próximo →")
                    }
                }

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
                        onClick = { onConfirm(diaSemana) },
                        enabled = !isLoading,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E355E))
                    ) {
                        if (isLoading) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(16.dp),
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Criando...")
                            }
                        } else {
                            Text("Criar Ficha")
                        }
                    }
                }
            }
        }
    }
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true)
@Composable
fun PreviewFichaScreen() {
    FichaScreen(
        navController = androidx.navigation.compose.rememberNavController(),
        treinoId = 1L
    )
}
