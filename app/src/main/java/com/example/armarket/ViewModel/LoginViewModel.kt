package com.example.armarket.ViewModel

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.facebook.AccessToken
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var loginState by mutableStateOf("")
    var googleLoginState by mutableStateOf("")
        private set

    private val auth: FirebaseAuth = Firebase.auth

    // Iniciar sesión con correo y contraseña
    fun login() {
        if (email.isEmpty() || password.isEmpty()) {
            loginState = "Por favor rellena todos los campos"
            return
        }

        viewModelScope.launch {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        loginState = "Inicio de sesión correcto."
                    } else {
                        loginState = "Error al iniciar sesión: ${task.exception?.message}"
                    }
                }
        }
    }

    // Recuperación de contraseña por correo
    fun recoverPasswordByEmail() {
        if (email.isEmpty()) {
            loginState = "Por favor, introduce tu correo para recuperar la contraseña"
            return
        }

        viewModelScope.launch {
            auth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        loginState = "Correo de recuperación enviado"
                    } else {
                        loginState = "Error al enviar correo: ${task.exception?.message}"
                    }
                }
        }
    }

    // Iniciar sesión con Google
    fun loginWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        viewModelScope.launch {
            auth.signInWithCredential(credential)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        loginState = "Inicio de sesión con Google exitoso."
                    } else {
                        loginState = "Error en Google sign-in: ${task.exception?.message}"
                    }
                }
        }
    }

    // Iniciar sesión con Facebook
    fun loginWithFacebook(result: LoginResult) {
        val credential = FacebookAuthProvider.getCredential(result.accessToken.token)
        viewModelScope.launch {
            auth.signInWithCredential(credential)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        loginState = "Inicio de sesión con Facebook exitoso."
                    } else {
                        loginState = "Error en Facebook sign-in: ${task.exception?.message}"
                    }
                }
        }
    }
}
