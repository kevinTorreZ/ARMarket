package com.example.armarket.ViewModel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    // Usamos la sintaxis get/set para las propiedades que serán observadas por Compose
    var email by mutableStateOf("")

    var password by mutableStateOf("")

    var loginState by mutableStateOf("")
        private set

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun login() {
        if (email.isEmpty() || password.isEmpty()) {
            loginState = "Please fill in all fields"
            return
        }

        viewModelScope.launch {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        loginState = "Login Successful"
                    } else {
                        loginState = "Login Failed: ${task.exception?.message}"
                    }
                }
        }
    }

    fun register() {
        if (email.isEmpty() || password.isEmpty()) {
            loginState = "Please fill in all fields"
            return
        }

        viewModelScope.launch {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        loginState = "Registration Successful"
                    } else {
                        loginState = "Registration Failed: ${task.exception?.message}"
                    }
                }
        }
    }
}
