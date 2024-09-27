package com.suisei.restfetch.presentation.view.account

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.suisei.restfetch.presentation.intent.AccountIntent
import com.suisei.restfetch.presentation.intent.VerifyEmailIntent
import com.suisei.restfetch.presentation.state.VerifyEmailState
import com.suisei.restfetch.presentation.view.theme.BasicButton
import com.suisei.restfetch.presentation.view.theme.buttonTransparentTheme
import com.suisei.restfetch.presentation.viewmodel.AccountViewModel

@Composable
fun SignUpScreen() {
    val viewModel: AccountViewModel = viewModel()

    val verifyState = viewModel.verifyEmailState.collectAsState()

    var email by remember { mutableStateOf("") }
    var code by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordConfirm by remember { mutableStateOf("") }
    var nickname by remember { mutableStateOf("") }

    BackHandler {
        viewModel.sendViewIntent(AccountIntent.LoadLogin)
    }

    AccountTemplate {
        EmailInput(
            email = email,
            onEmailChange = { email = it },
            enabled = verifyState.value != VerifyEmailState.VerifyComplete
        )
        Row(
            modifier = Modifier
                .width(288.dp)
                .height(IntrinsicSize.Min),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            AuthCodeInput(
                code = code, onCodeChange = {
                    code = it
                    if (code == "") viewModel.sendVerifyEmailIntent(VerifyEmailIntent.LoadRequestResendButton)
                    else if (verifyState.value != VerifyEmailState.WaitRequestCode) viewModel.sendVerifyEmailIntent(
                        VerifyEmailIntent.LoadRequestVerifyButton
                    )
                },
                enabled = verifyState.value != VerifyEmailState.VerifyComplete
            )

            when (val state = verifyState.value) {
                VerifyEmailState.WaitRequestCode -> VerifyNavigationButton(
                    "인증 번호 요청",
                    { viewModel.sendVerifyEmailIntent(VerifyEmailIntent.LoadRequestResendButton) })

                VerifyEmailState.WaitEnterCode -> VerifyNavigationButton("재전송", { })
                VerifyEmailState.WaitRequestVerify -> VerifyNavigationButton(
                    "인증 요청",
                    { viewModel.sendVerifyEmailIntent(VerifyEmailIntent.LoadVerifyComplete) })

                VerifyEmailState.VerifyComplete -> VerifyNavigationButton(
                    "인증 완료",
                    { },
                    enabled = false
                )
            }
        }

        PasswordInput(password = password, onPasswordChange = { password = it })
        PasswordInput(label = "Password Confirm", password = passwordConfirm, onPasswordChange = { passwordConfirm = it })
        NicknameInput(nickname = nickname, onNicknameChange = { nickname = it })

        CreateButton()
    }
}

@Composable
fun AuthCodeInput(code: String, onCodeChange: (String) -> Unit, enabled: Boolean) {
    OutlinedTextField(
        value = code,
        onValueChange = onCodeChange,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        placeholder = { Text(text = "Code") },
        textStyle = TextStyle.Default.copy(fontSize = 20.sp),
        modifier = Modifier.width(144.dp),
        singleLine = true,
        enabled = enabled
    )
}

@Composable
fun VerifyNavigationButton(navText: String, onClick: () -> Unit, enabled: Boolean = true) {
    BasicButton(
        onClick = onClick,
        modifier = Modifier.width(72.dp),
        enabled = enabled
    ) {
        Text(navText, fontSize = 14.sp, color = Color.DarkGray, textAlign = TextAlign.Center)
    }
}

@Composable
fun NicknameInput(nickname: String, onNicknameChange: (String) -> Unit) {
    OutlinedTextField(
        value = nickname,
        onValueChange = onNicknameChange,
        visualTransformation = PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        label = { Text(text = "Nickname") },
        textStyle = TextStyle.Default.copy(fontSize = 20.sp),
        modifier = Modifier.width(288.dp),
        singleLine = true
    )
}

@Composable
fun CreateButton() {
    BasicButton(onClick = { /*TODO*/ }, contentPadding = PaddingValues(12.dp)) {
        Text("계정 생성", fontSize = 22.sp, color = Color.DarkGray, textAlign = TextAlign.Center)
    }
}