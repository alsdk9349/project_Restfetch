package com.suisei.restfetch.presentation.view.main

import android.widget.Toast
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.suisei.restfetch.presentation.view.theme.BasicButton
import com.suisei.restfetch.presentation.view.theme.buttonTransparentTheme
import com.suisei.restfetch.presentation.viewmodel.MainViewModel
import com.suisei.restfetch.presentation.viewmodel.MyPageViewModel
import com.suisei.restfetch.presentation.viewmodel.QRScannerViewModel

@Composable
fun PreviewCamera(onDismissRequest: () -> Unit) {
    val viewModel: QRScannerViewModel = hiltViewModel()
    val myPageViewModel: MyPageViewModel = hiltViewModel()
    val productType = viewModel.productType.collectAsState()
    val serialNumber = viewModel.serialNumber.collectAsState()
    val nickname = viewModel.productNickname.collectAsState()
    val selectFetcherState = viewModel.selectFetcherState.collectAsState()
    val parentFetcher = viewModel.parentFetcher.collectAsState()

    AlertDialog(
        onDismissRequest = onDismissRequest,
        dismissButton = {
            Button(onClick = onDismissRequest) {
                Text("취소")
            }
        },
        confirmButton = {
            Button(onClick = {
                if(serialNumber.value != "" && nickname.value != "") {
                    if (productType.value == 1) {
                        myPageViewModel.addFetcher(
                            serialNumber.value,
                            nickname.value
                        )
                    } else if(productType.value == 2) {
                        if(parentFetcher.value.fetchSerialNumber != "") {
                            myPageViewModel.addObserver(
                                serialNumber.value,
                                parentFetcher.value.fetchSerialNumber,
                                nickname.value
                            )
                        }
                    }

                    onDismissRequest()
                }

            }) {
                Text("등록")
            }
        },
        title = {
            Text("QR Scanner")
        },
        text = {
            Column {
                Text(text = "시리얼 넘버: ${serialNumber.value}")
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    CameraPreview()
                }

                DeviceNicknameInput(
                    nickname = nickname.value,
                    onNicknameChange = { viewModel.updateProductNickname(it) })


                if (productType.value == 2) {
                    Spacer(modifier = Modifier.height(12.dp))
                    SelectFetcher()
                    if (selectFetcherState.value) {
                        MyFetcherList {
                            viewModel.updateSelectFetcherState(false)
                        }
                    }
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
fun SelectFetcher() {
    val viewModel: QRScannerViewModel = hiltViewModel()
    val parentFetcher = viewModel.parentFetcher.collectAsState()
    OutlinedButton(
        onClick = { viewModel.updateSelectFetcherState(true) },
        contentPadding = OutlinedTextFieldDefaults.contentPadding(),
        shape = OutlinedTextFieldDefaults.shape,
        colors = buttonTransparentTheme(),
        modifier = Modifier
            .width(288.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = parentFetcher.value.fetchName.ifEmpty { "Fetcher 선택" },
                fontSize = 20.sp,
                textAlign = TextAlign.Left,
                fontWeight = FontWeight.Normal
            )
        }

    }
}

@Composable
fun MyFetcherList(onDismissRequest: () -> Unit) {
    val viewModel: MainViewModel = hiltViewModel()
    val qrScannerViewModel: QRScannerViewModel = hiltViewModel()

    val fetcherList = viewModel.fetcherList.collectAsState()

    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            Button(onClick = onDismissRequest) {
                Text("확인")
            }
        },
        title = {
            Text("로봇 목록")
        },
        text = {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.Top)
            ) {
                items(fetcherList.value.size) { item ->
                    FetcherItem(
                        fetcherList.value[item].fetchName,
                        fetcherList.value[item].fetchSerialNumber
                    ) {
                        qrScannerViewModel.setParentFetcher(fetcherList.value[item])
                        onDismissRequest()
                    }
                }
            }
        })
}

@Composable
fun FetcherItem(nickname: String, serialNumber: String, onClick: () -> Unit) {
    BasicButton(onClick = onClick) {
        Column {
            Text(nickname)
            Text(serialNumber)
        }
    }
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

@Preview
@Composable
fun PreviewSelectFetcher() {
    Box(modifier = Modifier.background(Color.White)) {
        Column {
            DeviceNicknameInput(nickname = "TEXT") {}
            Spacer(modifier = Modifier.height(12.dp))
            SelectFetcher()
        }

    }

}