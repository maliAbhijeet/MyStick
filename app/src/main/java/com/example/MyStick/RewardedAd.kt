package com.example.MyStick

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.OnUserEarnedRewardListener
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import kotlinx.coroutines.launch

@Composable
fun RewardedAdMyStick(onWatchAd: () -> Unit, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var rewardedAd by remember { mutableStateOf<RewardedAd?>(null) }
    val btnText = remember { mutableStateOf("Loading") }
    val btnEnable = remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()

    fun loadRewarded(context: Context) {
        RewardedAd.load(
            context,
            "ca-app-pub-3940256099942544/5224354917",
            AdRequest.Builder().build(),
            object : RewardedAdLoadCallback() {
                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    super.onAdFailedToLoad(loadAdError)
                    rewardedAd = null
                    btnText.value = "Failed"
                    btnEnable.value = false
                }

                override fun onAdLoaded(ad: RewardedAd) {
                    super.onAdLoaded(ad)
                    rewardedAd = ad
                    btnText.value = "Watch ad"
                    btnEnable.value = true
                }
            }
        )
    }

    fun showRewardedAd(context: Context) {
        val activity = context.findActivity()
        if (activity != null) {
            rewardedAd?.let { ad ->
                ad.show(activity, OnUserEarnedRewardListener {
                    Toast.makeText(context, "Rewarded", Toast.LENGTH_SHORT).show()
                    loadRewarded(context)
                    onWatchAd()
                    rewardedAd = null
                    btnText.value = "Loading"
                    btnEnable.value = false
                })
            } ?: run {
                Toast.makeText(context, "Ad not loaded", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(context, "Activity not found", Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(Unit) {
        loadRewarded(context)
    }

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            enabled = btnEnable.value,
            onClick = {
                coroutineScope.launch {
                    showRewardedAd(context)
                }
            }
        ) {
            Text(text = btnText.value)
        }
    }
}

// Extension function to get the Activity from a Context
fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}

