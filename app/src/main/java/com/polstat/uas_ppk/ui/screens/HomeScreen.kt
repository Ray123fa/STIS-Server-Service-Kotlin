package com.polstat.uas_ppk.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.polstat.uas_ppk.data.UserPreferences
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun HomeScreen() {
    val context = LocalContext.current
    val userPreferences = remember { UserPreferences(context) }
    var userName by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    // Ambil nama user dari DataStore saat HomeScreen dibuka
    LaunchedEffect(Unit) {
        coroutineScope.launch {
            userPreferences.userData.collectLatest { userData ->
                if (userData != null) {
                    userName = userData.name // Hanya mengambil nama user
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Selamat Datang, $userName!",
            fontSize = 24.sp,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Anda telah berhasil login.",
            fontSize = 16.sp,
            textAlign = TextAlign.Center
        )
    }
}
