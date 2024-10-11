package com.example.armarket.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.facebook.login.LoginResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class LoginViewModel : ViewModel() {

    private val _email = MutableLiveData<String>()
    val email: LiveData<String> get() = _email

    private val _password = MutableLiveData<String>()
    val password: LiveData<String> get() = _password

    private val _emailError = MutableLiveData<String>()
    val emailError: LiveData<String> get() = _emailError

    private val _passwordError = MutableLiveData<String>()
    val passwordError: LiveData<String> get() = _passwordError

    private val _loginState = MutableLiveData<String>()
    val loginState: LiveData<String> get() = _loginState

    private val _isLoginSuccessful = MutableLiveData<Boolean>()
    val isLoginSuccessful: LiveData<Boolean> get() = _isLoginSuccessful

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val auth: FirebaseAuth = Firebase.auth

    fun updateEmail(newEmail: String) {
        _email.value = newEmail
    }

    fun updatePassword(newPassword: String) {
        _password.value = newPassword
    }

    // Iniciar sesión con Google
    fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)

        _isLoading.value = true

        viewModelScope.launch {
            try {
                auth.signInWithCredential(credential).await()
                _loginState.value = "Inicio de sesión con Google exitoso."
                _isLoginSuccessful.value = true
            } catch (e: Exception) {
                _loginState.value = "Error en Google sign-in: ${e.message}"
                _isLoginSuccessful.value = false
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Iniciar sesión con Facebook
    fun loginWithFacebook(result: LoginResult) {
        val credential = FacebookAuthProvider.getCredential(result.accessToken.token)

        _isLoading.value = true

        viewModelScope.launch {
            try {
                auth.signInWithCredential(credential).await()
                _loginState.value = "Inicio de sesión con Facebook exitoso."
                _isLoginSuccessful.value = true
            } catch (e: Exception) {
                _loginState.value = "Error en Facebook sign-in: ${e.message}"
                _isLoginSuccessful.value = false
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Recuperación de contraseña por correo
    fun recoverPasswordByEmail() {
        val currentEmail = _email.value ?: ""
        if (currentEmail.isEmpty()) {
            _emailError.value = "Por favor, introduce tu correo para recuperar la contraseña"
            return
        }

        _isLoading.value = true

        viewModelScope.launch {
            try {
                auth.sendPasswordResetEmail(currentEmail).await()
                _loginState.value = "Correo de recuperación enviado"
            } catch (e: Exception) {
                _loginState.value = "Error al enviar correo: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun login() {
        val currentEmail = _email.value ?: ""
        val currentPassword = _password.value ?: ""

        if (currentEmail.isEmpty() || currentPassword.isEmpty()) {
            _emailError.value = if (currentEmail.isEmpty()) "Por favor, introduce tu correo" else ""
            _passwordError.value = if (currentPassword.isEmpty()) "Por favor, introduce tu contraseña" else ""
            return
        }

        _isLoading.value = true

        viewModelScope.launch {
            try {
                auth.signInWithEmailAndPassword(currentEmail, currentPassword).await()
                _loginState.value = "Inicio de sesión exitoso."
                _isLoginSuccessful.value = true
            } catch (e: Exception) {
                _loginState.value = "Error en el inicio de sesión: ${e.message}"
                _isLoginSuccessful.value = false
            } finally {
                _isLoading.value = false
            }
        }
    }

}
