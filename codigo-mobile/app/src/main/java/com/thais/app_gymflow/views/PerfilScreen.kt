package com.thais.app_gymflow.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import com.thais.app_gymflow.viewmodels.PerfilViewModel
import com.thais.app_gymflow.viewmodels.PerfilUiState
import com.thais.app_gymflow.views.network.UsuarioUpdateRequest
import com.thais.app_gymflow.views.utils.SessionManager
import com.thais.app_gymflow.views.utils.TokenUtils
import android.util.Log
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun PerfilScreen(navController: NavController) {
    val viewModel: PerfilViewModel = viewModel()
    val usuario by viewModel.usuario.collectAsState()
    val uiState by viewModel.uiState.collectAsState()
    val isEditing by viewModel.isEditing.collectAsState()

    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }
    var usuarioId by remember { mutableStateOf<Long?>(null) }


    LaunchedEffect(Unit) {
        var id = sessionManager.getUserId()


        if (id == 1L) {
            val token = sessionManager.getAuthToken()
            if (!token.isNullOrEmpty()) {
                val extractedId = TokenUtils.extractUserIdFromToken(token)
                if (extractedId != -1L) {
                    id = extractedId
                    sessionManager.saveUserId(extractedId)

                }
            }
        }

        usuarioId = id
        if (usuarioId != null && usuarioId != -1L) {
            viewModel.carregarPerfil(usuarioId!!, context)
        }
    }


    LaunchedEffect(uiState) {
        when (uiState) {
            is PerfilUiState.Success -> {
                val successState = uiState as PerfilUiState.Success

            }
            is PerfilUiState.Error -> {
                val errorState = uiState as PerfilUiState.Error

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
                text = "Meu Perfil",
                modifier = Modifier.align(Alignment.Center),
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )


            Icon(
                imageVector = if (isEditing) Icons.Default.Save else Icons.Default.Edit,
                contentDescription = if (isEditing) "Salvar" else "Editar",
                tint = Color.White,
                modifier = Modifier
                    .size(24.dp)
                    .clickable {
                        if (isEditing) {

                            if (usuarioId != null) {

                            }
                        } else {
                            viewModel.setEditing(true)
                        }
                    }
                    .align(Alignment.CenterEnd)
            )
        }


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {

            if (uiState is PerfilUiState.Loading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFF1E355E))
                }
            }


            if (usuario != null) {
                PerfilCard(
                    usuario = usuario!!,
                    isEditing = isEditing,
                    onEditClick = { viewModel.setEditing(true) },
                    onSaveClick = { dadosAtualizados ->
                        if (usuarioId != null) {
                            viewModel.atualizarPerfil(usuarioId!!, dadosAtualizados, context)
                        }
                    },
                    onCancelClick = { viewModel.setEditing(false) }
                )
            } else if (uiState !is PerfilUiState.Loading) {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Sem perfil",
                        tint = Color(0xFF1E355E).copy(alpha = 0.5f),
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Perfil não encontrado",
                        color = Color(0xFF1E355E).copy(alpha = 0.7f),
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "Recarregue a página ou tente novamente",
                        color = Color(0xFF1E355E).copy(alpha = 0.5f),
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
fun PerfilCard(
    usuario: com.thais.app_gymflow.views.network.UsuarioPerfilResponse?,
    isEditing: Boolean,
    onEditClick: () -> Unit,
    onSaveClick: (UsuarioUpdateRequest) -> Unit,
    onCancelClick: () -> Unit
) {
    var showEditDialog by remember { mutableStateOf(false) }

    if (usuario == null) {
        Text("Carregando dados...", modifier = Modifier.padding(16.dp))
        return
    }
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Informações Pessoais",
                    color = Color(0xFF1E355E),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                if (!isEditing) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Editar",
                        tint = Color(0xFF1E355E),
                        modifier = Modifier
                            .size(24.dp)
                            .clickable {
                                showEditDialog = true
                            }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))


            PerfilInfoRow("Nome:", usuario.nome)
            PerfilInfoRow("Email:", usuario.email)
            PerfilInfoRow("CPF:", formatarCPF(usuario.CPF))
            PerfilInfoRow("Data de Nascimento:", formatarData(usuario.dataNascimento))
            PerfilInfoRow("Idade:", "${usuario.idade ?: "N/A"} anos")
            PerfilInfoRow("Peso:", "${usuario.peso ?: "N/A"} kg")
            PerfilInfoRow("Altura:", "${usuario.altura ?: "N/A"} m")
        }
    }


    if (showEditDialog) {
        EditarPerfilDialog(
            usuario = usuario,
            onDismiss = {
                showEditDialog = false
                onCancelClick()
            },
            onConfirm = { dadosAtualizados ->
                showEditDialog = false
                onSaveClick(dadosAtualizados)
            }
        )
    }
}

@Composable
fun PerfilInfoRow(label: String, value: String?) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = label,
            color = Color(0xFF1E355E),
            fontWeight = FontWeight.Medium,
            modifier = Modifier.width(120.dp)
        )
        Text(
            text = value ?: "Não informado",
            color = Color(0xFF1E355E).copy(alpha = 0.8f)
        )
    }
}

@Composable
fun EditarPerfilDialog(
    usuario: com.thais.app_gymflow.views.network.UsuarioPerfilResponse,
    onDismiss: () -> Unit,
    onConfirm: (UsuarioUpdateRequest) -> Unit
) {
    var nome by remember { mutableStateOf(usuario?.nome ?: "") }
    var cpf by remember { mutableStateOf(usuario?.CPF ?: "") }
    var peso by remember { mutableStateOf((usuario?.peso ?: 0.0).toString()) }
    var altura by remember { mutableStateOf((usuario?.altura ?: 0.0).toString()) }
    var dataNascimento by remember { mutableStateOf(usuario?.dataNascimento ?: "") }

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
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = "Editar Perfil",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E355E),
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                OutlinedTextField(
                    value = nome,
                    onValueChange = { nome = it },
                    label = { Text("Nome Completo") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    shape = RoundedCornerShape(8.dp)
                )

                OutlinedTextField(
                    value = cpf,
                    onValueChange = { cpf = it },
                    label = { Text("CPF") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    shape = RoundedCornerShape(8.dp)

                )

                OutlinedTextField(
                    value = peso,
                    onValueChange = { peso = it },
                    label = { Text("Peso (kg)") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    shape = RoundedCornerShape(8.dp)

                )

                OutlinedTextField(
                    value = altura,
                    onValueChange = { altura = it },
                    label = { Text("Altura (m)") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    shape = RoundedCornerShape(8.dp)

                )

                OutlinedTextField(
                    value = dataNascimento,
                    onValueChange = { dataNascimento = it },
                    label = { Text("Data de Nascimento (yyyy-MM-dd)") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp),
                    shape = RoundedCornerShape(8.dp),
                    placeholder = { Text("2025-11-30") }
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
                            val pesoDouble = peso.toDoubleOrNull() ?: usuario.peso
                            val alturaDouble = altura.toDoubleOrNull() ?: usuario.altura

                            val dadosAtualizados = UsuarioUpdateRequest(
                                nome = nome,
                                cpf = cpf,
                                peso = pesoDouble,
                                altura = alturaDouble,
                                dataNascimento = dataNascimento
                            )
                            onConfirm(dadosAtualizados)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E355E))
                    ) {
                        Text("Salvar")
                    }
                }
            }
        }
    }
}


fun formatarCPF(cpf: String?): String {

    if (cpf.isNullOrEmpty()) {
        return "Não informado"
    }

    val cpfNumerico = cpf.replace("[^\\d]".toRegex(), "")


    if (cpfNumerico.length != 11) {
        return cpf
    }

    return "${cpfNumerico.substring(0, 3)}.${cpfNumerico.substring(3, 6)}.${cpfNumerico.substring(6, 9)}-${cpfNumerico.substring(9)}"
}

private fun formatarData(data: String?): String {
    if (data.isNullOrEmpty()) {
        return "Não informado"
    }

    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val date = inputFormat.parse(data)
        outputFormat.format(date ?: return data)
    } catch (e: Exception) {
        data
    }
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true)
@Composable
fun PreviewPerfilScreen() {
    PerfilScreen(navController = androidx.navigation.compose.rememberNavController())
}