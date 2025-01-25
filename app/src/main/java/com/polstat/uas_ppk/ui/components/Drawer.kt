package com.polstat.uas_ppk.ui.components

import android.widget.Toast
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
    onLogout: () -> Unit
) {
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

        DrawerItem(text = "Dashboard", icon = Icons.Filled.Home, onClick = {
            navController.navigate("home") {
                popUpTo("home") { inclusive = true }
            }
        })
        DrawerItem(text = "Settings", icon = Icons.Filled.Settings, onClick = {
            onMenuClick("Settings")
        })
        DrawerItem(text = "Help", icon = Icons.AutoMirrored.Filled.Help, onClick = {
            onMenuClick("Help")
        })

        Spacer(modifier = Modifier.weight(1f))

        DrawerItem(text = "Logout", icon = Icons.AutoMirrored.Filled.ExitToApp, onClick = onLogout)
    }
}

@Composable
fun DrawerItem(text: String, icon: ImageVector, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = text, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Text(text, fontSize = 18.sp, fontFamily = Quicksand)
    }
}

@Composable
fun ParentScreen(navController: NavController) {
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
        }
    )
}
