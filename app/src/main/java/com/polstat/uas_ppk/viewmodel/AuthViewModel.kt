package com.polstat.uas_ppk.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.polstat.uas_ppk.api.RetrofitClient
import com.polstat.uas_ppk.api.model.AuthRequest
import com.polstat.uas_ppk.api.model.RegisterRequest
import kotlinx.coroutines.launch
import retrofit2.Response

class AuthViewModel : ViewModel() {
    fun login(email: String, password: String, onResult: (String, String?, String?, String?, String?) -> Unit) {
        viewModelScope.launch {
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
        }
    }

    fun register(
        name: String,
        email: String,
        password: String,
        onResult: (message: String, success: Boolean) -> Unit
    ) {
        viewModelScope.launch {
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
                onResult("Error: ${e.message}", false)
            }
        }
    }
}
