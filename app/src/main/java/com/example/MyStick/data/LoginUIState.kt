package com.example.loginflow.data

import androidx.compose.runtime.MutableState

data class LoginUIState (

    var email :String ="",
    var password :String="",


    var emailError :Boolean =false,
    var passwordError: Boolean =false

)


