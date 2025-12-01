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
import com.thais.app_gymflow.viewmodels.ExercicioViewModel
import com.thais.app_gymflow.viewmodels.ExercicioUiState
import com.thais.app_gymflow.views.network.GrupoMuscular
import com.thais.app_gymflow.views.network.Equipamento
import com.thais.app_gymflow.views.utils.SessionManager
import android.util.Log

@Composable
fun ExercicioScreen(navController: NavController, fichaId: Long?) {
    val viewModel: ExercicioViewModel = viewModel()
    val exercicios by viewModel.exercicios.collectAsState()
    val uiState by viewModel.uiState.collectAsState()

    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }
    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        val token = sessionManager.getAuthToken()
        Log.d("EXERCICIO_SCREEN", "FichaId recebido: $fichaId")
        if (token != null && fichaId != null) {
            viewModel.carregarExercicios(fichaId, context)
        } else {
            Log.e("EXERCICIO_SCREEN", "Token ou fichaId estão nulos!")
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

            // Título
            Text(
                text = "Exercícios da Ficha",
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
                text = "Exercícios",
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
                            text = "Criar Exercício",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Text(
                            text = "Adicionar novo exercício",
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 14.sp
                        )
                    }
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Criar Exercício",
                        tint = Color(0xFF00A0FF),
                        modifier = Modifier.size(32.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (uiState is ExercicioUiState.Loading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFF1E355E))
                }
            }

            if (exercicios.isNotEmpty()) {
                Text(
                    text = "Meus Exercícios",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E355E),
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(exercicios) { exercicio ->
                        ExercicioCard(exercicio = exercicio)
                    }
                }
            } else if (uiState !is ExercicioUiState.Loading) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.FitnessCenter,
                        contentDescription = "Sem exercícios",
                        tint = Color(0xFF1E355E).copy(alpha = 0.5f),
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Nenhum exercício criado ainda",
                        color = Color(0xFF1E355E).copy(alpha = 0.7f),
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "Clique em 'Criar Exercício' para começar",
                        color = Color(0xFF1E355E).copy(alpha = 0.5f),
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }

    if (showDialog) {
        CriarExercicioDialogSimples(
            onDismiss = { showDialog = false },
            onConfirm = { nome, grupoMuscular, equipamento ->
                val token = sessionManager.getAuthToken()
                if (token != null) {
                    viewModel.criarExercicio(nome, grupoMuscular, equipamento, context)
                } else {
                }
            },
            isLoading = uiState is ExercicioUiState.Loading
        )
    }
}

@Composable
fun ExercicioCard(
    exercicio: com.thais.app_gymflow.views.network.ExercicioCadastroResponse
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.FitnessCenter,
                    contentDescription = "Exercício",
                    tint = Color(0xFF00A0FF),
                    modifier = Modifier
                        .size(40.dp)
                        .padding(end = 16.dp)
                )

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = exercicio.nome,
                        color = Color(0xFF1E355E),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    Text(
                        text = "Grupo: ${exercicio.grupoMuscular}",
                        color = Color(0xFF1E355E).copy(alpha = 0.7f),
                        fontSize = 14.sp,
                        modifier = Modifier.padding(bottom = 2.dp)
                    )
                    Text(
                        text = "Equipamento: ${exercicio.equipamento}",
                        color = Color(0xFF1E355E).copy(alpha = 0.7f),
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

@Composable
fun CriarExercicioDialogSimples(
    onDismiss: () -> Unit,
    onConfirm: (String, String, String) -> Unit,
    isLoading: Boolean
) {
    var nome by remember { mutableStateOf("") }
    var grupoMuscular by remember { mutableStateOf(GrupoMuscular.PEITO) }
    var equipamento by remember { mutableStateOf(Equipamento.BARRA) }

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
                    text = "Criar Novo Exercício",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E355E),
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                OutlinedTextField(
                    value = nome,
                    onValueChange = { nome = it },
                    label = { Text("Nome do Exercício") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    shape = RoundedCornerShape(8.dp)
                )

                Text(
                    text = "Grupo Muscular",
                    color = Color(0xFF1E355E),
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Text(
                    text = grupoMuscular,
                    color = Color(0xFF1E355E),
                    fontSize = 16.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = {
                            val currentIndex = GrupoMuscular.todos.indexOf(grupoMuscular)
                            val newIndex = if (currentIndex > 0) currentIndex - 1 else GrupoMuscular.todos.size - 1
                            grupoMuscular = GrupoMuscular.todos[newIndex]
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E355E))
                    ) {
                        Text("Anterior")
                    }
                    Button(
                        onClick = {
                            val currentIndex = GrupoMuscular.todos.indexOf(grupoMuscular)
                            val newIndex = (currentIndex + 1) % GrupoMuscular.todos.size
                            grupoMuscular = GrupoMuscular.todos[newIndex]
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E355E))
                    ) {
                        Text("Próximo")
                    }
                }

                Text(
                    text = "Equipamento",
                    color = Color(0xFF1E355E),
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Text(
                    text = equipamento,
                    color = Color(0xFF1E355E),
                    fontSize = 16.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = {
                            val currentIndex = Equipamento.todos.indexOf(equipamento)
                            val newIndex = if (currentIndex > 0) currentIndex - 1 else Equipamento.todos.size - 1
                            equipamento = Equipamento.todos[newIndex]
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E355E))
                    ) {
                        Text("Anterior")
                    }
                    Button(
                        onClick = {
                            val currentIndex = Equipamento.todos.indexOf(equipamento)
                            val newIndex = (currentIndex + 1) % Equipamento.todos.size
                            equipamento = Equipamento.todos[newIndex]
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E355E))
                    ) {
                        Text("Próximo")
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
                        onClick = { onConfirm(nome, grupoMuscular, equipamento) },
                        enabled = nome.isNotEmpty() && !isLoading,
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
fun PreviewExercicioScreen() {
    ExercicioScreen(
        navController = androidx.navigation.compose.rememberNavController(),
        fichaId = 1L
    )
}