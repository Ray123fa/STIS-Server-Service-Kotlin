package com.polstat.uas_ppk.ui.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.polstat.uas_ppk.data.UserPreferences
import com.polstat.uas_ppk.viewmodel.AuthViewModel
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(navController: NavController, authViewModel: AuthViewModel = viewModel()) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val userPreferences = remember { UserPreferences(context) }
    val coroutineScope = rememberCoroutineScope()
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .imePadding(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Judul
        Text(
            text = "Layanan Pengajuan Akses Server STIS",
            fontSize = 32.sp,
            textAlign = TextAlign.Left,
            modifier = Modifier.fillMaxWidth(),
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(52.dp))

        Text(
            text = "Selamat Datang",
            textAlign = TextAlign.Left,
            modifier = Modifier.fillMaxWidth(),
            fontSize = 30.sp,
            fontWeight = FontWeight.SemiBold
        )

        Text(
            text = "Lengkapi isian berikut.",
            fontSize = 16.sp,
            modifier = Modifier.fillMaxWidth(),
            color = Color.Gray,
            textAlign = TextAlign.Left
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Input Email
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Input Password dengan Toggle Visibility
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = image, contentDescription = "Toggle Password Visibility")
                }
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = { keyboardController?.hide() } // Tutup keyboard saat tekan Done
            )
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Tombol Login dengan Toast
        Button(
            onClick = {
                authViewModel.login(email, password) { result, token ->
                    Log.d("LoginResult", result)
                    Toast.makeText(context, result, Toast.LENGTH_LONG).show()

                    if (token != null) {
                        coroutineScope.launch {
                            userPreferences.saveAccessToken(token)
                        }
                        navController.navigate("home") {
                            popUpTo("login") { inclusive = true }
                        }
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5A49F8)),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(text = "Masuk", color = Color.White, fontSize = 18.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Link Registrasi dengan Toast
        Row {
            Text("Belum punya akun?", fontSize = 16.sp, color = Color.Gray)
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "Daftar di sini!",
                fontSize = 16.sp,
                color = Color(0xFF3B82F6),
                modifier = Modifier.clickable {
                    Toast.makeText(context, "Navigasi ke halaman registrasi", Toast.LENGTH_SHORT).show()
                }
            )
        }
    }
}
