package com.polstat.uas_ppk

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.polstat.uas_ppk.data.UserPreferences
import com.polstat.uas_ppk.ui.screens.HomeScreen
import com.polstat.uas_ppk.ui.screens.LoginScreen
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val userPreferences = UserPreferences(this)

        // Gunakan coroutine untuk cek token
        lifecycleScope.launch {
            val token = userPreferences.accessToken.first()
            val startDestination = if (token != null) "home" else "login"

            setContent {
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = startDestination) {
                    composable("login") { LoginScreen(navController) }
                    composable("home") { HomeScreen() }
                }
            }
        }
    }
}