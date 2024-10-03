package com.example.MyStick.app

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.rememberNavController
import com.example.loginflow.components.MysticGameAnimation
import com.example.MyStick.navigation.NavGraph
import com.example.MyStick.navigation.PostOfficeAppRouter
import com.example.MyStick.data.rules.WalletViewModel
import kotlinx.coroutines.delay
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun FlashScreen() {
    val showAnimation = remember { mutableStateOf(true) }
    val animationCount = remember { mutableStateOf(0) }
    val navController = rememberNavController()
    val walletViewModel: WalletViewModel = viewModel()

    LaunchedEffect(animationCount.value) {
        if (animationCount.value < 4) {
            delay(1000) // Duration of one animation cycle
            animationCount.value++
        } else {
            showAnimation.value = false
            PostOfficeAppRouter.initialize()
        }
    }

    if (showAnimation.value) {
        MysticGameAnimation()
    } else {
        val currentScreen by remember { PostOfficeAppRouter.currentScreen }
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.White
        ) {
            NavGraph(navController = navController, startDestination = currentScreen.route, walletViewModel = walletViewModel)
        }
    }
}
