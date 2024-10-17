package com.example.armarket

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import androidx.lifecycle.lifecycleScope
import com.example.armarket.Componentes.LoginScreen
import com.example.armarket.Componentes.HomeScreen
import com.example.armarket.ViewModel.LoginViewModel
import com.example.armarket.ViewModel.LocationViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private lateinit var credentialManager: CredentialManager

    private val loginViewModel: LoginViewModel by viewModels()
    private val locationViewModel: LocationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        credentialManager = CredentialManager.create(this)

        requestLocationPermission()

        setContent {
            AppNavGraph(navController = rememberNavController(), locationViewModel = locationViewModel)
        }
    }

    private fun requestLocationPermission() {
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
        } else {
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
                    onGoogleSignIn = { requestGoogleCredential() },
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
                HomeScreen(locationViewModel)
            }
        }
    }

    private fun requestGoogleCredential() {
        val request = GetCredentialRequest.Builder()
            .build()

        lifecycleScope.launch {
            try {
                val response = credentialManager.getCredential(this@MainActivity, request)
                Log.d("CredentialManager", "Credencial recibida: ${response.credential}")
                // Aquí puedes navegar a la pantalla de inicio después de un inicio de sesión exitoso
            } catch (e: GetCredentialException) {
                Log.e("CredentialManager", "Error al obtener la credencial", e)
                // Aquí puedes iniciar el flujo de inicio de sesión si no se encuentra la credencial
                // startGoogleSignIn() (No se necesita si solo usas Credential Manager)
            } catch (e: Exception) {
                Log.e("CredentialManager", "Error desconocido", e)
            }
        }
    }

    private fun signInWithFacebook() {
        // Implementar lógica de inicio de sesión con Facebook aquí
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }
}
