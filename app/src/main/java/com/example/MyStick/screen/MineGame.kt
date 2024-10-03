import android.media.MediaPlayer
import android.widget.Toast
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.loginflow.components.WalletHeader
import com.example.MyStick.data.rules.WalletViewModel
import com.example.MyStick.navigation.SystemBackButtonHandler
import com.example.pink.R
import kotlinx.coroutines.delay
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BetAmountUIMine(betAmount: String, onBetAmountChange: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .background(
                brush = Brush.horizontalGradient(listOf(Color(0xFF121212), Color(0xFF1E1E1E))),
                shape = RoundedCornerShape(8.dp)
            )
            .shadow(4.dp, RoundedCornerShape(8.dp))
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Bet Amount",
            fontSize = 16.sp,
            color = Color.White,
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
                    .fillMaxWidth()
                    .background(Color(0xFF121212), RoundedCornerShape(8.dp))
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
                    .size(24.dp)
                    .background(Color(0xFFE7C341), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "₹",
                    fontSize = 16.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun ProfitDisplay(earnings: Double, multiplier: Double) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp)
            .background(
                brush = Brush.horizontalGradient(listOf(Color(0xFF121212), Color(0xFF1E1E1E))),
                shape = RoundedCornerShape(8.dp)
            )
            .shadow(4.dp, RoundedCornerShape(8.dp))
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Total profit (${String.format("%.2f", multiplier)}x)",
            fontSize = 16.sp,
            color = Color.White
        )
        Text(
            text = "₹${String.format("%.2f", earnings)}",
            fontSize = 16.sp,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(navController: NavController, walletViewModel: WalletViewModel = viewModel()) {

    var gridState by remember { mutableStateOf(List(5) { List(5) { "#6e6c66" } }) }
    var gameStarted by remember { mutableStateOf(false) }
    var redSquares by remember { mutableStateOf(emptyList<Pair<Int, Int>>()) }
    var revealedSquares by remember { mutableStateOf(emptySet<Pair<Int, Int>>()) }
    var gameOver by remember { mutableStateOf(false) }
    var numberOfRedSquares by remember { mutableStateOf("1") }
    var betAmount by remember { mutableStateOf("") }
    var earnings by remember { mutableStateOf(0.0) }
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }

    val walletAmount by walletViewModel.walletAmount.collectAsState()

    fun updateGridState() {
        gridState = List(5) { rowIndex ->
            List(5) { colIndex ->
                when {
                    redSquares.contains(Pair(rowIndex, colIndex)) -> if (revealedSquares.contains(Pair(rowIndex, colIndex))) "#FF0000" else "#6e6c66"
                    revealedSquares.contains(Pair(rowIndex, colIndex)) -> "#00FF00"
                    else -> "#6e6c66"
                }
            }
        }
    }

    fun calculateEarnings(): Double {
        val redSquareCount = numberOfRedSquares.toIntOrNull() ?: 1
        val bet = betAmount.toDoubleOrNull() ?: 0.0
        val profitPerGreenSquare = 0.02 + (redSquareCount - 1) * 0.02
        val greenSquareEarnings = revealedSquares.size * profitPerGreenSquare * bet
        return bet + greenSquareEarnings
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212))
            . verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            WalletHeader(String.format("%.2f", walletAmount)){
                showDialog = true
            }
        }

        MinesweeperGrid(gridState, gameStarted && !gameOver, revealedSquares, redSquares) { x, y ->
            if (redSquares.any { it.first == x && it.second == y }) {
                gameOver = true
                revealedSquares = revealedSquares + redSquares + (0..4).flatMap { row -> (0..4).map { col -> Pair(row, col) } }
                Toast.makeText(context, "Game Over", Toast.LENGTH_SHORT).show()
                // Do not subtract the bet amount here
            } else {
                revealedSquares = revealedSquares + Pair(x, y)
                earnings = calculateEarnings()
            }
            updateGridState()
        }

        ProfitDisplay(earnings, 0.02 + ((numberOfRedSquares.toIntOrNull() ?: 1) - 1) * 0.02)

        BetAmountUIMine(betAmount, onBetAmountChange = { betAmount = it })

        OutlinedTextField(
            value = numberOfRedSquares,
            onValueChange = { numberOfRedSquares = it },
            label = { Text("Number of Red Squares") },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF121212), RoundedCornerShape(8.dp))
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

        Button(
            onClick = {
                if (!gameStarted || gameOver) {
                    val number = numberOfRedSquares.toIntOrNull()
                    val bet = betAmount.toDoubleOrNull()
                    if (number == null || number < 1 || number > 24) {
                        Toast.makeText(context, "Please enter a number between 1 and 24", Toast.LENGTH_SHORT).show()
                    } else if (bet == null || bet <= 0 || bet > walletAmount) {
                        Toast.makeText(context, "Please enter a valid bet amount", Toast.LENGTH_SHORT).show()
                    } else {
                        val newRedSquares = mutableSetOf<Pair<Int, Int>>()
                        while (newRedSquares.size < number) {
                            newRedSquares.add(Pair(Random.nextInt(5), Random.nextInt(5)))
                        }
                        redSquares = newRedSquares.toList()
                        gameStarted = true
                        gameOver = false
                        revealedSquares = emptySet()
                        earnings = 0.0
                        updateGridState()
                        // Subtract bet amount here when the game starts
                        walletViewModel.updateWalletAmount(walletAmount - bet)
                    }
                } else {
                    walletViewModel.updateWalletAmount((walletAmount ?: 0.0) + earnings)
                    earnings = 0.0
                    gameStarted = false
                    revealedSquares = emptySet()
                    redSquares = emptyList()
                    updateGridState()
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
        ) {
            Text(if (gameStarted && !gameOver) "Cash Out" else "Bet")
        }

        if (gameOver) {
            Text("Game Over", fontWeight = FontWeight.Bold, fontSize = 24.sp, color = Color.Red)
        }
    }
    SystemBackButtonHandler {
        navController.popBackStack()
    }
}

@Composable
fun MinesweeperGrid(
    grid: List<List<String>>,
    clickable: Boolean,
    revealedSquares: Set<Pair<Int, Int>>,
    redSquares: List<Pair<Int, Int>>,
    onTileClick: (Int, Int) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        for (y in grid.indices) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                for (x in grid[y].indices) {
                    MinesweeperTile(
                        color = Color(android.graphics.Color.parseColor(grid[y][x])),
                        onClick = { onTileClick(y, x) },
                        clickable = clickable,
                        isGreen = revealedSquares.contains(Pair(y, x)) && !redSquares.contains(Pair(y, x)),
                        isRed = revealedSquares.contains(Pair(y, x)) && redSquares.contains(Pair(y, x))
                    )
                }
            }
        }
    }
}

@Composable
fun MinesweeperTile(color: Color, onClick: () -> Unit, clickable: Boolean, isGreen: Boolean, isRed: Boolean) {
    val context = LocalContext.current
    var clicked by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (clicked) 1.2f else 1f,
        animationSpec = tween(durationMillis = 300)
    )

    LaunchedEffect(clicked) {
        if (clicked) {
            if (isGreen) {
                MediaPlayer.create(context, R.raw.coin).start()
            } else if (isRed) {
                MediaPlayer.create(context, R.raw.explosion).start()
            }
            delay(300)
            clicked = false
        }
    }

    Box(
        modifier = Modifier
            .size(60.dp)
            .background(
                color = if (isGreen || isRed) Color.Transparent else color,
                shape = RoundedCornerShape(4.dp)
            )
            .clickable(
                onClick = {
                    if (clickable) {
                        clicked = true
                        onClick()
                    }
                },
                enabled = clickable
            ),
        contentAlignment = Alignment.Center
    ) {
        if (isGreen) {
            Image(
                painter = painterResource(id = R.drawable.diamond_without_bg),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .scale(scale)
            )
        } else if (isRed) {
            Image(
                painter = painterResource(id = R.drawable.bomb_without_bg), // replace with your red square image resource
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .scale(scale)
            )
        }
    }
}