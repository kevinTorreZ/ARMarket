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
import androidx.compose.runtime.livedata.observeAsState // Asegúrate de incluir esta línea

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = viewModel(),
    onGoogleSignIn: () -> Unit,
    onFacebookSignIn: () -> Unit,
    onPasswordRecovery: () -> Unit,
    onLoginSuccess: () -> Unit // Agregado el callback
) {
    val email by viewModel.email.observeAsState("")
    val password by viewModel.password.observeAsState("")
    val emailError by viewModel.emailError.observeAsState("")
    val passwordError by viewModel.passwordError.observeAsState("")
    val loginState by viewModel.loginState.observeAsState("")
    val isLoginSuccessful by viewModel.isLoginSuccessful.observeAsState(false)
    val isLoading by viewModel.isLoading.observeAsState(false)

    // Llamar a onLoginSuccess si el inicio de sesión es exitoso
    LaunchedEffect(isLoginSuccessful) {
        if (isLoginSuccessful) {
            onLoginSuccess() // Llamar al callback en caso de éxito
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Campo de texto para el email
        TextField(
            value = email,
            onValueChange = { viewModel.updateEmail(it) },
            label = { Text("Email") },
            isError = emailError.isNotEmpty(),
            modifier = Modifier.fillMaxWidth()
        )
        if (emailError.isNotEmpty()) {
            Text(
                text = emailError,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Campo de texto para la contraseña
        TextField(
            value = password,
            onValueChange = { viewModel.updatePassword(it) },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            isError = passwordError.isNotEmpty()
        )
        if (passwordError.isNotEmpty()) {
            Text(
                text = passwordError,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Indicador de carga durante el login
        if (isLoading) {
            CircularProgressIndicator()
        } else {
            // Botón de login
            Button(
                onClick = { viewModel.login() }, // Llamando a la función login() ahora
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Login")
            }
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
            onClick = {
                viewModel.recoverPasswordByEmail() // Añadir esta línea si quieres usarlo
                onPasswordRecovery()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Recover Password")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Mostrar estado del login
        if (loginState.isNotEmpty()) {
            Text(
                text = loginState,
                color = if (isLoginSuccessful) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen(onGoogleSignIn = {}, onFacebookSignIn = {}, onPasswordRecovery = {}, onLoginSuccess = {})
}
