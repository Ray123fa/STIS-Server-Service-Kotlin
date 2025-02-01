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

    private val _allServerRequests = MutableStateFlow<List<AllServerRequest>>(emptyList())
    val allServerRequests: StateFlow<List<AllServerRequest>> = _allServerRequests

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
                    purpose = MyServerRequest(purpose)
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

    fun fetchMyServerRequests(token: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = RetrofitClient.instance.getMyServerRequests("Bearer $token")
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

    fun fetchAllServerRequests(token: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response: Response<AllServerResponse> = RetrofitClient.instance.getAllServerRequests("Bearer $token")
                if (response.isSuccessful) {
                    response.body()?.let {
                        _allServerRequests.value = it.data
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun approveServerRequest(
        token: String,
        requestId: Int,
        onResult: (Boolean, String, String?, String?) -> Unit
    ) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response: Response<ApproveResponse> = RetrofitClient.instance.approveServerRequest(
                    token = "Bearer $token",
                    requestId = requestId
                )

                if (response.isSuccessful) {
                    val body = response.body()
                    val message = body?.message ?: "Permintaan server berhasil disetujui."
                    val username = body?.account?.username
                    val password = body?.account?.password

                    onResult(true, message, username, password)
                } else {
                    onResult(false, "Gagal menyetujui permintaan server.", null, null)
                }
            } catch (e: Exception) {
                onResult(false, "Terjadi kesalahan saat menyetujui permintaan server.", null, null)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun rejectServerRequest(
        token: String,
        requestId: Int,
        reason: String,
        onResult: (Boolean, String) -> Unit
    ) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response: Response<Map<String, Any>> = RetrofitClient.instance.rejectServerRequest(
                    token = "Bearer $token",
                    requestId = requestId,
                    reason = RejectRequest(reason)
                )

                if (response.isSuccessful) {
                    val responseBody = response.body()
                    val status = responseBody?.get("status") as? String
                    val message = responseBody?.get("message") as? String

                    if (status == "success") {
                        onResult(true, message ?: "Permintaan server berhasil ditolak.")
                    }
                } else {
                    onResult(false, "Gagal menolak permintaan server.")
                }
            } catch (e: Exception) {
                onResult(false, "Terjadi kesalahan saat menolak permintaan server.")
            } finally {
                _isLoading.value = false
            }
        }
    }
}
