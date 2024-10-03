package com.example.MyStick.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.MyStick.screen.LoginScreen
import com.example.loginflow.screen.SignUpScreen
import com.example.MyStick.screen.HomeScreen
import GameScreen
import com.example.MyStick.data.LoginViewModel
import com.example.loginflow.data.SignUpViewModel
import com.example.MyStick.screen.DragonEggGame
import com.example.MyStick.data.rules.WalletViewModel

@Composable
fun NavGraph(navController: NavHostController, startDestination: String, walletViewModel: WalletViewModel) {
    NavHost(navController = navController, startDestination = startDestination) {
        composable(Screen.SignUpScreen.route) {
            SignUpScreen(signUpViewModel = SignUpViewModel(), navController = navController)
        }
        composable(Screen.LoginScreen.route) {
            LoginScreen(loginViewModel = LoginViewModel(), navController = navController)
        }
        composable(Screen.HomeScreen.route) {
            HomeScreen(navController = navController, walletViewModel = walletViewModel, signUpViewModel = SignUpViewModel())
        }
        composable(Screen.MineGameScreen.route) {
            GameScreen(navController=navController,walletViewModel = walletViewModel)
        }
        composable(Screen.DragonGameScreen.route) {
            DragonEggGame(navController = navController, walletViewModel = walletViewModel)
        }
        composable(Screen.LimboGameScreen.route) {
            GameScreen(navController=navController,walletViewModel = walletViewModel)
        }
        composable(Screen.SpinGameScreen.route) {
            GameScreen(navController=navController,walletViewModel = walletViewModel)
        }
        composable(Screen.SwingGameScreen.route) {
            GameScreen(navController=navController,walletViewModel = walletViewModel)
        }
    }
}
