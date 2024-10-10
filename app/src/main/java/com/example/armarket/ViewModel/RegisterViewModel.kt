package com.example.armarket.ViewModel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {

    var email by mutableStateOf("")

    var password by mutableStateOf("")

    var RegisterState by mutableStateOf("")
        private set

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun register() {
        if (email.isEmpty() || password.isEmpty()) {
            RegisterState = "Porfavor rellena todos los campos"
            return
        }

        viewModelScope.launch {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        RegisterState = "Se ha registrado correctamente!"
                    } else {
                        RegisterState = "Ocurrio un error al registrarse: ${task.exception?.message}"
                    }
                }
        }
    }
}