package com.polstat.uas_ppk.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.polstat.uas_ppk.api.model.ServerRequest
import com.polstat.uas_ppk.data.UserPreferences
import com.polstat.uas_ppk.ui.components.*
import com.polstat.uas_ppk.ui.theme.PurpleGrey40
import com.polstat.uas_ppk.ui.theme.Quicksand
import com.polstat.uas_ppk.viewmodel.ServerViewModel
import kotlinx.coroutines.launch

@Composable
fun ServerRequestHistoryScreen(navController: NavController, serverViewModel: ServerViewModel = viewModel()) {
    val context = LocalContext.current
    val userPreferences = remember { UserPreferences(context) }
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    val serverRequests by serverViewModel.serverRequests.collectAsState()
    val isLoading by serverViewModel.isLoading.collectAsState()

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            userPreferences.userData.collect { userData ->
                userData?.let { serverViewModel.fetchMyServerRequests(it.accessToken) }
            }
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        drawerContent = {
            ParentScreen(navController, selectedItem = "Riwayat Pengajuan")
        },
        topBar = {
            TopBar(title = "Riwayat Pengajuan", scaffoldState, navController, userPreferences)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            ServerRequestTable(serverRequests, isLoading)
        }
    }
}

@Composable
fun ServerRequestTable(requests: List<ServerRequest>, isLoading: Boolean) {
    if (isLoading) {
        CircularProgressIndicator()
    } else {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp)),
            elevation = 4.dp,
            backgroundColor = PurpleGrey40
        ) {
            Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                Text("Daftar Pengajuan Server", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Spacer(modifier = Modifier.height(8.dp))
                Divider(color = Color.Gray.copy(alpha = 0.5f))
                Spacer(modifier = Modifier.height(8.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    TableHeaderItem("No", Modifier.weight(0.2f))
                    TableHeaderItem("Account", Modifier.weight(1f))
                    TableHeaderItem("Purpose", Modifier.weight(0.8f))
                    TableHeaderItem("Status", Modifier.weight(0.6f))
                }
                Divider(color = Color.Gray)

                LazyColumn {
                    itemsIndexed(requests) { index, request ->
                        ServerRequestRow(index + 1, request)
                        Divider(color = Color.Gray)
                    }
                }
            }
        }
    }
}

@Composable
fun ServerRequestRow(index: Int, request: ServerRequest) {
    val statusColor = when (request.status) {
        "APPROVED" -> Color.Green
        "REJECTED" -> Color.Red
        else -> Color.Yellow
    }
    val textStatusColor = when (request.status) {
        "APPROVED" -> Color.Black
        "REJECTED" -> Color.White
        else -> Color.Black
    }

    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        TableCell(text = index.toString(), Modifier.weight(0.2f))
        Column(modifier = Modifier.weight(1f)) { // 🔹 Kolom Account (Username & Password)
            Text(text = "Username: ${request.username}", fontSize = 14.sp, color = Color.White, fontFamily = Quicksand)
            Text(text = "Password: ${request.password}", fontSize = 14.sp, color = Color.White, fontFamily = Quicksand)
        }
        TableCell(text = request.purpose, Modifier.weight(0.8f))
        Text(
            text = request.status,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = textStatusColor,
            modifier = Modifier
                .weight(0.6f)
                .background(statusColor)
                .padding(4.dp)
        )
    }
}