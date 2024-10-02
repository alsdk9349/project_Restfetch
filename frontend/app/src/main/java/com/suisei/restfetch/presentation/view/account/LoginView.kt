package com.suisei.restfetch.presentation.view.account

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.suisei.restfetch.R
import com.suisei.restfetch.presentation.intent.AccountIntent
import com.suisei.restfetch.presentation.view.theme.buttonTransparentTheme
import com.suisei.restfetch.presentation.viewmodel.AccountViewModel

@Composable
fun LoginScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val context = LocalContext.current
    val viewModel: AccountViewModel = viewModel()

    AccountTemplate {
        LogoImage()
        Spacer(modifier = Modifier.height(24.dp))

        EmailInput(email = email, onEmailChange = { email = it })

        Column {
            PasswordInput(password = password, onPasswordChange = { password = it })
            NavigationContainer {
                LoadViewButton(text = "Sign Up", accountIntent = AccountIntent.LoadSignUp)
                LoadViewButton(
                    text = "Forgot Password?",
                    accountIntent = AccountIntent.LoadForgotPassword
                )
            }
        }

        EmailLoginButton {
            viewModel.login(email, password) { accessToken, refreshToken ->
                val sharedPreferences = context.getSharedPreferences("login_prefs", Context.MODE_PRIVATE)
                with(sharedPreferences.edit()) {
                    putString("accessToken", accessToken)
                    putString("refreshToken", refreshToken)
                    apply()
                }

                navController.navigate("main_screen") {
                    popUpTo("login_screen") {
                        inclusive = true
                    }
                }
            }
        }
        GoogleLoginButton()
    }
}

@Composable
fun LogoImage() {
    Image(
        painter = painterResource(id = R.drawable.logo),
        contentDescription = "Logo",
        modifier = Modifier.size(128.dp)
    )
}

@Composable
fun NavigationContainer(content: @Composable () -> Unit) {
    Row(
        modifier = Modifier
            .width(288.dp)
            .height(28.dp)
            .defaultMinSize(minHeight = 1.dp)
            .padding(8.dp, 0.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        content()
    }
}

@Composable
fun LoadViewButton(text: String, accountIntent: AccountIntent) {
    val viewModel: AccountViewModel = viewModel()
    Button(
        onClick = { viewModel.sendViewIntent(accountIntent) },
        colors = buttonTransparentTheme(),
        modifier = Modifier.defaultMinSize(minHeight = 1.dp),
        contentPadding = PaddingValues()
    ) {
        Text(text, color = Color.Black, fontSize = 14.sp)
    }
}

@Composable
fun EmailLoginButton(onClick: () -> Unit) {

    OutlinedButton(onClick = onClick) {
        Text(
            text = "Login",
            fontSize = 24.sp,
            color = Color.DarkGray,
            modifier = Modifier.padding(4.dp)
        )
    }
}

@Composable
fun GoogleLoginButton() {
    val viewModel: AccountViewModel = viewModel()
    val context = LocalContext.current
    Button(
        onClick = { viewModel.requestGoogleAuth(context) },
        modifier = Modifier
            .wrapContentHeight()
            .defaultMinSize(minWidth = 1.dp, minHeight = 1.dp),
        contentPadding = PaddingValues(),
        border = null
    ) {
        Image(
            painter = painterResource(id = R.drawable.google_ctn_logo),
            contentDescription = ""
        )
    }
}