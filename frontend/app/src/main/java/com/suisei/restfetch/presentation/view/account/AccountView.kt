package com.suisei.restfetch.presentation.view.account

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.suisei.restfetch.presentation.state.AccountViewState
import com.suisei.restfetch.presentation.view.Notify
import com.suisei.restfetch.presentation.view.theme.backgroundColor
import com.suisei.restfetch.presentation.viewmodel.AccountViewModel


@Composable
fun AccountScreen(navController: NavController) {
    val viewModel: AccountViewModel = hiltViewModel()
    val viewState = viewModel.viewState.collectAsState()

    Notify()

    Column(
        modifier = Modifier.background(backgroundColor())
    ) {
        when (val state = viewState.value) {
            AccountViewState.Login -> LoginScreen(navController)
            AccountViewState.SignUp -> SignUpScreen()
            AccountViewState.ForgotPassword -> ForgotPasswordScreen()
        }
    }
}

@Composable
fun AccountTemplate(content: @Composable () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterVertically)
    ) {
        content()
    }
}

@Composable
fun EmailInput(email: String, onEmailChange: (String) -> Unit, enabled: Boolean = true) {
    OutlinedTextField(
        value = email,
        onValueChange = onEmailChange,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        label = { Text(text = "Email") },
        textStyle = TextStyle.Default.copy(fontSize = 20.sp),
        modifier = Modifier.width(288.dp),
        singleLine = true,
        enabled = enabled
    )
}

@Composable
fun PasswordInput(
    label: String = "password",
    password: String,
    onPasswordChange: (String) -> Unit
) {
    OutlinedTextField(
        value = password,
        onValueChange = onPasswordChange,
        visualTransformation = PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        label = { Text(text = label) },
        textStyle = TextStyle.Default.copy(fontSize = 20.sp),
        modifier = Modifier.width(288.dp),
        singleLine = true
    )
}