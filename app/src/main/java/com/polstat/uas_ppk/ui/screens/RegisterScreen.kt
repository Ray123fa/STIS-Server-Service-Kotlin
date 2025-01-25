package com.polstat.uas_ppk.ui.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import com.polstat.uas_ppk.ui.theme.Quicksand
import com.polstat.uas_ppk.viewmodel.AuthViewModel

@Composable
fun RegisterScreen(navController: NavController, authViewModel: AuthViewModel = viewModel()) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    val context = LocalContext.current
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
            fontFamily = Quicksand,
            fontSize = 32.sp,
            textAlign = TextAlign.Left,
            modifier = Modifier.fillMaxWidth(),
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(52.dp))

        Text(
            text = "Buat Akun Baru",
            fontFamily = Quicksand,
            textAlign = TextAlign.Left,
            modifier = Modifier.fillMaxWidth(),
            fontSize = 30.sp,
            fontWeight = FontWeight.SemiBold
        )

        Text(
            text = "Lengkapi isian berikut.",
            fontFamily = Quicksand,
            fontSize = 16.sp,
            modifier = Modifier.fillMaxWidth(),
            color = Color.Gray,
            textAlign = TextAlign.Left
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Input Nama
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nama", fontFamily = Quicksand) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Input Email
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email", fontFamily = Quicksand) },
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
            label = { Text("Password", fontFamily = Quicksand) },
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
                imeAction = ImeAction.Next
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Input Konfirmasi Password
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Konfirmasi Password", fontFamily = Quicksand) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (confirmPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
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

        // Tombol Register
        Button(
            onClick = {
                if (password != confirmPassword) {
                    Toast.makeText(context, "Password tidak cocok!", Toast.LENGTH_LONG).show()
                } else {
                    authViewModel.register(name, email, password) { result, success ->
                        Log.d("RegisterResult", result)
                        Toast.makeText(context, result, Toast.LENGTH_LONG).show()

                        if (success) {
                            navController.navigate("login") {
                                popUpTo("register") { inclusive = true } // Tidak bisa kembali ke register
                            }
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
            Text(text = "Daftar", color = Color.White, fontSize = 18.sp, fontFamily = Quicksand)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row {
            Text("Sudah punya akun?", fontSize = 16.sp, color = Color.Gray, fontFamily = Quicksand)
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "Masuk di sini!",
                fontFamily = Quicksand,
                fontSize = 16.sp,
                color = Color(0xFF3B82F6),
                modifier = Modifier.clickable {
                    navController.navigate("login") {
                        popUpTo("register") { inclusive = true } // Tidak bisa kembali ke login
                    }
                }
            )
        }
    }
}
