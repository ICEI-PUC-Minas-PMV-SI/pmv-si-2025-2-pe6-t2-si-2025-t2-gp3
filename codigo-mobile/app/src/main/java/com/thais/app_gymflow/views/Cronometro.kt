package com.thais.app_gymflow.views

import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*

import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import androidx.navigation.NavController
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack

@Composable
fun CronometroScreen(navController: NavController) {
    var timeInSeconds by remember { mutableStateOf(0) }
    var isRunning by remember { mutableStateOf(false) }

    LaunchedEffect(isRunning) {
        while (isRunning) {
            delay(1000L)
            timeInSeconds++
        }
    }


    val minutes = timeInSeconds / 60
    val seconds = timeInSeconds % 60
    val formattedTime = String.format("%02d:%02d", minutes, seconds)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF052c51))
            .padding(16.dp)
    ) {

        IconButton(
            onClick = { navController.navigate("tela_inicial") },
            modifier = Modifier.align(Alignment.TopStart)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Voltar",
                tint = Color.White
            )
        }


        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Cronômetro",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            Text(
                text = formattedTime,
                fontSize = 64.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(40.dp))


            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = { isRunning = true },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF24ab54))
                ) {
                    Text("Iniciar")
                }

                Button(
                    onClick = { isRunning = false },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1a3c57))
                ) {
                    Text("Pausar")
                }

                Button(
                    onClick = {
                        isRunning = false
                        timeInSeconds = 0
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F))
                ) {
                    Text("Zerar")
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun CronometroPreview() {
    val navController = rememberNavController()
    CronometroScreen(navController = navController)
}