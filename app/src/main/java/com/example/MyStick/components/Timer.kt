package com.example.MyStick.components


import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.delay
import java.util.concurrent.TimeUnit
@Composable
fun CountdownTimer(totalTime: Long = TimeUnit.HOURS.toMillis(3)) {
    var currentTime by remember { mutableStateOf(totalTime) }
    var hours by remember { mutableStateOf(0L) }
    var minutes by remember { mutableStateOf(0L) }
    var seconds by remember { mutableStateOf(0L) }

    LaunchedEffect(key1 = currentTime) {
        if (currentTime > 0) {
            delay(1000L)
            currentTime -= 1000L
            hours = TimeUnit.MILLISECONDS.toHours(currentTime)
            minutes = TimeUnit.MILLISECONDS.toMinutes(currentTime) % 60
            seconds = TimeUnit.MILLISECONDS.toSeconds(currentTime) % 60
        }
    }

    Text(
        text = String.format("%02d:%02d:%02d", hours, minutes, seconds),
        style = MaterialTheme.typography.headlineLarge
    )
}

@Preview(showBackground = true)
@Composable
fun CountdownTimerPreview() {
    CountdownTimer()
}
