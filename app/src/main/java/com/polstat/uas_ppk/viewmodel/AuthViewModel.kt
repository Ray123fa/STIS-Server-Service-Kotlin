package com.polstat.uas_ppk.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.polstat.uas_ppk.api.RetrofitClient
import com.polstat.uas_ppk.api.model.*
import kotlinx.coroutines.launch
import retrofit2.Response

class AuthViewModel : ViewModel() {
    var isLoading = mutableStateOf(false)

    fun login(email: String, password: String, onResult: (String, String?, String?, String?, String?) -> Unit) {
        viewModelScope.launch {
            isLoading.value = true
            try {
                val response = RetrofitClient.instance.login(AuthRequest(email, password))
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        onResult("Login Berhasil", body.accessToken, body.name, body.email, body.role)
                    }
                } else {
                    onResult("Email atau password tidak sesuai.", null, null, null, null)
                }
            } catch (e: Exception) {
                onResult("Terjadi kesalahan saat login.", null, null, null, null)
            }
            isLoading.value = false
        }
    }

    fun register(
        name: String,
        email: String,
        password: String,
        onResult: (message: String, success: Boolean) -> Unit
    ) {
        viewModelScope.launch {
            isLoading.value = true
            try {
                val response: Response<Map<String, Any>> = RetrofitClient.instance.register(RegisterRequest(name, email, password))

                if (response.isSuccessful) {
                    val responseBody = response.body()
                    val status = responseBody?.get("status") as? String
                    val message = responseBody?.get("message") as? String

                    if (status == "success") {
                        onResult(message ?: "Registrasi berhasil.", true)
                    } else {
                        onResult(message ?: "Registrasi gagal.", false)
                    }
                } else {
                    onResult("Registrasi Gagal: ${response.message()}", false)
                }
            } catch (e: Exception) {
                onResult("Terjadi kesalahan saat registrasi.", false)
            }
            isLoading.value = false
        }
    }

    fun updatePassword(
        token: String,
        currentPassword: String,
        newPassword: String,
        confirmPassword: String,
        onResult: (message: String, success: Boolean) -> Unit
    ) {
        viewModelScope.launch {
            isLoading.value = true
            try {
                val response: Response<Map<String, Any>> = RetrofitClient.instance.updatePassword(
                    token = "Bearer $token", // Format Bearer Token
                    request = UpdatePasswordRequest(currentPassword, newPassword, confirmPassword)
                )

                val responseBody = response.body()
                val status = responseBody?.get("status") as? String
                val message = responseBody?.get("message") as? String

                if (response.isSuccessful) {
                    if (status == "success") {
                        onResult(message ?: "Password berhasil diperbarui.", true)
                    }
                } else {
                    onResult("Password saat ini tidak sesuai.", false)
                }
            } catch (e: Exception) {
                onResult("Terjadi kesalahan saat memperbarui password.", false)
            }
            isLoading.value = false
        }
    }

    fun updateEmail(
        token: String,
        newEmail: String,
        onResult: (message: String, success: Boolean) -> Unit
    ) {
        viewModelScope.launch {
            isLoading.value = true
            try {
                val response: Response<Map<String, Any>> = RetrofitClient.instance.updateEmail(
                    token = "Bearer $token",
                    request = UpdateEmailRequest(newEmail)
                )

                val responseBody = response.body()
                val status = responseBody?.get("status") as? String
                val message = responseBody?.get("message") as? String

                if (response.isSuccessful) {
                    if (status == "success") {
                        onResult(message ?: "Email berhasil diperbarui.", true)
                    }
                } else {
                    onResult("Gagal memperbarui email.", false)
                }
            } catch (e: Exception) {
                onResult("Terjadi kesalahan saat memperbarui email.", false)
            }
            isLoading.value = false
        }
    }
}
