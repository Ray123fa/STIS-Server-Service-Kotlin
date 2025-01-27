package com.polstat.uas_ppk.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.polstat.uas_ppk.api.model.User
import com.polstat.uas_ppk.data.UserPreferences
import com.polstat.uas_ppk.ui.components.ParentScreen
import com.polstat.uas_ppk.ui.components.TopBar
import com.polstat.uas_ppk.ui.theme.PurpleGrey40
import com.polstat.uas_ppk.ui.theme.Quicksand
import com.polstat.uas_ppk.viewmodel.AdminViewModel
import kotlinx.coroutines.launch

@Composable
fun UserListScreen(navController: NavController, adminViewModel: AdminViewModel = viewModel()) {
    val context = LocalContext.current
    val userPreferences = remember { UserPreferences(context) }
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    val userList by adminViewModel.userList.collectAsState()
    val isLoading by adminViewModel.isLoading.collectAsState()

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            userPreferences.userData.collect { userData ->
                userData?.let {
                    adminViewModel.fetchUsers(it.accessToken)
                }
            }
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        drawerContent = {
            ParentScreen(navController, selectedItem = "User Management")
        },
        topBar = {
            TopBar(title = "User Management", scaffoldState, navController, userPreferences)
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
            Spacer(modifier = Modifier.height(8.dp))
            UserTable(navController, userList, isLoading, adminViewModel)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

// Komponen Tabel
@Composable
fun UserTable(navController: NavController, userList: List<User>, isLoading: Boolean, adminViewModel: AdminViewModel) {
    if (isLoading) {
        CircularProgressIndicator()
    } else {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp)),
            elevation = 4.dp,
            backgroundColor = PurpleGrey40
        ) {
            Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                Text("Daftar Pengguna", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Spacer(modifier = Modifier.height(8.dp))
                Divider(color = Color.Gray.copy(alpha = 0.5f))
                Spacer(modifier = Modifier.height(8.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    TableHeaderItem("No", Modifier.weight(0.35f))
                    TableHeaderItem("Name", Modifier.weight(1f))
                    TableHeaderItem("Email", Modifier.weight(1f))
                    TableHeaderItem("Role", Modifier.weight(1.6f))
                    TableHeaderItem("Actions", Modifier.weight(1f))
                }
                Divider(color = Color.Gray)

                LazyColumn {
                    itemsIndexed(userList) { index, user ->
                        TableRow(navController, index + 1, user, adminViewModel)
                        Divider(color = Color.Gray)
                    }
                }
            }
        }
    }
}

// Komponen Header Tabel
@Composable
fun TableHeaderItem(text: String, modifier: Modifier) {
    Text(text, fontWeight = FontWeight.Bold, color = Color.White, fontSize = 14.sp, modifier = modifier)
    Spacer(modifier = Modifier.width(4.dp))
}

// Komponen Row Tabel
@Composable
fun TableRow(navController: NavController, index: Int, user: User, adminViewModel: AdminViewModel) {
    val context = LocalContext.current
    val userPreferences = remember { UserPreferences(context) }
    val userData by userPreferences.userData.collectAsState(initial = null)
    var token by remember { mutableStateOf(userData?.accessToken ?: "") }
    token = userData?.accessToken ?: ""

    var showDialog by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        TableCell(text = index.toString(), Modifier.weight(0.35f))
        TableCell(text = user.name, Modifier.weight(1f))
        TableCell(text = user.email, Modifier.weight(1f))
        TableCell(text = user.role, Modifier.weight(1.6f))

        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ActionButton(text = "Edit", backgroundColor = Color(0xFF4CAF50)) {
                Toast.makeText(context, "Anda mengklik Edit pada ${user.name}", Toast.LENGTH_SHORT).show()
            }
            ActionButton(text = "Delete", backgroundColor = Color(0xFFD32F2F)) {
                showDialog = true
            }
        }
    }

    // Dialog Konfirmasi Hapus
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Delete Confirmation") },
            text = { Text(buildAnnotatedString {
                append("Apakah Anda yakin ingin menghapus user dengan email ")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(user.email)
                }
                append("?")
            }) },
            confirmButton = {
                Button(
                    onClick = {
                        adminViewModel.deleteUser(token = token, email = user.email) { result, success ->
                            Toast.makeText(context, result, Toast.LENGTH_SHORT).show()

                            if (success) {
                                navController.navigate("user_management") {
                                    popUpTo("user_management") { inclusive = true }
                                }
                            }
                        }
                        showDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red)
                ) {
                    Text("Hapus", color = Color.White)
                }
            },
            dismissButton = {
                Button(onClick = { showDialog = false }) {
                    Text("Batal")
                }
            }
        )
    }
}

// Komponen Sel Tabel
@Composable
fun TableCell(text: String, modifier: Modifier) {
    Text(
        text,
        fontSize = 14.sp,
        color = Color.White,
        fontFamily = Quicksand,
        modifier = modifier
    )
    Spacer(modifier = Modifier.width(4.dp))
}

// Komponen Tombol Aksi
@Composable
fun ActionButton(text: String, backgroundColor: Color, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(backgroundColor = backgroundColor)
    ) {
        Text(text, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color.White)
    }
}
