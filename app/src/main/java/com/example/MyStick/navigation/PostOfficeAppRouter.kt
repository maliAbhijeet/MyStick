package com.example.MyStick.navigation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.google.firebase.auth.FirebaseAuth

sealed class Screen(val route: String) {
    object SignUpScreen : Screen("sign_up_screen")
    object LoginScreen : Screen("login_screen")
    object HomeScreen : Screen("home_screen")
    object MineGameScreen : Screen("mine_game_screen")
    object LimboGameScreen : Screen("limbo_game_screen")
    object SpinGameScreen : Screen("spin_game_screen")
    object DragonGameScreen : Screen("dragon_game_screen")
    object SwingGameScreen : Screen("swing_game_screen")
}

object PostOfficeAppRouter {
    var currentScreen: MutableState<Screen> = mutableStateOf(Screen.SignUpScreen)

    fun navigateTo(destination: Screen) {
        currentScreen.value = destination
    }

    fun initialize() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            currentScreen.value = Screen.HomeScreen
        } else {
            currentScreen.value = Screen.LoginScreen
        }
    }
}
