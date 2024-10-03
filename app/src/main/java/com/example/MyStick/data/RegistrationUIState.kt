package com.example.loginflow.data

import androidx.compose.foundation.pager.PagerState

data class RegistrationUIState(
    var firstName : String ="",
    var lastName:String ="",
    var email :String ="",
    var password :String="",

    var firstNameError :Boolean =false,
    var lastNameError: Boolean =false,
    var emailError :Boolean =false,
    var passwordError: Boolean =false
)


