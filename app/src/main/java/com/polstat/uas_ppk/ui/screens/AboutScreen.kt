package com.polstat.uas_ppk.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
import com.polstat.uas_ppk.ui.components.ParentScreen
import com.polstat.uas_ppk.ui.components.TopBar
import com.polstat.uas_ppk.ui.theme.PurpleGrey40
import com.polstat.uas_ppk.ui.theme.Quicksand

@Composable
fun AboutScreen(navController: NavController) {
    val context = LocalContext.current
    val userPreferences = remember { UserPreferences(context) }
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        drawerContent = {
            ParentScreen(navController, selectedItem = "About")
        },
        topBar = {
            TopBar(title = "About", scaffoldState, navController, userPreferences)
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
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .padding(8.dp),
                backgroundColor = PurpleGrey40,
                elevation = 8.dp
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Nama: Muhammad Rayhan Faridh",
                        fontFamily = Quicksand,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color.White
                    )

                    Text(
                        text = "NIM: 222212766",
                        fontFamily = Quicksand,
                        fontSize = 16.sp,
                        color = Color.White
                    )

                    Text(
                        text = "Kelas: 3SI2",
                        fontFamily = Quicksand,
                        fontSize = 16.sp,
                        color = Color.White
                    )

                    Text(
                        text = "No. Absen: 26",
                        fontFamily = Quicksand,
                        fontSize = 16.sp,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Git STIS Repository",
                        fontFamily = Quicksand,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Cyan,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .clickable {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://git.stis.ac.id/rayhan-ppk/penyediaan-server-kotlin.git"))
                                context.startActivity(intent)
                            }
                            .padding(8.dp)
                    )
                }
            }
        }
    }
}
