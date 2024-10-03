package com.example.MyStick.data

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.loginflow.data.LoginUIEvent
import com.example.loginflow.data.LoginUIState
import com.example.loginflow.data.rules.Validator
import com.example.MyStick.data.rules.WalletViewModel
import com.example.MyStick.navigation.PostOfficeAppRouter
import com.example.MyStick.navigation.Screen
import com.google.firebase.auth.FirebaseAuth

class LoginViewModel : ViewModel() {
    private val TAG = LoginViewModel::class.simpleName
    var loginUIState = mutableStateOf(LoginUIState())
    var allValidationPassed = mutableStateOf(false)
    var loginProgress = mutableStateOf(false)
    private val walletViewModel = WalletViewModel() // Create an instance of WalletViewModel

    fun onEvent(event: LoginUIEvent) {
        when (event) {
            is LoginUIEvent.EmailChanged -> {
                loginUIState.value = loginUIState.value.copy(
                    email = event.email
                )
            }
            is LoginUIEvent.PasswordChanged -> {
                loginUIState.value = loginUIState.value.copy(
                    password = event.password
                )
            }
            is LoginUIEvent.LoginButtonClicked -> {
                login()
            }
        }
        validateLoginUIDataWithRules()
    }

    private fun validateLoginUIDataWithRules() {
        val emailResult = Validator.validateEmail(
            email = loginUIState.value.email
        )
        val passwordResult = Validator.validatePassword(
            password = loginUIState.value.password
        )
        loginUIState.value = loginUIState.value.copy(
            emailError = emailResult.status,
            passwordError = passwordResult.status
        )
        allValidationPassed.value = emailResult.status && passwordResult.status
    }

    private fun login() {
        loginProgress.value = true
        val email = loginUIState.value.email
        val password = loginUIState.value.password
        FirebaseAuth
            .getInstance()
            .signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                Log.d(TAG, "Inside_login_success")
                Log.d(TAG, "${it.isSuccessful}")
                if (it.isSuccessful) {
                    loginProgress.value = false
                    walletViewModel.loadWalletAmount() // Load the wallet amount after successful login
                    PostOfficeAppRouter.navigateTo(Screen.HomeScreen)
                }
            }
            .addOnFailureListener {
                Log.d(TAG, "Inside_login_failure")
                Log.d(TAG, "${it.localizedMessage}")
                loginProgress.value = false
            }
    }
}

