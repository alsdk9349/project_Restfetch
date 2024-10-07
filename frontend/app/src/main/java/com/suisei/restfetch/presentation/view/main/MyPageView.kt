package com.suisei.restfetch.presentation.view.main

import android.widget.Toast
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.suisei.restfetch.R
import com.suisei.restfetch.presentation.intent.QRScannerIntent
import com.suisei.restfetch.presentation.view.theme.menuButtonBorderColor
import com.suisei.restfetch.presentation.view.theme.menuButtonColor
import com.suisei.restfetch.presentation.viewmodel.MainViewModel
import com.suisei.restfetch.presentation.viewmodel.MyPageViewModel
import com.suisei.restfetch.presentation.viewmodel.QRScannerViewModel

@Composable
fun MyPageScreen() {
    val viewModel: MyPageViewModel = hiltViewModel()
    val scrollState = rememberScrollState()
    val userData = viewModel.userData.collectAsState()

    val qrScannerViewModel: QRScannerViewModel = hiltViewModel()
    val qrScanState = qrScannerViewModel.qrScannerState.collectAsState()

    val context = LocalContext.current


    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .weight(1.2f)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp, alignment = Alignment.Bottom),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                imageVector = Icons.Rounded.AccountCircle,
                contentDescription = "account",
                modifier = Modifier.size(96.dp)
            )
            Text(userData.value.nickname, fontSize = 28.sp)
            Text("사용 횟수 : " + "10", fontSize = 28.sp)
        }


        Column(
            modifier = Modifier
                .weight(2f)
                .padding(24.dp, 0.dp)
                .verticalScroll(scrollState)
        ) {
            MenuRow {
                MenuButton(menuImageId = R.drawable.logo, description = "내 기기", onClick = { })
                MenuButton(menuImageId = R.drawable.logo, description = "QR 기기 등록", onClick = { qrScannerViewModel.sendIntent(
                    QRScannerIntent.ShowQRRegistration) })
            }
            MenuRow {
                MenuButton(menuImageId = R.drawable.logo, description = "페치 등록", onClick = { })
                MenuButton(menuImageId = R.drawable.logo, description = "옵저버 등록", onClick = { })
            }
            MenuRow {
                MenuButton(menuImageId = R.drawable.logo, description = "기기 변경", onClick = { })
                MenuButton(menuImageId = R.drawable.logo, description = "기기 제거", onClick = { })
            }
        }

        if(qrScanState.value.scanning) {
            PreviewCamera {
                qrScannerViewModel.sendIntent(QRScannerIntent.HideQRRegistration)
                qrScannerViewModel.stopCamera()
            }
        }

        BottomAppBar()
    }
}

@Composable
fun MenuRow(content: @Composable () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp, 0.dp, 0.dp, 24.dp),
        horizontalArrangement = Arrangement.spacedBy(24.dp, alignment = Alignment.CenterHorizontally),
        verticalAlignment = Alignment.Top
    ) {
        content()
    }
}

@Composable
fun MenuButton(menuImageId: Int, description: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = menuButtonColor(),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .border(
                width = 2.dp,
                color = menuButtonBorderColor(),
                shape = RoundedCornerShape(8.dp)
            )
            .width(160.dp),
        contentPadding = PaddingValues(8.dp, 16.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(painter = painterResource(id = menuImageId), contentDescription = description)
            Text(description, color = Color.Black, fontSize = 24.sp)
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0XFFFFFDF8)
@Composable
fun PreviewMyPageScreen() {
    MyPageScreen()
}