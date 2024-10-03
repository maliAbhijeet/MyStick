package com.example.MyStick.screen

import androidx.compose.foundation.Image
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavController
import com.example.MyStick.data.rules.WalletViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.loginflow.components.WalletDialog
import com.example.loginflow.components.WalletHeader
import com.example.loginflow.data.SignUpViewModel
import com.example.MyStick.Game
import com.example.MyStick.navigation.Screen
import com.example.pink.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController, walletViewModel: WalletViewModel, signUpViewModel: SignUpViewModel) {
    val walletAmount by walletViewModel.walletAmount.collectAsState()
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        WalletHeader(walletAmount = walletAmount.let { String.format("%.2f", it) }) {
                            showDialog = true
                        }
                    }
                },
                actions = {
                    IconButton(onClick = { signUpViewModel.logOut() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.profile), // Replace with your user logo resource
                            contentDescription = "User Logo"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFF0F0F0),
                    titleContentColor = Color.Black,
                    actionIconContentColor = Color.Black
                )
            )
        },
        content = { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFF0F0F0)) // Light grey background color
                    .padding(padding)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Spacer(modifier = Modifier.height(16.dp))
                    GamesScreen(onGameClick = { gameName ->
                        when (gameName) {
                            "Mine" -> navController.navigate(Screen.MineGameScreen.route)
                            "Limbo" -> navController.navigate(Screen.LimboGameScreen.route)
                            "Spin" -> navController.navigate(Screen.SpinGameScreen.route)
                            "Dragon" -> navController.navigate(Screen.DragonGameScreen.route)
                            "Swing" -> navController.navigate(Screen.SwingGameScreen.route)
                            else -> navController.navigate(Screen.HomeScreen.route) // Default route if game name doesn't match
                        }
                    })
                }
            }
            if (showDialog) {
                WalletDialog(
                    walletAmount = walletAmount,
                    onWithdraw = { /* Handle withdraw */ },
                    onWatchAd = {
                        walletViewModel.addToWallet(5.00) // Add bonus amount
                    },
                    onDismiss = { showDialog = false }
                )
            }
        }
    )
}

@Composable
fun GamesScreen(onGameClick: (String) -> Unit) {
    val games = listOf(
        Game(R.drawable.mine_game, "Mine"),
        Game(R.drawable.limbo, "Limbo"),
        Game(R.drawable.spin, "Spin"),
        Game(R.drawable.dragon_tower_game, "Dragon"),
        Game(R.drawable.dice, "Dice"),
    )

    LazyVerticalGrid(
        GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(games.size) { index ->
            GameItem(game = games[index], onClick = { onGameClick(games[index].gameName) })
        }
    }
}

@Composable
fun GameItem(game: Game, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.elevatedCardElevation(8.dp),
        modifier = Modifier
            .fillMaxSize()
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(game.gameImage),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(16.dp))
            )
            Text(
                text = game.gameName,
                color = Color.Black,
                style = TextStyle(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}


