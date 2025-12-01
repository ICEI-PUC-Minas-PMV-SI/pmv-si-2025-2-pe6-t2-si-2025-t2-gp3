package com.thais.app_gymflow.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.util.Log
import com.thais.app_gymflow.views.network.RetrofitClient
import com.thais.app_gymflow.views.network.LoginRequest
import com.thais.app_gymflow.views.utils.SessionManager
import androidx.lifecycle.viewmodel.compose.viewModel
import com.thais.app_gymflow.viewmodels.TreinoViewModel
import com.thais.app_gymflow.views.utils.TokenUtils

@Composable
fun Login(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }
    val treinoViewModel: TreinoViewModel = viewModel()

    fun loadUserData(
        sessionManager: SessionManager,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        val token = sessionManager.getAuthToken()
        val userEmail = sessionManager.getUserEmail()

        if (token.isNullOrEmpty() || userEmail.isNullOrEmpty()) {
            onError("Token ou email não encontrado")
            return
        }

        val firstName = userEmail.split("@").firstOrNull() ?: "Usuário"

        val formattedName = firstName.replaceFirstChar { it.uppercase() }

        onSuccess(formattedName)

    }

    fun processarLogin() {
        if (email.isBlank() || password.isBlank()) {
            errorMessage = "Por favor, preencha todos os campos"
            return
        }

        isLoading = true
        errorMessage = null

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val loginRequest = LoginRequest(
                    email = email.trim(),
                    senha = password
                )


                val responses = listOf(
                    "login" to try { RetrofitClient.api.login(loginRequest) } catch (e: Exception) { null },
                    "auth/login" to try { RetrofitClient.api.login2(loginRequest) } catch (e: Exception) { null },
                    "api/login" to try { RetrofitClient.api.login3(loginRequest) } catch (e: Exception) { null },
                    "api/usuarios/login" to try { RetrofitClient.api.login4(loginRequest) } catch (e: Exception) { null },
                    "authenticate" to try { RetrofitClient.api.login5(loginRequest) } catch (e: Exception) { null }
                )

                var success = false
                var loginError: String? = null

                for ((endpointName, response) in responses) {
                    if (response != null) {

                        when (response.code()) {
                            200, 201 -> {
                                val loginResponse = response.body()
                                val token = loginResponse?.token

                                if (!token.isNullOrEmpty()) {

                                    withContext(Dispatchers.Main) {
                                        sessionManager.saveAuthToken(token)
                                        sessionManager.saveUserEmail(email)

                                        val usuarioId = TokenUtils.extractUserIdFromToken(token)
                                        if (usuarioId != -1L) {
                                            sessionManager.saveUserId(usuarioId)
                                            Log.d("LOGIN_SUCCESS", "✅ UserId extraído do token: $usuarioId")

                                            loadUserData(
                                                sessionManager = sessionManager,
                                                onSuccess = { userName ->

                                                    treinoViewModel.carregarTreinos(usuarioId, context)

                                                    navController.navigate("menu_inicial") {
                                                        popUpTo("login") { inclusive = true }
                                                    }
                                                },
                                                onError = { error ->
                                                    treinoViewModel.carregarTreinos(usuarioId, context)
                                                    navController.navigate("menu_inicial") {
                                                        popUpTo("login") { inclusive = true }
                                                    }
                                                }
                                            )
                                        } else {
                                            isLoading = false

                                            loadUserData(
                                                sessionManager = sessionManager,
                                                onSuccess = { userName ->
                                                    treinoViewModel.carregarTreinos(1L, context)
                                                    navController.navigate("menu_inicial") {
                                                        popUpTo("login") { inclusive = true }
                                                    }
                                                },
                                                onError = { error ->
                                                    treinoViewModel.carregarTreinos(1L, context)
                                                    navController.navigate("menu_inicial") {
                                                        popUpTo("login") { inclusive = true }
                                                    }
                                                }
                                            )
                                        }
                                    }
                                    success = true
                                    break
                                } else {

                                }
                            }
                            401 -> {
                                loginError = "E-mail ou senha incorretos"
                            }
                            404 -> {

                            }
                            500 -> {
                                loginError = "Erro interno do servidor. Tente novamente."

                            }
                            else -> {

                            }
                        }
                    } else {

                    }
                }

                if (!success) {
                    withContext(Dispatchers.Main) {
                        isLoading = false
                        errorMessage = loginError ?: "Nenhum endpoint de login funcionou. Verifique com o administrador."
                    }
                }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    isLoading = false
                    errorMessage = "Erro de conexão: ${e.message}"

                }
            }
        }
    }

    fun executarDiagnosticoBackend() {
        CoroutineScope(Dispatchers.IO).launch {
            val token = sessionManager.getAuthToken()
            if (!token.isNullOrEmpty()) {

                try {

                    val apiService = RetrofitClient.getApiWithToken(token)
                    val response = apiService.getTreinosPorUsuario(35)


                    if (response.isSuccessful()) {

                        val treinos = response.body()
                    } else {
                        val errorBody = response.errorBody()?.string()

                        try {
                            val usuariosResponse = apiService.getUsuarios()


                            if (usuariosResponse.isSuccessful()) {

                                val usuarios = usuariosResponse.body()

                            } else {
                                val usuariosError = usuariosResponse.errorBody()?.string()
                            }
                        } catch (e: Exception) {

                        }
                    }
                    TokenUtils.debugToken(token)

                } catch (e: Exception) {

                    e.printStackTrace()
                }

            } else {

            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = com.thais.app_gymflow.R.drawable.logogymflow),
            contentDescription = "Logo GymFlow",
            modifier = Modifier
                .width(220.dp)
                .height(140.dp),
            contentScale = ContentScale.Fit
        )

        Text(
            text = "Bem-vindo de volta!",
            fontSize = 29.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1a3c57),
            modifier = Modifier.padding(bottom = 10.dp)
        )
        Text(
            text = "Entre para acessar seus treinos",
            fontSize = 17.sp,
            color = Color.DarkGray,
            modifier = Modifier.padding(bottom = 10.dp)
        )


        errorMessage?.let { message ->
            Text(
                text = message,
                color = if (message.contains("realizado")) Color(0xFF2E7D32) else Color.Red,
                fontSize = 14.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )
        }

        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                errorMessage = null
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            label = { Text(text = "E-mail", color = Color.DarkGray) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Email,
                    contentDescription = "E-mail",
                    tint = Color(0xFF052c51)
                )
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedTextColor = Color.DarkGray,
                unfocusedTextColor = Color.DarkGray,
                focusedLabelColor = Color(0xFF2E7D32),
                unfocusedLabelColor = Color.Gray,
                focusedIndicatorColor = Color(0xFF2E7D32),
                unfocusedIndicatorColor = Color.LightGray,
                cursorColor = Color(0xFF2E7D32)
            ),
            shape = RoundedCornerShape(12.dp),
            singleLine = true
        )

        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                errorMessage = null
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            label = { Text(text = "Senha", color = Color.DarkGray) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Lock,
                    contentDescription = "Senha",
                    tint = Color(0xFF052c51)
                )
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = PasswordVisualTransformation(),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedTextColor = Color.DarkGray,
                unfocusedTextColor = Color.DarkGray,
                focusedLabelColor = Color(0xFF2E7D32),
                unfocusedLabelColor = Color.Gray,
                focusedIndicatorColor = Color(0xFF2E7D32),
                unfocusedIndicatorColor = Color.LightGray,
                cursorColor = Color(0xFF2E7D32)
            ),
            shape = RoundedCornerShape(12.dp),
            singleLine = true
        )

        Button(
            onClick = {
                processarLogin()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF24ab54)),
            shape = RoundedCornerShape(12.dp),
            enabled = !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            } else {
                Text(
                    text = "Entrar",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }


        Button(
            onClick = {
                executarDiagnosticoBackend()
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Magenta),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        ) {
            Text("🔧 Diagnóstico Backend")
        }


        Button(
            onClick = {

                val token = sessionManager.getAuthToken()
                if (!token.isNullOrEmpty()) {
                    TokenUtils.debugToken(token)

                    val userId = TokenUtils.extractUserIdFromToken(token)

                } else {

                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Blue),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        ) {
            Text("🔍 Debug Token")
        }

        Button(
            onClick = {
                sessionManager.logout()
                navController.navigate("login") {
                    popUpTo("menu_inicial") { inclusive = true }
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        ) {
            Text("🔄 Forçar Novo Login")
        }

        TextButton(
            onClick = {
                navController.navigate("cadastro")
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text(
                text = "Não tem uma conta? Cadastre-se",
                color = Color(0xFF1a3c57),
                fontSize = 14.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginPreview() {
    val navController = rememberNavController()
    Login(navController = navController)
}