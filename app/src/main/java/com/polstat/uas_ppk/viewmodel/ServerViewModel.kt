package com.polstat.uas_ppk.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.polstat.uas_ppk.api.RetrofitClient
import com.polstat.uas_ppk.api.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Response

class ServerViewModel : ViewModel() {
    private val _serverRequests = MutableStateFlow<List<ServerRequest>>(emptyList())
    val serverRequests: StateFlow<List<ServerRequest>> = _serverRequests

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun submitServerRequest(
        token: String,
        purpose: String,
        onResult: (String, Boolean) -> Unit
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response: Response<Map<String, Any>> = RetrofitClient.instance.requestServer(
                    token = "Bearer $token",
                    request = MyServerRequest(purpose)
                )

                if (response.isSuccessful) {
                    val responseBody = response.body()
                    val status = responseBody?.get("status") as? String
                    val message = responseBody?.get("message") as? String

                    if (status == "success") {
                        onResult(message ?: "Server berhasil diajukan!", true)
                    }
                } else {
                    onResult("Server gagal diajukan!", false)
                }
            } catch (e: Exception) {
                onResult("Terjadi kesalahan saat mengajukan server.", false)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun fetchServerRequests(token: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = RetrofitClient.instance.getServerRequests("Bearer $token")
                if (response.isSuccessful) {
                    response.body()?.let {
                        _serverRequests.value = it.data
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
}
