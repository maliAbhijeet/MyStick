package com.example.loginflow.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.MyStick.RewardedAdMyStick
import kotlinx.coroutines.delay

@Composable
fun NormalTextComponent(value:String){
    Text(
        text = value,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 40.dp),
        style = TextStyle(
            fontSize = 24.sp,
            fontWeight = FontWeight.Normal,
            fontStyle = FontStyle.Normal
        )
        , color = Color(0xFF101617),
        textAlign = TextAlign.Center
    )

}
@Composable
fun HeadingTextComponent(value:String){
    Text(
        text = value,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(),
        style = TextStyle(
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Normal
        )
        , color = Color(0xFF101617),
        textAlign = TextAlign.Center
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTextFieldComponent(labelValue: String, painterResource: Painter,
                         onTextSelected: (String) -> Unit,
                        errorStatus:Boolean= false ) {
    val textValue = remember {
        mutableStateOf("")
    }
    OutlinedTextField(
        singleLine = true,
        maxLines = 1,
        modifier = Modifier.fillMaxWidth(),
        label = { Text(text = labelValue) },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color(0xFF92A3FD),
            focusedLabelColor = Color(0xFF92A3FD),
            cursorColor = Color(0xFF92A3FD),
        ),
        value = textValue.value,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        onValueChange = {
            textValue.value = it
            onTextSelected(it)
        },
        leadingIcon = {
            Icon(painter = painterResource, contentDescription ="" )
        }, isError = !errorStatus

    )
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordTextFieldComponent(labelValue: String, painterResource: Painter,onTextSelected: (String) -> Unit,errorStatus:Boolean =false) {
    val localFocusManager = LocalFocusManager.current
    val password = remember { mutableStateOf("") }
    val passwordVisible = remember { mutableStateOf(false) }

    OutlinedTextField(
        singleLine = true,
        maxLines = 1,
        modifier = Modifier.fillMaxWidth(),
        label = { Text(text = labelValue) },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color(0xFF92A3FD),
            focusedLabelColor = Color(0xFF92A3FD),
            cursorColor = Color(0xFF92A3FD),
        ),
        value = password.value,
        keyboardOptions = KeyboardOptions(keyboardType = if (passwordVisible.value) KeyboardType.Text else KeyboardType.Password, imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions{
                                         localFocusManager.clearFocus()
        },
        onValueChange = {
            password.value = it
            onTextSelected(it)
        },
        leadingIcon = {
            Icon(painter = painterResource, contentDescription = "")
        },
        trailingIcon = {
            val iconImage = if (passwordVisible.value) {
                Icons.Filled.Visibility
            } else {
                Icons.Filled.VisibilityOff
            }
            val description = if (passwordVisible.value) {
                "Hide password"
            } else {
                "Show password"
            }
            IconButton(onClick = { passwordVisible.value = !passwordVisible.value }) {
                Icon(imageVector = iconImage, contentDescription = description)
            }
        },
        visualTransformation = if (passwordVisible.value) VisualTransformation.None else PasswordVisualTransformation(),
        isError = !errorStatus
    )
}
@Composable
fun CheckBoxComponent(value: String){
    Row(modifier = Modifier
        .fillMaxWidth()
        .heightIn(56.dp)
        .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,

    ) {
        val checkedState = remember {
            mutableStateOf(false)
        }
        Checkbox(checked = checkedState.value, onCheckedChange = {checkedState.value != checkedState.value
        })
        NormalTextComponent( value)


    }
}
@Composable
fun ButtonComponent(value: String, onButtonClicked :()->Unit, isEnabled :Boolean =false){
    Button(onClick = {
                onButtonClicked.invoke()
    },
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(48.dp),
        contentPadding = PaddingValues(),
        colors = ButtonDefaults.buttonColors(Color.Transparent),
        enabled = isEnabled

                ) {
        Box(modifier = Modifier
            .fillMaxWidth()
            .heightIn(48.dp)
            .background(
                brush = Brush.horizontalGradient(listOf(Color(0xFF92A3FD), Color(0xFF9DCEFF))),
                shape = RoundedCornerShape(50.dp)
            ),
            contentAlignment = Alignment.Center
        ){
            Text(text = value,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold)
        }

    }
    
}
@Composable
fun DividerTextComponent(){
    Row (modifier =Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ){

        Divider(modifier = Modifier
            .fillMaxWidth()
            .weight(1f),
            thickness = 1.dp
        )
        Text(modifier = Modifier.padding(8.dp),
            text = "Or",
            fontSize = 18.sp,
            color = Color.Black)
        Divider(modifier = Modifier
            .fillMaxWidth()
            .weight(1f),
            thickness = 1.dp
        )

    }
}
@Composable
fun ClickableLoginTextComponent(tryingToLogin:Boolean=true, onTextSelected:(String)->Unit){

    val initialText =if(tryingToLogin)"Already have an account" else "Don't have an account yet"
    val loginText =if(tryingToLogin) "Login" else "Register"

    val annotatedString = buildAnnotatedString {
        append(initialText)
        withStyle(style = SpanStyle(Color(0xFF92A3FD))){
            pushStringAnnotation(tag = loginText, annotation = loginText)
            append(loginText)
        }
    }
    ClickableText(text = annotatedString,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 40.dp),
        style = TextStyle(
            fontSize = 21.sp,
            fontWeight = FontWeight.Normal,
            fontStyle = FontStyle.Normal,
            textAlign = TextAlign.Center
        ),
       onClick = { offset ->

        annotatedString.getStringAnnotations(offset,offset)
            .firstOrNull()?.also { span ->
                if (span.item == loginText) {
                    onTextSelected(span.item)

                }
            }

    })

}
@Composable
fun UnderLinedTextComponent(value:String){
    Text(
        text = value,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 40.dp),
        style = TextStyle(
            fontSize = 24.sp,
            fontWeight = FontWeight.Normal,
            fontStyle = FontStyle.Normal
        )
        , color = Color(0xFF101617),
        textAlign = TextAlign.Center,
        textDecoration = TextDecoration.Underline
    )

}
@Composable
fun MysticGameAnimation() {

    val isAnimationRunning = remember { mutableStateOf(true) }

    val transition = rememberInfiniteTransition()
    val alpha by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Box(
        modifier = Modifier.fillMaxSize()
            .background(color = Color.Black),
        contentAlignment = Alignment.Center
    ) {
        AnimatedText(text = "MyStick", modifier = Modifier.alpha(alpha))
    }

    // Track when animation completes its cycle
    LaunchedEffect(Unit) {
        while (isAnimationRunning.value) {
            delay(2000) // Duration for two animation cycles
            isAnimationRunning.value = false // Stop animation after two cycles
        }
    }
}

@Composable
fun AnimatedText(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        fontSize = 48.sp,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
        modifier = modifier,
        color = Color.White
    )
}
@Composable
fun WalletDialog(walletAmount: Double, onWithdraw: () -> Unit, onWatchAd: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Wallet Information") },
        text = {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    "Your wallet amount is ₹${String.format("%.2f", walletAmount)}"
                )
                Spacer(modifier = Modifier.height(24.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = onWithdraw,
                        modifier = Modifier.weight(1f).padding(end = 2.dp)
                    ) {
                        Text("Withdraw")
                    }
                    RewardedAdMyStick(
                        onWatchAd = onWatchAd,
                        modifier = Modifier.weight(1f).padding(start = 2.dp)
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close")
            }
        },
        modifier = Modifier.fillMaxHeight(0.5f)
            .fillMaxWidth(0.99f)
    )
}
@Composable
fun WalletHeader(walletAmount: String?, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .width(200.dp)
            .padding(16.dp)
            .background(
                brush = Brush.horizontalGradient(listOf(Color(0xFF3E3E3E), Color(0xFF1E1E1E))),
                shape = RoundedCornerShape(8.dp)
            )
            .shadow(4.dp, RoundedCornerShape(8.dp))
            .clickable { onClick() },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (walletAmount != null) {
            Text(
                text = "₹$walletAmount",
                fontSize = 20.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(8.dp)
            )
        } else {
            CircularProgressIndicator(
                color = Color.Blue,
                modifier = Modifier.padding(8.dp).size(20.dp)
            )
        }
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(Color(0xFF1E88E5), RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "₹",
                fontSize = 20.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }
    }
}