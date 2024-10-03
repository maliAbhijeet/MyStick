package com.example.loginflow.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.loginflow.components.ButtonComponent
import com.example.loginflow.components.ClickableLoginTextComponent
import com.example.loginflow.components.DividerTextComponent
import com.example.loginflow.components.HeadingTextComponent
import com.example.loginflow.components.MyTextFieldComponent
import com.example.loginflow.components.NormalTextComponent
import com.example.loginflow.components.PasswordTextFieldComponent
import com.example.loginflow.data.SignUpUIEvent
import com.example.loginflow.data.SignUpViewModel
import com.example.MyStick.navigation.PostOfficeAppRouter
import com.example.MyStick.navigation.Screen
import com.example.pink.R

@Composable
fun SignUpScreen(signUpViewModel: SignUpViewModel = viewModel(), navController: NavController) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Surface(
            color = Color.White,
            modifier = Modifier
                .fillMaxSize()
                .padding(28.dp)
                .background(color = Color.White)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                NormalTextComponent(value = "Hello there ")
                HeadingTextComponent(value = "Create an Account")
                Spacer(modifier = Modifier.height(20.dp))

                MyTextFieldComponent(
                    labelValue = "First name", painterResource(id = R.drawable.profile),
                    onTextSelected = {
                        signUpViewModel.onEvent(SignUpUIEvent.FirstNameChanged(it))
                    }, signUpViewModel.registrationUIState.value.firstNameError
                )
                MyTextFieldComponent(
                    labelValue = "Last name", painterResource(id = R.drawable.profile),
                    onTextSelected = {
                        signUpViewModel.onEvent(SignUpUIEvent.LastNameChanged(it))
                    }, signUpViewModel.registrationUIState.value.lastNameError
                )
                MyTextFieldComponent(
                    labelValue = "Email", painterResource(id = R.drawable.mail),
                    onTextSelected = {
                        signUpViewModel.onEvent(SignUpUIEvent.EmailChanged(it))
                    }, signUpViewModel.registrationUIState.value.emailError
                )
                PasswordTextFieldComponent(
                    labelValue = "Password", painterResource(id = R.drawable.lock),
                    onTextSelected = {
                        signUpViewModel.onEvent(SignUpUIEvent.PasswordChanged(it))
                    }, signUpViewModel.registrationUIState.value.passwordError
                )
                Spacer(modifier = Modifier.height(40.dp))

                ButtonComponent(
                    value = "Register", onButtonClicked = {
                        signUpViewModel.onEvent(SignUpUIEvent.RegisterButtonClicked)
                    },
                    isEnabled = signUpViewModel.allValidationPassed.value
                )
                Spacer(modifier = Modifier.height(20.dp))
                DividerTextComponent()
                ClickableLoginTextComponent(tryingToLogin = true, onTextSelected = {
                    PostOfficeAppRouter.navigateTo(Screen.LoginScreen)
                })
            }
        }
        if (signUpViewModel.signUpInProgress.value) {
            CircularProgressIndicator()
        }
    }
}


