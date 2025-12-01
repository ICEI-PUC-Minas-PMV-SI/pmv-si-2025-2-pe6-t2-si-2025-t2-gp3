package com.thais.app_gymflow.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.thais.app_gymflow.R
import com.thais.app_gymflow.ui.theme.VERDELIMA
import com.thais.app_gymflow.viewmodels.CadastroViewModel
import com.thais.app_gymflow.viewmodels.CadastroUiState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState

@Composable
fun Cadastro(navController: NavController) {
    val viewModel: CadastroViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()

    var nome by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var confirmarSenha by remember { mutableStateOf("") }
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorDialogMessage by remember { mutableStateOf("") }

    LaunchedEffect(uiState) {
        when (uiState) {
            is CadastroUiState.Success -> {
                navController.navigate("login") {
                    popUpTo("cadastro") { inclusive = true }
                }
            }
            is CadastroUiState.Error -> {
                val errorMessage = (uiState as CadastroUiState.Error).message
                if (errorMessage.contains("409") || errorMessage.contains("já está cadastrado")) {
                    errorDialogMessage = "Este usuário já está cadastrado"
                    showErrorDialog = true
                } else {
                    errorDialogMessage = errorMessage
                    showErrorDialog = true
                }
            }
            else -> {}
        }
    }

    if (showErrorDialog) {
        AlertDialog(
            onDismissRequest = {
                showErrorDialog = false
                viewModel.resetState()
            },
            title = {
                Text("Erro no Cadastro")
            },
            text = {
                Text(errorDialogMessage)
            },
            confirmButton = {
                Button(
                    onClick = {
                        showErrorDialog = false
                        viewModel.resetState()
                    }
                ) {
                    Text("OK")
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.gymflowlogo),
            contentDescription = null,
            modifier = Modifier
                .width(500.dp)
                .height(250.dp)
                .padding(top = 50.dp),
            contentScale = ContentScale.Fit
        )

        Text(
            text = "Mais do que treinar, é evoluir com inteligência, foco e tecnologia.",
            color = Color.DarkGray,
            fontSize = 15.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp, vertical = 8.dp)
        )

        Text(
            text = "CADASTRE-SE",
            color = Color(0xFF1a3c57),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 30.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = nome,
            onValueChange = { nome = it },
            placeholder = { Text("Nome Completo") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp, vertical = 4.dp),
            shape = RoundedCornerShape(5.dp),
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            placeholder = { Text("E-mail") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp, vertical = 4.dp),
            shape = RoundedCornerShape(5.dp),
        )

        OutlinedTextField(
            value = senha,
            onValueChange = { senha = it },
            placeholder = { Text("Senha") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp, vertical = 4.dp),
            shape = RoundedCornerShape(5.dp),
            visualTransformation = PasswordVisualTransformation() // AGORA FUNCIONA
        )

        OutlinedTextField(
            value = confirmarSenha,
            onValueChange = { confirmarSenha = it },
            placeholder = { Text("Confirmação de Senha") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp, vertical = 4.dp),
            shape = RoundedCornerShape(5.dp),
            visualTransformation = PasswordVisualTransformation() // AGORA FUNCIONA
        )

        if (uiState is CadastroUiState.Loading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .padding(16.dp),
                color = VERDELIMA
            )
        }

        Button(
            onClick = {
                when {
                    nome.isEmpty() || email.isEmpty() || senha.isEmpty() -> {
                        errorDialogMessage = "Preencha todos os campos"
                        showErrorDialog = true
                    }
                    senha != confirmarSenha -> {
                        errorDialogMessage = "As senhas não conferem"
                        showErrorDialog = true
                    }
                    else -> {
                        viewModel.cadastrarUsuario(nome, email, senha)
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = VERDELIMA,
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(5.dp),
            modifier = Modifier
                .width(300.dp)
                .height(50.dp)
                .padding(top = 17.dp),
            enabled = uiState !is CadastroUiState.Loading
        ) {
            Text(
                text = if (uiState is CadastroUiState.Loading) "CADASTRANDO..." else "CADASTRE-SE",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CadastroPreview() {
    val navController = rememberNavController()
    Cadastro(navController = navController)
}