package com.polstat.uas_ppk.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.polstat.uas_ppk.api.model.AuthResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.concurrent.TimeUnit

private val Context.dataStore by preferencesDataStore("user_prefs")

class UserPreferences(private val context: Context) {
    companion object {
        private val ACCESS_TOKEN = stringPreferencesKey("access_token")
        private val USER_NAME = stringPreferencesKey("user_name")
        private val USER_EMAIL = stringPreferencesKey("user_email")
        private val USER_ROLE = stringPreferencesKey("user_role")
        private val TOKEN_TIMESTAMP = longPreferencesKey("token_timestamp")
        private const val TOKEN_EXPIRATION_DAYS = 7
    }

    // Ambil data jika masih berlaku (kurang dari 7 hari)
    val userData: Flow<AuthResponse?> = context.dataStore.data.map { prefs ->
        val token = prefs[ACCESS_TOKEN]
        val name = prefs[USER_NAME]
        val email = prefs[USER_EMAIL]
        val role = prefs[USER_ROLE]
        val timestamp = prefs[TOKEN_TIMESTAMP] ?: 0L
        val isTokenValid = System.currentTimeMillis() - timestamp < TimeUnit.DAYS.toMillis(
            TOKEN_EXPIRATION_DAYS.toLong()
        )

        if (token != null && name != null && email != null && role != null && isTokenValid) {
            AuthResponse(name, email, role, token)
        } else {
            null // Jika token expired, kembalikan null
        }
    }

    // Simpan data user dengan timestamp
    suspend fun saveUserData(token: String, name: String, email: String, role: String) {
        context.dataStore.edit { prefs ->
            prefs[ACCESS_TOKEN] = token
            prefs[USER_NAME] = name
            prefs[USER_EMAIL] = email
            prefs[USER_ROLE] = role
            prefs[TOKEN_TIMESTAMP] = System.currentTimeMillis()
        }
    }

    // Hapus data user saat logout
    suspend fun clearUserData() {
        context.dataStore.edit { prefs ->
            prefs.remove(ACCESS_TOKEN)
            prefs.remove(USER_NAME)
            prefs.remove(USER_EMAIL)
            prefs.remove(USER_ROLE)
            prefs.remove(TOKEN_TIMESTAMP)
        }
    }
}