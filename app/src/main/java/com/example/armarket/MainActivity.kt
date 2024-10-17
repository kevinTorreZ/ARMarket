package com.example.armarket

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.example.armarket.Componentes.LoginScreen
import com.example.armarket.Componentes.HomeScreen
import com.example.armarket.ViewModel.LoginViewModel
import com.example.armarket.ViewModel.LocationViewModel
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember

class MainActivity : ComponentActivity() {

    private val loginViewModel: LoginViewModel by viewModels()
    private val locationViewModel: LocationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Solicitar permisos de ubicación
        requestLocationPermission()

        setContent {
            AppNavGraph(navController = rememberNavController(), locationViewModel = locationViewModel)
        }
    }

    private fun requestLocationPermission() {
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
        } else {
            // Los permisos ya están concedidos
            locationViewModel.requestLocationUpdates()
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun AppNavGraph(navController: NavHostController, locationViewModel: LocationViewModel) {
        NavHost(navController = navController, startDestination = "login") {
            composable("login") {
                LoginScreen(
                    viewModel = loginViewModel,
                    onGoogleSignIn = { signInWithGoogle() },
                    onFacebookSignIn = { signInWithFacebook() },
                    onPasswordRecovery = { loginViewModel.recoverPasswordByEmail() },
                    onLoginSuccess = {
                        navController.navigate("home") {
                            popUpTo("login") { inclusive = true }
                        }
                    }
                )
            }
            composable("home") {
                val locationViewModel: LocationViewModel by viewModels()
                HomeScreen(locationViewModel)
            }
        }
    }

    private fun signInWithGoogle() {
        val idToken = "google-id-token"
        loginViewModel.firebaseAuthWithGoogle(idToken)
    }

    private fun signInWithFacebook() {
        val result = "facebook-result" // Reemplazar con el resultado real de Facebook
        // Implementa la lógica para iniciar sesión con Facebook aquí
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }
}
