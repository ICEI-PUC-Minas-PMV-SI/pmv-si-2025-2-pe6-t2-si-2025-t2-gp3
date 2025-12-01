package com.thais.app_gymflow.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import android.util.Log
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.thais.app_gymflow.viewmodels.MetaViewModel
import com.thais.app_gymflow.viewmodels.MetaUiState
import com.thais.app_gymflow.views.network.MetaRequest
import com.thais.app_gymflow.views.network.TipoMeta
import com.thais.app_gymflow.views.network.UnidadeMedida
import com.thais.app_gymflow.views.utils.SessionManager
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun MetasScreen(navController: NavController) {
    val viewModel: MetaViewModel = viewModel()
    val metas by viewModel.metas.collectAsState()
    val uiState by viewModel.uiState.collectAsState()
    val metaSelecionada by viewModel.metaSelecionada.collectAsState()

    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }
    var showDialogCriar by remember { mutableStateOf(false) }
    var showDialogDetalhes by remember { mutableStateOf(false) }
    var usuarioId by remember { mutableStateOf<Long?>(null) }


    LaunchedEffect(Unit) {
        val validUserId = sessionManager.getValidUserId()
        if (validUserId != -1L) {
            usuarioId = validUserId
            viewModel.carregarMetas(usuarioId!!, context)
        } else {

            navController.navigate("login") {
                popUpTo("metas") { inclusive = true }
            }
        }
    }


    LaunchedEffect(uiState) {
        when (uiState) {
            is MetaUiState.Success -> {
                showDialogCriar = false
            }
            else -> {}
        }
    }

    LaunchedEffect(metaSelecionada) {
        if (metaSelecionada != null) {
            showDialogDetalhes = true
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
                text = "Metas",
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
                text = "Minhas Metas",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E355E),
                modifier = Modifier.padding(bottom = 24.dp)
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        showDialogCriar = true
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
                            text = "Criar Nova Meta",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Text(
                            text = "Definir novo objetivo",
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 14.sp
                        )
                    }
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Criar Meta",
                        tint = Color(0xFF00A0FF),
                        modifier = Modifier.size(32.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))


            if (uiState is MetaUiState.Loading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFF1E355E))
                }
            }

            if (metas.isNotEmpty()) {
                Text(
                    text = "Metas Ativas (${metas.size})",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E355E),
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(metas) { meta ->
                        MetaCard(
                            meta = meta,
                            onCardClick = {
                                viewModel.selecionarMeta(meta)
                            }
                        )
                    }
                }
            } else if (uiState !is MetaUiState.Loading) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.Flag,
                        contentDescription = "Sem metas",
                        tint = Color(0xFF1E355E).copy(alpha = 0.5f),
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Nenhuma meta definida ainda",
                        color = Color(0xFF1E355E).copy(alpha = 0.7f),
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "Clique em 'Criar Nova Meta' para começar",
                        color = Color(0xFF1E355E).copy(alpha = 0.5f),
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }

    if (showDialogCriar) {
        CriarMetaDialog(
            onDismiss = { showDialogCriar = false },
            onConfirm = { tipo, atual, desejado, inicio, prazo, unidadeMedida ->
                if (usuarioId != null) {
                    val metaRequest = MetaRequest(
                        idUsuario = usuarioId!!,
                        tipo = tipo,
                        atual = atual,
                        desejado = desejado,
                        inicio = inicio,
                        prazo = prazo,
                        unidadeDeMedida = unidadeMedida
                    )
                    viewModel.criarMeta(metaRequest, context)
                }
            },
            isLoading = uiState is MetaUiState.Loading
        )
    }


    if (showDialogDetalhes && metaSelecionada != null) {
        DetalhesMetaDialog(
            meta = metaSelecionada!!,
            onDismiss = {
                showDialogDetalhes = false
                viewModel.limparMetaSelecionada()
            },
            onConcluir = { metaAtualizada ->
                if (usuarioId != null) {
                    val metaRequest = MetaRequest(
                        idUsuario = usuarioId!!,
                        tipo = metaAtualizada.tipo,
                        atual = metaAtualizada.atual,
                        desejado = metaAtualizada.desejado,
                        inicio = metaAtualizada.inicio,
                        prazo = metaAtualizada.prazo,
                        unidadeDeMedida = metaAtualizada.unidadeDeMedida
                    )
                    viewModel.atualizarMeta(metaAtualizada.id, metaRequest, context)
                }
            },
            isLoading = uiState is MetaUiState.Loading
        )
    }
}

@Composable
fun MetaCard(
    meta: com.thais.app_gymflow.views.network.MetaResponse,
    onCardClick: () -> Unit
) {
    val progresso = (meta.atual / meta.desejado * 100).coerceIn(0.0, 100.0)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCardClick() },
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
                    imageVector = Icons.Default.Flag,
                    contentDescription = "Meta",
                    tint = Color(0xFF00A0FF),
                    modifier = Modifier
                        .size(40.dp)
                        .padding(end = 16.dp)
                )

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = meta.tipo,
                        color = Color(0xFF1E355E),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    Text(
                        text = "${meta.atual} ${meta.unidadeDeMedida} / ${meta.desejado} ${meta.unidadeDeMedida}",
                        color = Color(0xFF1E355E).copy(alpha = 0.7f),
                        fontSize = 14.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )


                    LinearProgressIndicator(
                        progress = (progresso / 100).toFloat(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp),
                        color = Color(0xFF00A0FF),
                        trackColor = Color(0xFF1E355E).copy(alpha = 0.2f)
                    )

                    Text(
                        text = "Progresso: ${String.format("%.1f", progresso)}%",
                        color = Color(0xFF1E355E).copy(alpha = 0.7f),
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun CriarMetaDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, Double, Double, String, String, String) -> Unit,
    isLoading: Boolean
) {
    var tipo by remember { mutableStateOf(TipoMeta.PESO) }
    var atual by remember { mutableStateOf("") }
    var desejado by remember { mutableStateOf("") }
    var inicio by remember { mutableStateOf("") }
    var prazo by remember { mutableStateOf("") }
    var unidadeMedida by remember { mutableStateOf(UnidadeMedida.QUILOS) }

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
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = "Criar Nova Meta",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E355E),
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Seleção de Tipo
                Text(
                    text = "Tipo de Meta",
                    color = Color(0xFF1E355E),
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = tipo,
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
                            val currentIndex = TipoMeta.todos.indexOf(tipo)
                            val newIndex = if (currentIndex > 0) currentIndex - 1 else TipoMeta.todos.size - 1
                            tipo = TipoMeta.todos[newIndex]
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E355E))
                    ) {
                        Text("Anterior")
                    }
                    Button(
                        onClick = {
                            val currentIndex = TipoMeta.todos.indexOf(tipo)
                            val newIndex = (currentIndex + 1) % TipoMeta.todos.size
                            tipo = TipoMeta.todos[newIndex]
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E355E))
                    ) {
                        Text("Próximo")
                    }
                }

                OutlinedTextField(
                    value = atual,
                    onValueChange = { atual = it },
                    label = { Text("Valor Atual") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    shape = RoundedCornerShape(8.dp)

                )

                OutlinedTextField(
                    value = desejado,
                    onValueChange = { desejado = it },
                    label = { Text("Valor Desejado") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    shape = RoundedCornerShape(8.dp)

                )


                Text(
                    text = "Unidade de Medida",
                    color = Color(0xFF1E355E),
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = unidadeMedida,
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
                            val currentIndex = UnidadeMedida.todas.indexOf(unidadeMedida)
                            val newIndex = if (currentIndex > 0) currentIndex - 1 else UnidadeMedida.todas.size - 1
                            unidadeMedida = UnidadeMedida.todas[newIndex]
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E355E))
                    ) {
                        Text("Anterior")
                    }
                    Button(
                        onClick = {
                            val currentIndex = UnidadeMedida.todas.indexOf(unidadeMedida)
                            val newIndex = (currentIndex + 1) % UnidadeMedida.todas.size
                            unidadeMedida = UnidadeMedida.todas[newIndex]
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E355E))
                    ) {
                        Text("Próximo")
                    }
                }


                OutlinedTextField(
                    value = inicio.ifEmpty { dataAtual },
                    onValueChange = { inicio = it },
                    label = { Text("Data de Início (yyyy-MM-dd)") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    shape = RoundedCornerShape(8.dp),
                    placeholder = { Text(dataAtual) }
                )

                OutlinedTextField(
                    value = prazo.ifEmpty { dataAtual },
                    onValueChange = { prazo = it },
                    label = { Text("Prazo (yyyy-MM-dd)") },
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
                            val atualDouble = atual.toDoubleOrNull() ?: 0.0
                            val desejadoDouble = desejado.toDoubleOrNull() ?: 0.0
                            val inicioFinal = if (inicio.isEmpty()) dataAtual else inicio
                            val prazoFinal = if (prazo.isEmpty()) dataAtual else prazo

                            if (atualDouble > 0 && desejadoDouble > 0) {
                                onConfirm(tipo, atualDouble, desejadoDouble, inicioFinal, prazoFinal, unidadeMedida)
                            }
                        },
                        enabled = atual.isNotEmpty() && desejado.isNotEmpty() && !isLoading,
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

@Composable
fun DetalhesMetaDialog(
    meta: com.thais.app_gymflow.views.network.MetaResponse,
    onDismiss: () -> Unit,
    onConcluir: (com.thais.app_gymflow.views.network.MetaResponse) -> Unit,
    isLoading: Boolean
) {
    var valorAtual by remember { mutableStateOf(meta.atual.toString()) }
    var isConcluida by remember { mutableStateOf(false) }

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
                    text = "Detalhes da Meta",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E355E),
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                InfoRow("Tipo:", meta.tipo)
                InfoRow("Unidade:", meta.unidadeDeMedida)
                InfoRow("Início:", meta.inicio)
                InfoRow("Prazo:", meta.prazo)
                InfoRow("Valor Inicial:", "${meta.atual} ${meta.unidadeDeMedida}")
                InfoRow("Valor Desejado:", "${meta.desejado} ${meta.unidadeDeMedida}")

                Spacer(modifier = Modifier.height(16.dp))


                val progresso = (meta.atual / meta.desejado * 100).coerceIn(0.0, 100.0)
                Text(
                    text = "Progresso: ${String.format("%.1f", progresso)}%",
                    color = Color(0xFF1E355E),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                LinearProgressIndicator(
                    progress = (progresso / 100).toFloat(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(12.dp),
                    color = Color(0xFF00A0FF),
                    trackColor = Color(0xFF1E355E).copy(alpha = 0.2f)
                )

                Spacer(modifier = Modifier.height(24.dp))

                OutlinedTextField(
                    value = valorAtual,
                    onValueChange = { valorAtual = it },
                    label = { Text("Valor Atualizado") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    shape = RoundedCornerShape(8.dp)

                )


                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 24.dp)
                ) {
                    Checkbox(
                        checked = isConcluida,
                        onCheckedChange = { isConcluida = it },
                        colors = CheckboxDefaults.colors(
                            checkedColor = Color(0xFF00A0FF),
                            checkmarkColor = Color.White
                        )
                    )
                    Text(
                        text = "Meta concluída",
                        color = Color(0xFF1E355E),
                        modifier = Modifier.padding(start = 8.dp)
                    )
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
                        Text("Fechar")
                    }
                    Button(
                        onClick = {
                            val novoValor = valorAtual.toDoubleOrNull() ?: meta.atual
                            val metaAtualizada = meta.copy(
                                atual = if (isConcluida) meta.desejado else novoValor
                            )
                            onConcluir(metaAtualizada)
                        },
                        enabled = !isLoading,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E355E))
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                color = Color.White
                            )
                        } else {
                            Text("Salvar")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(
            text = label,
            color = Color(0xFF1E355E),
            fontWeight = FontWeight.Medium,
            modifier = Modifier.width(100.dp)
        )
        Text(
            text = value,
            color = Color(0xFF1E355E).copy(alpha = 0.8f)
        )
    }
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true)
@Composable
fun PreviewMetasScreen() {
    MetasScreen(navController = androidx.navigation.compose.rememberNavController())
}