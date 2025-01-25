package com.polstat.uas_ppk.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.polstat.uas_ppk.api.RetrofitClient
import com.polstat.uas_ppk.api.model.AuthRequest
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    fun login(email: String, password: String, onResult: (String, String?) -> Unit) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.instance.login(AuthRequest(email, password))
                if (response.isSuccessful) {
                    val token = response.body()?.accessToken
                    onResult("Login Berhasil", token)
                } else {
                    onResult("Email atau password tidak sesuai.", null)
                }
            } catch (e: Exception) {
                onResult("Terjadi kesalahan saat login.", null)
            }
        }
    }
}
