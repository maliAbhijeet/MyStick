package com.example.MyStick.navigation

import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcherOwner
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalContext

private val LocalBackPressedDispatcher = staticCompositionLocalOf<OnBackPressedDispatcherOwner?> { null }

private class ComposableBackNavigationHandler(enabled: Boolean) : OnBackPressedCallback(enabled) {
    lateinit var onBackPressed: () -> Unit

    override fun handleOnBackPressed() {
        onBackPressed()
    }
}

@Composable
internal fun ComposableHandler(enabled: Boolean = true, onBackPressed: () -> Unit) {
    val dispatcher = (LocalBackPressedDispatcher.current ?: return).onBackPressedDispatcher
    val handler = remember {
        ComposableBackNavigationHandler(enabled)
    }
    DisposableEffect(dispatcher) {
        dispatcher.addCallback(handler)

        onDispose { handler.remove() }
    }
    LaunchedEffect(enabled) {
        handler.isEnabled = enabled
        handler.onBackPressed = onBackPressed
    }
}

@Composable
internal fun SystemBackButtonHandler(onBackPressed: () -> Unit) {
    val lifecycleOwner = LocalContext.current as? OnBackPressedDispatcherOwner
    CompositionLocalProvider(LocalBackPressedDispatcher provides lifecycleOwner) {
        ComposableHandler {
            onBackPressed()
        }
    }
}
