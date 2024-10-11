package com.suisei.restfetch.presentation.view

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import com.suisei.restfetch.presentation.viewmodel.NotifyViewModel

@Composable
fun Notify() {
    val notifyViewModel: NotifyViewModel = hiltViewModel()
    val notifyState = notifyViewModel.notifyState.collectAsState()
    val notifyMessage = notifyViewModel.notifyMessage.collectAsState()

    if (notifyState.value) {
        AlertDialog(
            onDismissRequest = {  },
            text = { Text(notifyMessage.value) },
            confirmButton = {
                Button(onClick = { notifyViewModel.closeNotify() }) {
                    Text("확인")
                }
            })
    }
}
