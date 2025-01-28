package com.polstat.uas_ppk.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.polstat.uas_ppk.data.UserPreferences
import com.polstat.uas_ppk.ui.components.ParentScreen
import com.polstat.uas_ppk.ui.components.TopBar
import com.polstat.uas_ppk.ui.theme.PurpleGrey40
import com.polstat.uas_ppk.ui.theme.Quicksand
import com.polstat.uas_ppk.viewmodel.ServerViewModel
import kotlinx.coroutines.launch

@Composable
fun ServerRequestScreen(navController: NavController, serverViewModel: ServerViewModel = viewModel()) {
    val context = LocalContext.current
    val userPreferences = remember { UserPreferences(context) }
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    var purpose by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    val userData by userPreferences.userData.collectAsState(initial = null)
    var token by remember { mutableStateOf(userData?.accessToken ?: "") }

    LaunchedEffect(userData) {
        token = userData?.accessToken ?: ""
    }

    Scaffold(
        scaffoldState = scaffoldState,
        drawerContent = {
            ParentScreen(navController, selectedItem = "Server Request")
        },
        topBar = {
            TopBar(title = "Pengajuan Server", scaffoldState, navController, userPreferences)
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
            // Card Form Pengajuan Server
            Card(
                modifier = Modifier
                    .fillMaxWidth(),
                elevation = 4.dp,
                backgroundColor = PurpleGrey40
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Tujuan Pengajuan", fontFamily = Quicksand, fontSize = 14.sp, color = Color.White)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = purpose,
                        onValueChange = { purpose = it },
                        placeholder = { Text("Misal: Penelitian, Proyek, dll.", fontFamily = Quicksand, color = Color.White.copy(0.5f)) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Done
                        ),
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = Color(0xFF2D3748),
                            focusedIndicatorColor = Color.White,
                            unfocusedIndicatorColor = Color.Transparent,
                            textColor = Color.White
                        ),
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Tombol Submit
            Button(
                onClick = {
                    if (purpose.isEmpty()) {
                        Toast.makeText(context, "Mohon isi tujuan pengajuan server!", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    isLoading = true
                    coroutineScope.launch {
                        serverViewModel.submitServerRequest(token, purpose) { result, success ->
                            isLoading = false
                            Toast.makeText(context, result, Toast.LENGTH_LONG).show()

                            if (success) {
                                navController.popBackStack()
                            }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF5A49F8)),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text("Ajukan Server", fontFamily = Quicksand, color = Color.White, fontSize = 18.sp)
                }
            }
        }
    }
}
