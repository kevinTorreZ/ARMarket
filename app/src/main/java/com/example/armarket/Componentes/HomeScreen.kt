package com.example.armarket.Componentes

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.livedata.observeAsState
import com.example.armarket.ViewModel.LocationViewModel

@Composable
fun HomeScreen(locationViewModel: LocationViewModel = viewModel()) {
    // Observar la ubicación desde el ViewModel
    val location by locationViewModel.location.observeAsState()
    val isGpsEnabled by locationViewModel.isGpsEnabled.observeAsState(true)
    val nearbySupermarkets by locationViewModel.nearbySupermarkets.observeAsState(emptyList())

    // Inicia la obtención de supermercados cercanos si la ubicación está disponible
    LaunchedEffect(Unit) {
        locationViewModel.requestLocationUpdates()
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(text = "Bienvenido a la pantalla principal", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        // Verifica si el GPS está habilitado antes de mostrar los supermercados
        if (isGpsEnabled) {
            if (nearbySupermarkets.isNotEmpty()) {
                Text(text = "Supermercados cercanos:", style = MaterialTheme.typography.titleMedium)
                nearbySupermarkets.forEach { supermarket ->
                    Text(text = "${supermarket.name} / Distancia: ${"%.2f".format(supermarket.distance / 1000)} km") // Dividir por 1000 para convertir a kilómetros
                }
            } else {
                Text(text = "No se encontraron supermercados cercanos.")
            }
        } else {
            Text(text = "El GPS está desactivado. Por favor, actívalo para ver los supermercados cercanos.")
        }
    }
}
