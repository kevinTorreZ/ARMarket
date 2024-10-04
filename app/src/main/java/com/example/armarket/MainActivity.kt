package com.example.armarket
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxSize
import com.example.armarket.ui.theme.ARMarketTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.armarket.Componentes.LoginScreen
import com.example.armarket.ViewModel.LoginViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()  // Esto es para una transición de diseño fluida, lo puedes mantener si ya lo tienes configurado.

        setContent {
            ARMarketTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) {
                    // Aquí pasamos el ViewModel y mostramos la pantalla de Login
                    LoginScreen(viewModel = viewModel())
                }
            }
        }
    }
}
