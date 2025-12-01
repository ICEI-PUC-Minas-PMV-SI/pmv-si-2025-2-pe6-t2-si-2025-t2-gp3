package com.thais.app_gymflow.views

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.thais.app_gymflow.R
import com.thais.app_gymflow.ui.theme.VERDELIMA

@Composable

fun Inicio(navController: NavController){
    Column(
        modifier = Modifier.fillMaxSize().background(Color.White).verticalScroll(state = rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){

        Image(
         painter = painterResource(id = R.drawable.logogymflow),
            contentDescription = null,
            modifier = Modifier.width(width = 520.dp).height(height = 440.dp),
            contentScale = ContentScale.Crop,
            alignment = Alignment.Center
        )
        Text(
            text = "Mais do que treinar, é evoluir com inteligência, foco e tecnologia",
            fontSize = 25.sp,
            color = Color.DarkGray,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth().padding(start = 20.dp, top = 0.dp, end = 20.dp, bottom = 100.dp),
        )
        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ){
            Button(
                onClick = {
                    navController.navigate("cadastro")
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent
                ),
                border = BorderStroke(
                    width = 1.dp,
                    color = Color.DarkGray
                )
            ) {
               Text(
                   text = "Cadastra-se",
                    fontSize = 18.sp,
                   fontWeight = FontWeight.Bold,
                   color = Color.DarkGray,
               )
            }

            Button(
                onClick = {
                    navController.navigate("login")
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = VERDELIMA
                ),
                modifier = Modifier.shadow(
                    elevation = 15.dp,
                    shape = CircleShape,
                    spotColor = VERDELIMA
                )
            ) {
                Text(
                    text = "Fazer Login",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                )
            }

        }

    }
}

@Preview(showBackground = true)
@Composable
private fun InicioPreview() {
    val navController = rememberNavController()
    Inicio(navController = navController)
}

