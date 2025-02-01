package com.polstat.uas_ppk.ui.components

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.polstat.uas_ppk.data.UserPreferences
import com.polstat.uas_ppk.ui.theme.Quicksand
import kotlinx.coroutines.launch

@Composable
fun DrawerContent(
    navController: NavController,
    onMenuClick: (String) -> Unit,
    onLogout: () -> Unit,
    selectedItem: String = "Dashboard"
) {
    val context = LocalContext.current
    val userPreferences = remember { UserPreferences(context) }
    val userData by userPreferences.userData.collectAsState(initial = null)
    val role = userData?.role ?: "MAHASISWA" // Default role USER jika null

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .padding(16.dp)
    ) {
        Text(
            "Layanan Pengajuan\nServer STIS",
            fontWeight = FontWeight.Bold,
            fontFamily = Quicksand,
            fontSize = 22.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        DrawerItem(text = "Dashboard", icon = Icons.Filled.Home, isSelected = selectedItem == "Dashboard", onClick = {
            navController.navigate("home") {
                popUpTo("home") { inclusive = true }
            }
        })

        when(role) {
            "ADMINISTRATOR" -> {
                DrawerItem(text = "User Management", icon = Icons.Filled.ManageAccounts, isSelected = selectedItem == "User Management", onClick = {
                    navController.navigate("user_management") {
                        popUpTo("user_management") { inclusive = true }
                    }
                })
                DrawerItem(text = "Daftar Pengajuan", icon = Icons.Filled.Cloud, isSelected = selectedItem == "Daftar Pengajuan", onClick = {
                    navController.navigate("all_request_history") {
                        popUpTo("all_request_history") { inclusive = true }
                    }
                })
            }

            "MAHASISWA" -> {
                DrawerItem(text = "Ajukan Server", icon = Icons.Filled.Cloud, isSelected = selectedItem == "Server Request", onClick = {
                    navController.navigate("server_request") {
                        popUpTo("server_request") { inclusive = true }
                    }
                })
                DrawerItem(text = "Riwayat Pengajuan", icon = Icons.Default.History, isSelected = selectedItem == "Riwayat Pengajuan", onClick = {
                    navController.navigate("request_history") {
                        popUpTo("request_history") { inclusive = true }
                    }
                })
            }
        }

        DrawerItem(text = "Settings", icon = Icons.Filled.Settings, isSelected = selectedItem == "Settings", onClick = {
            navController.navigate("profile") {
                popUpTo("profile") { inclusive = true }
            }
        })

        DrawerItem(text = "About", icon = Icons.Filled.Info, isSelected = selectedItem == "About", onClick = {
            navController.navigate("about") {
                popUpTo("about") { inclusive = true }
            }
        })

        Spacer(modifier = Modifier.weight(1f))

        DrawerItem(text = "Logout", icon = Icons.AutoMirrored.Filled.ExitToApp, onClick = onLogout)
    }
}

@Composable
fun DrawerItem(text: String, icon: ImageVector, isSelected: Boolean = false, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .height(45.dp)
            .then(if (isSelected) Modifier.background(Color.Gray.copy(alpha = 0.3f)) else Modifier),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width(4.dp))
        Icon(icon, contentDescription = text, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Text(text, fontSize = 18.sp, fontFamily = Quicksand)
        Spacer(modifier = Modifier.width(4.dp))
    }
}

@Composable
fun ParentScreen(navController: NavController, selectedItem: String = "Dashboard") {
    val context = LocalContext.current
    val userPreferences = remember { UserPreferences(context) }
    val coroutineScope = rememberCoroutineScope()

    DrawerContent(
        navController = navController,
        onMenuClick = { menuName ->
            Toast.makeText(context, "Anda telah masuk ke $menuName", Toast.LENGTH_SHORT).show()
        },
        onLogout = {
            coroutineScope.launch {
                userPreferences.clearUserData()
                navController.navigate("login") {
                    popUpTo("home") { inclusive = true }
                }
            }
        },
        selectedItem = selectedItem
    )
}
