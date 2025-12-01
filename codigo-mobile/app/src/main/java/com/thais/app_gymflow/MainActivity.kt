package com.thais.app_gymflow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.thais.app_gymflow.ui.theme.AppGymFlowTheme
import com.thais.app_gymflow.views.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppGymFlowTheme {
                Surface {
                    AppNavigation()
                }
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "tela_inicial"
    ) {
        composable("tela_inicial") { Inicio(navController) }
        composable("login") { Login(navController) }
        composable("cadastro") { Cadastro(navController) }
        composable("menu_inicial") { HomeScreen(navController) }
        composable("cronometro") { CronometroScreen(navController) }
        composable("treinos") { TreinoScreen(navController) }
        composable("metas") { MetasScreen(navController) }
        composable("progresso") { ProgressoScreen(navController) }


        composable("fichas/{treinoId}") { backStackEntry ->
            val treinoId = backStackEntry.arguments?.getString("treinoId")?.toLongOrNull()
            FichaScreen(navController, treinoId)
        }
        composable("exercicios/{fichaId}") { backStackEntry ->
            val fichaId = backStackEntry.arguments?.getString("fichaId")?.toLongOrNull()
            ExercicioScreen(navController, fichaId)
        }

        composable("perfil") { PerfilScreen(navController) }
    }
}