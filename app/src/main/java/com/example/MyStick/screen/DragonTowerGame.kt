package com.example.MyStick.screen

import android.content.Context
import android.media.MediaPlayer
import android.widget.Toast
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.loginflow.components.WalletHeader
import com.example.MyStick.data.rules.WalletViewModel
import com.example.MyStick.navigation.SystemBackButtonHandler
import com.example.pink.R
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BetAmountUIDragonTower(betAmount: String, onBetAmountChange: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .background(
                brush = Brush.horizontalGradient(listOf(Color(0xFF121212), Color(0xFF1E1E1E))),
                shape = RoundedCornerShape(12.dp)
            )
            .shadow(8.dp, RoundedCornerShape(12.dp))
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Bet Amount",
            fontSize = 18.sp,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(end = 8.dp)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = betAmount,
                onValueChange = onBetAmountChange,
                singleLine = true,
                modifier = Modifier
                    .background(Color(0xFF121212), RoundedCornerShape(12.dp))
                    .padding(8.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    cursorColor = Color.Blue,
                    focusedBorderColor = Color.White,
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.Gray,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                )
            )
            Box(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .size(30.dp)
                    .background(Color(0xFFE7C341), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "₹",
                    fontSize = 18.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun ProfitDisplay(earnings: Double) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(
                brush = Brush.horizontalGradient(listOf(Color(0xFF121212), Color(0xFF1E1E1E))),
                shape = RoundedCornerShape(12.dp)
            )
            .shadow(8.dp, RoundedCornerShape(12.dp))
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Earnings",
            fontSize = 18.sp,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "₹${String.format("%.2f", earnings)}",
            fontSize = 18.sp,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun DragonEggGame(navController: NavController, walletViewModel: WalletViewModel) {
    val walletAmount by walletViewModel.walletAmount.collectAsState(initial = 100.00) // Initial value as fallback
    var betAmount by remember { mutableStateOf("") }
    var currentRow by remember { mutableIntStateOf(8) }
    var earnings by remember { mutableDoubleStateOf(0.0) }
    var difficulty by remember { mutableStateOf("Easy") }
    val easyMultipliers = listOf(13.05, 9.79, 7.34, 5.51, 4.13, 3.10, 2.32, 1.74, 1.31)
    val mediumMultipliers = listOf(37.67, 25.11, 16.74, 11.156, 7.44, 4.96, 3.31, 2.21, 1.47)
    val hardMultipliers = listOf(90.15, 70.14, 60.16, 40.16, 31.36, 15.84, 7.84, 3.92, 1.96)
    var multipliers by remember { mutableStateOf(easyMultipliers) }
    var multiplier by remember { mutableDoubleStateOf(multipliers[currentRow]) }
    var gameInProgress by remember { mutableStateOf(false) }
    var gameOver by remember { mutableStateOf(false) }
    var grid by remember { mutableStateOf(List(9) { MutableList(4) { "" } }) }
    var revealedCells by remember { mutableStateOf(List(9) { MutableList(4) { false } }) }
    val difficulties = listOf("Easy", "Medium", "Hard")
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }

    fun updateMultipliers() {
        multipliers = when (difficulty) {
            "Medium" -> mediumMultipliers
            "Hard" -> hardMultipliers
            else -> easyMultipliers
        }
        multiplier = multipliers[currentRow]
    }

    fun initializeGrid() {
        val dragonsPerRow = when (difficulty) {
            "Medium" -> 2
            "Hard" -> 3
            else -> 1
        }
        grid = List(9) { rowIndex ->
            val dragonPositions = MutableList(dragonsPerRow) { Random.nextInt(4) }
            MutableList(4) { colIndex ->
                if (colIndex in dragonPositions) "dragon" else "egg"
            }
        }
    }

    fun revealAllCells() {
        for (i in 0..8) {
            for (j in 0..3) {
                revealedCells[i][j] = true
            }
        }
    }

    fun playSound(context: Context, resId: Int) {
        val mediaPlayer = MediaPlayer.create(context, resId)
        mediaPlayer.setOnCompletionListener {
            it.release()
        }
        mediaPlayer.start()
    }

    fun handleCellClick(row: Int, col: Int) {
        if (row == currentRow && !gameOver && gameInProgress) {
            revealedCells[row][col] = true
            if (grid[row][col] == "egg") {
                playSound(context, R.raw.coin)
                earnings = betAmount.toDouble() * multiplier
                currentRow--
                if (currentRow < 0) {
                    walletViewModel.addToWallet(earnings) // Add earnings to wallet
                    earnings = 0.0
                    gameOver = true
                    revealAllCells()
                } else {
                    multiplier = multipliers[currentRow]
                }
            } else {
                playSound(context, R.raw.explosion)
                gameOver = true
                revealAllCells()
            }
        }
    }

    fun resetGame() {
        currentRow = 8
        earnings = 0.0
        updateMultipliers()
        gameOver = false
        gameInProgress = false
        initializeGrid()
        revealedCells = List(9) { MutableList(4) { false } }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212))
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        WalletHeader(String.format("%.2f", walletAmount)) {
            showDialog = true
        }

        DragonEggGridUI(grid, revealedCells, currentRow, gameOver, ::handleCellClick)

        ProfitDisplay(earnings)

        BetAmountUIDragonTower(betAmount, onBetAmountChange = { betAmount = it })

        // Difficulty selection
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            var expanded by remember { mutableStateOf(false) }
            val interactionSource = remember { MutableInteractionSource() }

            Text(
                text = "Select Difficulty",
                fontSize = 18.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(end = 8.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF1E1E1E), RoundedCornerShape(12.dp))
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) { expanded = !expanded }
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = difficulty, color = Color.White, fontWeight = FontWeight.Bold)
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    difficulties.forEach { level ->
                        DropdownMenuItem(
                            text = { Text(level) },
                            onClick = {
                                difficulty = level
                                updateMultipliers()
                                expanded = false
                            }
                        )
                    }
                }
            }
        }

        Button(
            onClick = {
                if (gameInProgress) {
                    if (gameOver) {
                        resetGame()
                    } else {
                        walletViewModel.addToWallet(earnings)
                        resetGame()
                    }
                } else {
                    val betAmountValue = betAmount.toDoubleOrNull()
                    if (betAmountValue != null && betAmountValue <= walletAmount!! && betAmountValue > 0) {
                        walletViewModel.deductFromWallet(betAmountValue)
                        gameInProgress = true
                        initializeGrid()
                    } else {
                        Toast.makeText(
                            context,
                            "Please enter a valid bet amount",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(
                    brush = Brush.horizontalGradient(
                        listOf(
                            Color(0xFF4CAF50),
                            Color(0xFF388E3C)
                        )
                    )
                ),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
        ) {
            Text(
                text = if (gameInProgress) {
                    if (gameOver) "Bet Again" else "Cash Out"
                } else {
                    "Place Bet"
                },
                fontSize = 20.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }
    }

    SystemBackButtonHandler {
        navController.popBackStack()
    }
}

@Composable
fun DragonEggGridUI(
    grid: List<List<String>>,
    revealedCells: List<MutableList<Boolean>>,
    currentRow: Int,
    gameOver: Boolean,
    onCellClick: (Int, Int) -> Unit
) {
    Column {
        for (rowIndex in grid.indices) {
            Row {
                for (colIndex in grid[rowIndex].indices) {
                    val isRevealed = revealedCells[rowIndex][colIndex] || gameOver
                    val backgroundColor by animateColorAsState(
                        targetValue = when {
                            isRevealed -> Color(0xFF1A1A1A)
                            rowIndex == currentRow && !gameOver -> Color.Green
                            else -> Color.Gray
                        }
                    )
                    Box(
                        modifier = Modifier
                            .size(55.dp)
                            .padding(4.dp) // Adding space between squares
                            .background(backgroundColor, RoundedCornerShape(8.dp))
                            .clickable { onCellClick(rowIndex, colIndex) },
                        contentAlignment = Alignment.Center
                    ) {
                        if (isRevealed) {
                            if (grid[rowIndex][colIndex] == "egg") {
                                Image(
                                    painter = painterResource(id = R.drawable.egg),
                                    contentDescription = null,
                                    modifier = Modifier.size(50.dp)
                                )
                            } else {
                                Image(
                                    painter = painterResource(id = R.drawable.skull),
                                    contentDescription = null,
                                    modifier = Modifier.size(50.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
