package com.thais.app_gymflow.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.compose.foundation.border
import androidx.navigation.compose.rememberNavController
import com.thais.app_gymflow.views.utils.SessionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import android.util.Log
import java.util.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.thais.app_gymflow.viewmodels.TreinoViewModel
import com.thais.app_gymflow.views.utils.TokenUtils

@Composable
fun HomeScreen(navController: NavController) {
    val selectedDay = remember { mutableStateOf("T") }
    val days = listOf("S", "T", "Q", "Q", "S", "S", "D")

    var userName by remember { mutableStateOf("") }
    var greeting by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(true) }

    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }


    val treinoViewModel: TreinoViewModel = viewModel()


    LaunchedEffect(Unit) {
        loadUserData(sessionManager, { name ->
            userName = name
            greeting = getGreeting()
            isLoading = false
        }, { error ->
            userName = ""
            greeting = getGreeting()
            isLoading = false
        })
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
            Text(
                text = "GYMFLOW",
                modifier = Modifier.align(Alignment.TopCenter),
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 30.sp
            )
        }


        if (isLoading) {
            Text(
                text = "Carregando...",
                modifier = Modifier
                    .padding(start = 16.dp, top = 24.dp, end = 16.dp, bottom = 8.dp),
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium
            )
        } else {
            Text(
                text = "$greeting, $userName!",
                modifier = Modifier
                    .padding(start = 16.dp, top = 24.dp, end = 16.dp, bottom = 8.dp),
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium
            )
        }


        Card(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Frequência de Treinos",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    days.forEach { day ->
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(40.dp)
                                .background(
                                    if (selectedDay.value == day) Color(0xFF1E88E5)
                                    else Color.Transparent,
                                    shape = CircleShape
                                )
                                .border(2.dp, Color(0xFF1E88E5), shape = CircleShape)
                                .clickable { selectedDay.value = day }
                        ) {
                            Text(
                                text = day,
                                color = if (selectedDay.value == day) Color.White else Color(0xFF1E88E5),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Column(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            val buttonData = listOf(
                "Treinos" to Icons.Default.FitnessCenter,
                "Metas" to Icons.Default.Assignment,
                "Meu Progresso" to Icons.Default.BarChart,
                "Cronômetro" to Icons.Default.AccessTime
            )

            buttonData.chunked(2).forEach { rowItems ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    rowItems.forEach { (title, icon) ->
                        Card(
                            modifier = Modifier
                                .weight(1f)
                                .height(100.dp)
                                .clickable {

                                    when (title) {
                                        "Treinos" -> {

                                            var usuarioId = sessionManager.getUserId()


                                            if (usuarioId == 1L) {
                                                val token = sessionManager.getAuthToken()
                                                if (!token.isNullOrEmpty()) {
                                                    val extractedId = TokenUtils.extractUserIdFromToken(token)
                                                    if (extractedId != -1L) {
                                                        usuarioId = extractedId
                                                        sessionManager.saveUserId(extractedId)
                                                    }
                                                }
                                            }

                                            if (usuarioId != -1L) {
                                               CoroutineScope(Dispatchers.IO).launch {
                                                    treinoViewModel.carregarTreinos(usuarioId, context)
                                               }
                                                navController.navigate("treinos")
                                            } else {

                                                navController.navigate("treinos")
                                            }
                                        }
                                        "Metas" -> navController.navigate("metas")
                                        "Meu Progresso" -> navController.navigate("progresso")
                                        "Cronômetro" -> navController.navigate("cronometro")
                                        "Perfil" -> navController.navigate("perfil")
                                    }
                                },
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E355E))
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(12.dp),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    icon,
                                    contentDescription = null,
                                    tint = Color(0xFF00A0FF),
                                    modifier = Modifier.size(28.dp)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = title,
                                    color = Color.White,
                                    textAlign = TextAlign.Center,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }
                }
            }
        }


        Spacer(modifier = Modifier.weight(1f))


        BottomNavigationBar(navController)
    }
}

private fun getGreeting(): String {
    val calendar = Calendar.getInstance()
    val hour = calendar.get(Calendar.HOUR_OF_DAY)

    return when {
        hour in 5..11 -> "Bom dia"
        hour in 12..17 -> "Boa tarde"
        else -> "Boa noite"
    }
}

private fun loadUserData(
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

@Composable
fun BottomNavigationBar(navController: NavController) {
    var selectedItem by remember { mutableStateOf(0) }
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }

    val items = listOf("Início", "Perfil")
    val icons = listOf(
        Icons.Default.Home,
        Icons.Default.Person
    )

    Button(
        onClick = {
            val token = sessionManager.getAuthToken()
            val userId = sessionManager.getUserId()


            if (!token.isNullOrEmpty()) {
                val extractedId = TokenUtils.extractUserIdFromToken(token)
                val isValid = TokenUtils.isTokenValid(token)

            }

        },
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
    ) {
        Text("🔍 Debug Token")
    }

    NavigationBar(
        containerColor = Color(0xFF1E355E)
    ) {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = selectedItem == index,
                onClick = {
                    selectedItem = index
                    when (item) {
                        "Início" -> {

                        }
                        "Perfil" -> {


                            val token = sessionManager.getAuthToken()
                            if (token.isNullOrEmpty()) {
                                navController.navigate("login") {
                                    popUpTo("menu_inicial") { inclusive = true }
                                }
                            } else {

                                if (TokenUtils.isTokenValid(token)) {
                                    navController.navigate("perfil")
                                } else {

                                    navController.navigate("login") {
                                        popUpTo("menu_inicial") { inclusive = true }
                                    }
                                }
                            }
                        }
                    }
                },
                icon = {
                    Icon(
                        icons[index],
                        contentDescription = item,
                        tint = Color.White
                    )
                },
                label = {
                    Text(
                        item,
                        color = Color.White,
                        fontSize = 12.sp
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.White,
                    selectedTextColor = Color.White,
                    unselectedIconColor = Color.White.copy(alpha = 0.7f),
                    unselectedTextColor = Color.White.copy(alpha = 0.7f),
                    indicatorColor = Color(0xFF00A0FF)
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewHomeScreen() {
    HomeScreen(navController = rememberNavController())
}