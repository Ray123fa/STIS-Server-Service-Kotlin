package com.polstat.uas_ppk.ui.screens

import android.widget.Toast
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
import com.polstat.uas_ppk.api.model.AllServerRequest
import com.polstat.uas_ppk.data.UserPreferences
import com.polstat.uas_ppk.ui.components.*
import com.polstat.uas_ppk.ui.theme.PurpleGrey40
import com.polstat.uas_ppk.viewmodel.ServerViewModel
import kotlinx.coroutines.launch

@Composable
fun AllServerRequestsHistoryScreen(navController: NavController, serverViewModel: ServerViewModel = viewModel()) {
    val context = LocalContext.current
    val userPreferences = remember { UserPreferences(context) }
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    val allServerRequests by serverViewModel.allServerRequests.collectAsState()
    val isLoading by serverViewModel.isLoading.collectAsState()

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            userPreferences.userData.collect { userData ->
                userData?.let { serverViewModel.fetchAllServerRequests(it.accessToken) }
            }
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        drawerContent = {
            ParentScreen(navController, selectedItem = "Daftar Pengajuan")
        },
        topBar = {
            TopBar(title = "Daftar Pengajuan", scaffoldState, navController, userPreferences)
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
            AllServerRequestTable(allServerRequests, isLoading, serverViewModel, navController)
        }
    }
}

@Composable
fun AllServerRequestTable(requestList: List<AllServerRequest>, isLoading: Boolean, serverViewModel: ServerViewModel, navController: NavController) {
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
                Text("Server Requests", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Spacer(modifier = Modifier.height(8.dp))
                Divider(color = Color.Gray.copy(alpha = 0.5f))
                Spacer(modifier = Modifier.height(8.dp))

                // **HEADER TABEL**
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    TableHeaderItem("No", Modifier.weight(0.2f))
                    TableHeaderItem("Owner Email", Modifier.weight(1f))
                    TableHeaderItem("Purpose", Modifier.weight(1f))
                    TableHeaderItem("Status", Modifier.weight(0.6f))
                    TableHeaderItem("Actions", Modifier.weight(0.8f))
                }
                Divider(color = Color.Gray)

                // **ISI TABEL**
                LazyColumn {
                    itemsIndexed(requestList) { index, request ->
                        AllServerRequestRow(index + 1, request, serverViewModel, navController)
                        Divider(color = Color.Gray)
                    }
                }
            }
        }
    }
}

@Composable
fun AllServerRequestRow(index: Int, request: AllServerRequest, serverViewModel: ServerViewModel, navController: NavController) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val userPreferences = remember { UserPreferences(context) }
    val userData by userPreferences.userData.collectAsState(initial = null)
    var token by remember { mutableStateOf(userData?.accessToken ?: "") }
    token = userData?.accessToken ?: ""

    var showApproveDialog by remember { mutableStateOf(false) }
    var showRejectDialog by remember { mutableStateOf(false) }
    var rejectReason by remember { mutableStateOf("") }

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
        TableCell(text = request.owner.email, Modifier.weight(1f))
        TableCell(text = request.purpose, Modifier.weight(1f))

        // **Status dengan Warna**
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

        // **TOMBOL ACTIONS**
        Column(
            modifier = Modifier.weight(0.8f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (request.status == "PENDING") {
                ActionButton(text = "Approve", bgColor = Color(0xFF4CAF50), onClick = { showApproveDialog = true })
                ActionButton(text = "Reject", bgColor = Color(0xFFD32F2F), onClick = { showRejectDialog = true })
            } else {
                Text("No Actions", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color.White)
            }
        }
    }

    if (showApproveDialog) {
        AlertDialog(
            onDismissRequest = { showApproveDialog = false },
            title = { Text("Konfirmasi Persetujuan") },
            text = { Text("Apakah Anda yakin ingin menyetujui permintaan server ini?") },
            confirmButton = {
                Button(
                    onClick = {
                        showApproveDialog = false
                        coroutineScope.launch {
                            serverViewModel.approveServerRequest(token, request.id) { success, message, username, password ->
                                Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                                if (success) {
                                    navController.popBackStack()
                                }
                            }
                        }
                    }
                ) {
                    Text("Ya, Setujui")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showApproveDialog = false }) {
                    Text("Batal")
                }
            }
        )
    }

    if (showRejectDialog) {
        AlertDialog(
            onDismissRequest = { showRejectDialog = false },
            title = { Text("Konfirmasi Penolakan") },
            text = {
                Column {
                    Text("Apakah Anda yakin ingin menolak permintaan server ini?")
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = rejectReason,
                        onValueChange = { rejectReason = it },
                        label = { Text("Alasan Penolakan") },
                        placeholder = { Text("Masukkan alasan penolakan") }
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (rejectReason.isNotEmpty()) {
                            showRejectDialog = false
                            coroutineScope.launch {
                                serverViewModel.rejectServerRequest(token, request.id, rejectReason) { success, message ->
                                    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                                    if (success) {
                                        navController.popBackStack()
                                    }
                                }
                            }
                        } else {
                            Toast.makeText(context, "Alasan penolakan harus diisi!", Toast.LENGTH_SHORT).show()
                        }
                    }
                ) {
                    Text("Ya, Tolak")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showRejectDialog = false }) {
                    Text("Batal")
                }
            }
        )
    }
}