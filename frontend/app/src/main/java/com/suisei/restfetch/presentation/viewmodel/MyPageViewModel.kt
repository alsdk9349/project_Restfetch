package com.suisei.restfetch.presentation.viewmodel

import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import com.suisei.restfetch.data.model.User
import com.suisei.restfetch.data.repository.MyDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(private val myDataRepository: MyDataRepository) : ViewModel() {
    val userData = myDataRepository.userData
}