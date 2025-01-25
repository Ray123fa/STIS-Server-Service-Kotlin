package com.polstat.uas_ppk.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.polstat.uas_ppk.data.UserPreferences
import com.polstat.uas_ppk.ui.components.*
import com.polstat.uas_ppk.ui.theme.*
import com.polstat.uas_ppk.utils.Helper

@Composable
fun HomeScreen(navController: NavController) {
    val context = LocalContext.current
    val userPreferences = remember { UserPreferences(context) }
    val scaffoldState = rememberScaffoldState()

    val userData by userPreferences.userData.collectAsState(initial = null)

    val greeting = Helper.getGreeting()
    val quote = Helper.getQuotes()
    val userName = userData?.name ?: "Pengguna" // Jika userName kosong, tampilkan "Pengguna"

    Scaffold(
        scaffoldState = scaffoldState,
        drawerContent = {
            ParentScreen(navController, selectedItem = "Dashboard")
        },
        topBar = {
            TopBar(title = "Dashboard", scaffoldState, navController, userPreferences)
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
            // Card untuk menampilkan greeting dan quote
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp)),
                elevation = 4.dp,
                backgroundColor = PurpleGrey40 // Warna latar belakang sesuai desain
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "$greeting, $userName",
                        fontFamily = Quicksand,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 20.sp,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Divider(color = Color.Gray.copy(alpha = 0.5f))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = quote,
                        fontFamily = Quicksand,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Justify,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }
            }
        }
    }
}
