package com.suisei.restfetch.presentation.view.main

import android.widget.Toast
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.suisei.restfetch.presentation.viewmodel.QRScannerViewModel

@Composable
fun PreviewCamera(onDismissRequest: () -> Unit) {
    val viewModel: QRScannerViewModel = hiltViewModel()
    val scanState = viewModel.qrScannerState.collectAsState()
    val scannedText = viewModel.scannedText.collectAsState()
    val nickname = viewModel.productNickname.collectAsState()

    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = onDismissRequest,
        dismissButton = {
            Button(onClick = onDismissRequest) {
                Text("취소")
            }
        },
        confirmButton = {
            Button(onClick = { /*TODO*/ }) {
                Text("등록")
            }
        },
        title = {
            Text("QR Scanner")
        },
        text = {
            Column {
                // 스캔된 텍스트 표시
                Text(text = "스캔된 텍스트: ${scannedText.value}")
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    CameraPreview()
                }

                DeviceNicknameInput(nickname = nickname.value, onNicknameChange = { viewModel.updateProductNickname(it) })

                if(scanState.value.qrScanState) {
                    Toast.makeText(context, "스캔 성공", Toast.LENGTH_SHORT).show()
                }
            }
        })
}

@Composable
fun DeviceNicknameInput(nickname: String, onNicknameChange: (String) -> Unit) {
    OutlinedTextField(
        value = nickname,
        onValueChange = onNicknameChange,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        label = { Text(text = "Nickname") },
        textStyle = TextStyle.Default.copy(fontSize = 20.sp),
        modifier = Modifier.width(288.dp),
        singleLine = true
    )
}

@Composable
fun CameraPreview() {
    val context = LocalContext.current
    val viewModel: QRScannerViewModel = hiltViewModel()

    val lifecycleOwner = LocalLifecycleOwner.current

    var previewView by remember {
        mutableStateOf(PreviewView(context))
    }

    LaunchedEffect(Unit) {
        viewModel.requestPermission()
        viewModel.startCamera(previewView, lifecycleOwner)
    }

    Box(modifier = Modifier.fillMaxWidth()) {
        Column {
            AndroidView(
                factory = { previewView },
                update = { }
            )
        }
    }
}