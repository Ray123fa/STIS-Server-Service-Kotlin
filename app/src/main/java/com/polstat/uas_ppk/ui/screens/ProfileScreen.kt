package com.polstat.uas_ppk.ui.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.polstat.uas_ppk.data.UserPreferences
import com.polstat.uas_ppk.ui.components.*
import com.polstat.uas_ppk.ui.theme.*
import com.polstat.uas_ppk.viewmodel.AuthViewModel
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(navController: NavController) {
    val context = LocalContext.current
    val userPreferences = remember { UserPreferences(context) }
    val scaffoldState = rememberScaffoldState()

    val userData by userPreferences.userData.collectAsState(initial = null)

    var token by remember { mutableStateOf(userData?.accessToken ?: "") }
    var userName by remember { mutableStateOf(userData?.name ?: "Pengguna") }
    var userEmail by remember { mutableStateOf(userData?.email ?: "Email") }

    LaunchedEffect(userData) {
        token = userData?.accessToken ?: ""
        userName = userData?.name ?: "Pengguna"
        userEmail = userData?.email ?: "Email"
    }

    Scaffold(
        scaffoldState = scaffoldState,
        drawerContent = {
            ParentScreen(navController, selectedItem = "Settings")
        },
        topBar = {
            TopBar(title = "Profile", scaffoldState, navController, userPreferences)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()), // Tambahkan scroll di sini
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            // Profile Photo Section
            ProfilePhotoSection(userName)

            Spacer(modifier = Modifier.height(16.dp))

            // Profile Information Section
            ProfileInformationSection(navController = navController, userPreferences = userPreferences, token = token, userName = userName, userEmail = userEmail, onEmailChange = { userEmail = it })

            Spacer(modifier = Modifier.height(16.dp))

            // Update Password Section
            UpdatePasswordSection(token = token)
        }
    }
}

// Profile Photo Section
@Composable
fun ProfilePhotoSection(userName: String) {
    val avatarUrl = "https://ui-avatars.com/api/?background=7F9CF5&color=ffffff&name=${userName.replace(" ", "+")}"

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        backgroundColor = PurpleGrey40
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                SectionTitle("Profile Photo")
                SectionDescription("Your profile picture is generated based on your name.")
            }

            Spacer(modifier = Modifier.height(8.dp))
            Divider(color = Color.Gray.copy(alpha = 0.5f))
            Spacer(modifier = Modifier.height(8.dp))

            // Circular Profile Image from UI Avatars
            Image(
                painter = rememberAsyncImagePainter(avatarUrl),
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
            )
        }
    }
}

// Profile Information Section
@Composable
fun ProfileInformationSection(
    navController: NavController,
    authViewModel: AuthViewModel = viewModel(),
    userPreferences: UserPreferences,
    token: String,
    userName: String,
    userEmail: String,
    onEmailChange: (String) -> Unit)
{
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val isLoading by authViewModel.isLoading

    val isEmailValid = remember(userEmail) {
        userEmail.endsWith("@stis.ac.id")
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        backgroundColor = PurpleGrey40
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Column(modifier = Modifier.fillMaxWidth()) {
                SectionTitle("Profile Information")
                SectionDescription("Update your account’s profile information and email address.")
            }

            Spacer(modifier = Modifier.height(8.dp))
            Divider(color = Color.Gray.copy(alpha = 0.5f))
            Spacer(modifier = Modifier.height(8.dp))

            ProfileTextField(label = "Name", value = userName, readOnly = true)
            ProfileTextField(label = "Email", value = userEmail, onValueChange = onEmailChange)

            Spacer(modifier = Modifier.height(8.dp))

            SaveButton(onClick = {
                if (userEmail.isEmpty()) {
                    Toast.makeText(context, "Email tidak boleh kosong!", Toast.LENGTH_LONG).show()
                    return@SaveButton
                }

                if (!isEmailValid) {
                    Toast.makeText(context, "Hanya email @stis.ac.id yang diperbolehkan!", Toast.LENGTH_LONG).show()
                    return@SaveButton
                }

                authViewModel.updateEmail(
                    token = token,
                    newEmail = userEmail
                ) { result, success ->
                    Log.d("UpdateEmailResult", result)

                    if (success) {
                        Toast.makeText(context, "Email berhasil diperbarui! Silakan login kembali.", Toast.LENGTH_LONG).show()

                        coroutineScope.launch {
                            userPreferences.clearUserData() // **Hapus data user untuk logout**
                        }

                        // **Navigasi ke login setelah logout**
                        navController.navigate("login") {
                            popUpTo("home") { inclusive = true } // Pastikan tidak bisa kembali ke home
                        }
                    } else {
                        Toast.makeText(context, result, Toast.LENGTH_LONG).show()
                    }
                }
            }, isLoading = isLoading)
        }
    }
}

// Reusable Profile TextField
@Composable
fun ProfileTextField(label: String, value: String, readOnly: Boolean = false, onValueChange: (String) -> Unit = {}) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(label, fontFamily = Quicksand, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
        Spacer(modifier = Modifier.height(4.dp))
        TextField(
            value = value,
            onValueChange = onValueChange,
            readOnly = readOnly,
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next
            ),
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .height(45.dp),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color(0xFF2D3748),
                focusedIndicatorColor = Color.White,
                unfocusedIndicatorColor = Color.Transparent,
                textColor = Color.White
            ),
        )
        Spacer(modifier = Modifier.height(8.dp))
    }
}

// Update Password Section
@Composable
fun UpdatePasswordSection(authViewModel: AuthViewModel = viewModel(), token: String) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    val isLoading by authViewModel.isLoading

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        backgroundColor = PurpleGrey40
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Column(modifier = Modifier.fillMaxWidth()) {
                SectionTitle("Update Password")
                SectionDescription("Ensure your account is using a long, random password to stay secure.")
            }

            Spacer(modifier = Modifier.height(8.dp))
            Divider(color = Color.Gray.copy(alpha = 0.5f))
            Spacer(modifier = Modifier.height(8.dp))

            PasswordTextField(label = "Current Password", password = currentPassword, onPasswordChange = { currentPassword = it })
            PasswordTextField(label = "New Password", password = newPassword, onPasswordChange = { newPassword = it })
            PasswordTextField(label = "Confirm Password", password = confirmPassword, onPasswordChange = { confirmPassword = it })

            Spacer(modifier = Modifier.height(8.dp))

            SaveButton(onClick = {
                if (currentPassword.isEmpty()) {
                    Toast.makeText(context, "Masukkan password saat ini!", Toast.LENGTH_LONG).show()
                    return@SaveButton
                }
                if (newPassword != confirmPassword) {
                    Toast.makeText(context, "Password tidak cocok!", Toast.LENGTH_LONG).show()
                    return@SaveButton
                }

                // Panggil ViewModel untuk update password
                authViewModel.updatePassword(
                    token = token,
                    currentPassword = currentPassword,
                    newPassword = newPassword,
                    confirmPassword = confirmPassword
                ) { result, success ->
                    Log.d("UpdatePasswordResult", result)

                    if (success) {
                        Toast.makeText(context, "Password berhasil diperbarui!", Toast.LENGTH_SHORT).show()

                        currentPassword = ""
                        newPassword = ""
                        confirmPassword = ""
                    } else {
                        Toast.makeText(context, result, Toast.LENGTH_LONG).show()
                    }
                }
            }, isLoading = isLoading)
        }
    }
}

// Reusable Password TextField
@Composable
fun PasswordTextField(label: String, password: String, onPasswordChange: (String) -> Unit) {
    var passwordVisible by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(label, fontFamily = Quicksand, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
        Spacer(modifier = Modifier.height(4.dp))
        TextField(
            value = password,
            onValueChange = onPasswordChange,
            singleLine = true,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next
            ),
            trailingIcon = {
                val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                androidx.compose.material3.IconButton(onClick = {
                    passwordVisible = !passwordVisible
                }) {
                    androidx.compose.material3.Icon(
                        imageVector = image,
                        contentDescription = "Toggle Password Visibility",
                        tint = Color.White
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .height(45.dp),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color(0xFF2D3748),
                focusedIndicatorColor = Color.White,
                unfocusedIndicatorColor = Color.Transparent,
                textColor = Color.White
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(title, fontFamily = Quicksand, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
}

@Composable
fun SectionDescription(description: String) {
    Text(description, fontFamily = Quicksand, textAlign = TextAlign.Justify, fontSize = 14.sp, color = Color.White.copy(alpha = 0.8f))
}

@Composable
fun SaveButton(onClick: () -> Unit, isLoading: Boolean = false) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End // Posisikan tombol ke kanan
    ) {
        Button(
            onClick = onClick,
            modifier = Modifier.height(40.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = Purple80),
            enabled = !isLoading,
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier.size(24.dp) // Ukuran lebih kecil untuk dalam tombol
                )
            } else {
                Text("SIMPAN", fontFamily = Quicksand, fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
            }
        }
    }
}