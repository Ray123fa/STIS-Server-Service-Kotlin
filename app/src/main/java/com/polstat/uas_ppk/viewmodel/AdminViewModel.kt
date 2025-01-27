package com.polstat.uas_ppk.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.polstat.uas_ppk.api.RetrofitClient
import com.polstat.uas_ppk.api.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Response

class AdminViewModel : ViewModel() {
    private val _userList = MutableStateFlow<List<User>>(emptyList())
    val userList: StateFlow<List<User>> = _userList

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun fetchUsers(token: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = RetrofitClient.instance.getUserList("Bearer $token")
                if (response.isSuccessful) {
                    response.body()?.let {
                        _userList.value = it.data
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteUser(token: String, email: String, onResult: (String, Boolean) -> Unit) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response: Response<Map<String, Any>> = RetrofitClient.instance.deleteUser(
                    token = "Bearer $token",
                    email = email
                )

                if (response.isSuccessful) {
                    val body = response.body()
                    val message = body?.get("message") as? String ?: "User berhasil dihapus."
                    val status = body?.get("status") as? String

                    if (status == "success") {
                        onResult(message, true)
                    }
                } else {
                    onResult("Gagal menghapus user.", false)
                }
            } catch (e: Exception) {
                onResult("Terjadi kesalahan saat menghapus user.", false)
            } finally {
                _isLoading.value = false
            }
        }
    }
}
