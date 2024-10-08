package com.suisei.restfetch.presentation.view.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.suisei.restfetch.R
import com.suisei.restfetch.data.model.Report
import com.suisei.restfetch.presentation.intent.MainIntent
import com.suisei.restfetch.presentation.state.HomeViewState
import com.suisei.restfetch.presentation.view.theme.backgroundColor
import com.suisei.restfetch.presentation.view.theme.buttonTransparentTheme
import com.suisei.restfetch.presentation.view.theme.dropdownBackgroundColor
import com.suisei.restfetch.presentation.view.theme.fetchButtonColor
import com.suisei.restfetch.presentation.view.theme.menuButtonBorderColor
import com.suisei.restfetch.presentation.viewmodel.MainViewModel

@Composable
fun HomeScreen(homeViewState: HomeViewState) {
    val viewModel: MainViewModel = hiltViewModel()
    val reportList by viewModel.reportList.collectAsState()
    val crtLocation = viewModel.crtLocation.collectAsState()
    //val state = viewModel.state.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopAppBar()

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f), contentAlignment = Alignment.Center
        ) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.Top),
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxHeight()
            ) {
                items(reportList.size) { index ->
                    if (crtLocation.value.location == "전체" || crtLocation.value.observerId == reportList[index].observerId) {
                        FallenObject(
                            reportList[index],
                            viewModel.stringToImageBitmap(reportList[index].picture),
                            imageObject = "TEST"
                        )
                    }
                }
            }


            if (homeViewState.selectState.isSelected) {
                FetchButton(
                    Modifier
                        .align(Alignment.BottomCenter)
                )
            }
        }

        BottomAppBar()
    }
}

@Composable
fun TopAppBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        MenuButton()
        AreaDropDown()
        NotificationButton()
    }
}

@Composable
fun MenuButton() {
    Button(
        onClick = { /*TODO*/ },
        colors = buttonTransparentTheme(),
        modifier = Modifier.padding(0.dp),
        contentPadding = PaddingValues(0.dp)
    ) {
        Image(
            imageVector = Icons.Default.Menu,
            contentDescription = "Menu",
            modifier = Modifier
                .size(42.dp)
                .padding(0.dp)
        )
    }
}

@Composable
fun AreaDropDown() {
    var showDialog by remember { mutableStateOf(false) }
    var location by remember { mutableStateOf("안방") }

    var list = listOf("안방", "큰방")

    Row(modifier = Modifier.padding(12.dp, 0.dp)) {
        Button(
            onClick = { showDialog = true },
            colors = buttonTransparentTheme(),
            shape = RectangleShape,
            contentPadding = PaddingValues(0.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(0.75f)
                    .background(color = if (!showDialog) Color.Transparent else dropdownBackgroundColor())
                    .border(
                        1.dp,
                        color = if (!showDialog) Color.Transparent else menuButtonBorderColor()
                    ),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.width(24.dp))
                Text(
                    text = location,
                    color = Color.Black,
                    fontSize = 24.sp,
                    modifier = Modifier.padding(8.dp)
                )
                Image(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "DropDown",
                    modifier = Modifier.padding(8.dp, 0.dp),
                    contentScale = ContentScale.FillHeight
                )
            }

            DropdownMenu(
                containerColor = backgroundColor(),
                modifier = Modifier.fillMaxWidth(0.54f),
                offset = DpOffset(0.dp, 6.dp),
                expanded = showDialog,
                onDismissRequest = { showDialog = false }) {
                list.forEachIndexed { index, s ->
                    DropdownMenuItem(text = {
                        Text(
                            text = s,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(32.dp),
                            fontSize = 24.sp
                        )
                    },
                        onClick = {
                            showDialog = false
                            location = s
                            list = listOf("안방", "큰방", "작은방")
                        })
                }
            }
        }
    }
}

@Composable
fun NotificationButton() {
    var showDialog by remember { mutableStateOf(false) }
    Button(
        onClick = { showDialog = true },
        colors = buttonTransparentTheme(),
        contentPadding = PaddingValues(0.dp)
    ) {
        Image(
            imageVector = Icons.Outlined.Notifications,
            contentDescription = "Notification",
            modifier = Modifier
                .size(42.dp)
                .padding(0.dp)
        )
        NotificationList(showDialog = showDialog, dismissList = { showDialog = false })
    }
}

@Composable
fun NotificationList(showDialog: Boolean, dismissList: () -> Unit) {
    var list = listOf("안방", "큰방")
    DropdownMenu(
        containerColor = Color.Transparent,
        modifier = Modifier.fillMaxWidth(0.55f),
        offset = DpOffset(0.dp, 6.dp),
        expanded = showDialog,
        onDismissRequest = dismissList
    ) {
        list.forEachIndexed { index, s ->
            DropdownMenuItem(text = {
                Text(
                    text = s,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = 24.sp
                )
            },
                onClick = {
                    dismissList()
                    list = listOf("안방", "큰방", "작은방")
                })
        }
    }
}

@Composable
fun FallenObject(item: Report, picture: ImageBitmap, imageObject: String) {
    val viewModel: MainViewModel = hiltViewModel()
    Button(
        onClick = { /*viewModel.sendIntent(MainIntent.ShowFetchButton)*/ viewModel.selectReport(
            item
        )
        },
        colors = buttonTransparentTheme(),
        shape = RectangleShape,
        elevation = ButtonDefaults.elevatedButtonElevation(pressedElevation = 8.dp),
        modifier = Modifier.padding(4.dp),
        contentPadding = PaddingValues(8.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                bitmap = picture,
                contentDescription = "Fallen"
            )
            Text(imageObject, fontSize = 28.sp, color = Color.Black)
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0XFFFFFDF8)
@Composable
fun PreviewHomeView() {
    HomeScreen(HomeViewState())
}

@Composable
fun FetchButton(modifier: Modifier) {
    val viewModel: MainViewModel = hiltViewModel()
    Button(
        onClick = { viewModel.sendIntent(MainIntent.HideFetchButton) },
        colors = fetchButtonColor(),
        modifier = modifier
            .padding(64.dp),
        contentPadding = PaddingValues(24.dp)
    ) {
        Text(text = "갤럭시 워치 Fetch", fontSize = 28.sp)
    }
}

@Preview
@Composable
fun PreviewTest() {
    Text("TEST")
}