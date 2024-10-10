package com.example.armarket.Componentes

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.armarket.ViewModel.LoginViewModel

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = viewModel(),
    onGoogleSignIn: () -> Unit,
    onFacebookSignIn: () -> Unit,
    onPasswordRecovery: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Campo de texto para el email
        TextField(
            value = viewModel.email,
            onValueChange = { viewModel.email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Campo de texto para la contraseña
        TextField(
            value = viewModel.password,
            onValueChange = { viewModel.password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Botón de login
        Button(
            onClick = { viewModel.login() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Login")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Botones de inicio de sesión con redes sociales
        Button(
            onClick = onGoogleSignIn,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Login with Google")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = onFacebookSignIn,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Login with Facebook")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Botón de recuperación de contraseña
        Button(
            onClick = onPasswordRecovery,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Recover Password")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Mostrar estado del login
        Text(text = viewModel.loginState)
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen(onGoogleSignIn = {}, onFacebookSignIn = {}, onPasswordRecovery = {})
}
