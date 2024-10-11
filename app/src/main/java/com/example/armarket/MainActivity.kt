package com.example.armarket

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.example.armarket.Componentes.LoginScreen
import com.example.armarket.Componentes.HomeScreen // Asegúrate de tener esta importación
import com.example.armarket.ViewModel.LoginViewModel

class MainActivity : ComponentActivity() {

    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppNavGraph(navController = rememberNavController())
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun AppNavGraph(navController: NavHostController) {
        NavHost(navController = navController, startDestination = "login") {
            composable("login") {
                LoginScreen(
                    viewModel = loginViewModel,
                    onGoogleSignIn = { signInWithGoogle() },
                    onFacebookSignIn = { signInWithFacebook() },
                    onPasswordRecovery = { loginViewModel.recoverPasswordByEmail() },
                    onLoginSuccess = {
                        navController.navigate("home") {
                            popUpTo("login") { inclusive = true } // Opcional: limpiar la pila de navegación
                        }
                    }
                )
            }
            composable("home") {
                HomeScreen() // Asegúrate de tener esta pantalla definida
            }
        }
    }

    private fun signInWithGoogle() {
        val idToken = "google-id-token" // Reemplazar con el token real
        loginViewModel.firebaseAuthWithGoogle(idToken)
    }

    private fun signInWithFacebook() {
        val result = "facebook-result" // Reemplazar con el resultado real de Facebook
        // Implementa la lógica para iniciar sesión con Facebook aquí
    }
}
