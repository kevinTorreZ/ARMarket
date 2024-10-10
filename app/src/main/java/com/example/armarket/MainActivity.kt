package com.example.armarket

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.example.armarket.ui.theme.ARMarketTheme
import com.example.armarket.Componentes.LoginScreen
import com.example.armarket.ViewModel.LoginViewModel
import com.facebook.CallbackManager
import com.facebook.login.LoginManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.GoogleAuthProvider

class MainActivity : ComponentActivity() {

    private lateinit var callbackManager: CallbackManager
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth

    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicialización de Facebook CallbackManager
        callbackManager = CallbackManager.Factory.create()

        // Configuración de Google Sign-In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)) // Asegúrate de tener este ID en strings.xml
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
        auth = FirebaseAuth.getInstance()

        // Lanzador para manejar el resultado de Google Sign-In
        val googleSignInLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            handleGoogleSignInResult(task)
        }

        setContent {
            ARMarketTheme {
                LoginScreen(
                    viewModel = loginViewModel,
                    onGoogleSignIn = {
                        val signInIntent = googleSignInClient.signInIntent
                        googleSignInLauncher.launch(signInIntent)
                    },
                    onFacebookSignIn = {
                        LoginManager.getInstance().logInWithReadPermissions(
                            this,
                            listOf("email", "public_profile")
                        )
                    },
                    onPasswordRecovery = { loginViewModel.recoverPasswordByEmail() }
                )
            }
        }
    }

    // Manejar el resultado de Google Sign-In
    private fun handleGoogleSignInResult(task: Task<GoogleSignInAccount>) {
        try {
            val account = task.getResult(Exception::class.java)
            firebaseAuthWithGoogle(account.idToken!!)
        } catch (e: Exception) {
            e.printStackTrace()
            loginViewModel.loginState = "Error en Google sign-in: ${e.message}"
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    loginViewModel.loginState = "Error en Google sign-in: ${task.exception?.message}"
                }
            }
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            loginViewModel.loginState = "Inicio de sesión exitoso con Google"
        } else {
            loginViewModel.loginState = "Error en el inicio de sesión"
        }
    }

    // Manejar los resultados de las autenticaciones
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }
}
