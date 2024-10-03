package com.example.MyStick.screen

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
import com.example.loginflow.components.UnderLinedTextComponent
import com.example.loginflow.data.LoginUIEvent
import com.example.MyStick.data.LoginViewModel
import com.example.MyStick.navigation.PostOfficeAppRouter
import com.example.MyStick.navigation.Screen
import com.example.MyStick.navigation.SystemBackButtonHandler
import com.example.pink.R

@Composable
fun LoginScreen(loginViewModel: LoginViewModel = viewModel(), navController: NavController){

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
        Surface(

            color = Color.White,
            modifier = Modifier
                .fillMaxSize()
                .padding(28.dp)
                .background(color = Color.White)

        ) {
            Column( modifier = Modifier
                .fillMaxSize()) {
                NormalTextComponent(value ="Login" )
                HeadingTextComponent(value = "Welcome Back")
                MyTextFieldComponent(labelValue = "Email", painterResource(id = R.drawable.mail),
                    onTextSelected = {
                        loginViewModel.onEvent(LoginUIEvent.EmailChanged(it))


                    }, errorStatus = loginViewModel.loginUIState.value.emailError)
                PasswordTextFieldComponent(labelValue = "Password", painterResource(id = R.drawable.lock), onTextSelected = {
                    loginViewModel.onEvent(LoginUIEvent.PasswordChanged(it))


                }, errorStatus = loginViewModel.loginUIState.value.passwordError)
                Spacer(modifier =Modifier.height(40.dp))

                UnderLinedTextComponent(value = "Forgot your password")
                Spacer(modifier =Modifier.height(20.dp))
                ButtonComponent(value ="Login", onButtonClicked = {
                        loginViewModel.onEvent(LoginUIEvent.LoginButtonClicked)

                },
                    isEnabled =  loginViewModel.allValidationPassed.value)

                Spacer(modifier =Modifier.height(20.dp))
                DividerTextComponent()

                ClickableLoginTextComponent( tryingToLogin = false, onTextSelected = {
                    PostOfficeAppRouter.navigateTo(Screen.SignUpScreen)

                })



            }


        }
        if (loginViewModel.loginProgress.value){
            CircularProgressIndicator()

        }


    }


    SystemBackButtonHandler {
        PostOfficeAppRouter.navigateTo(Screen.SignUpScreen)
        navController.popBackStack()
    }
}
