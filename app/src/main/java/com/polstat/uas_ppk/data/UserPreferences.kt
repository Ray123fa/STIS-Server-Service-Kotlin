package com.polstat.uas_ppk.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.concurrent.TimeUnit

private val Context.dataStore by preferencesDataStore("user_prefs")

class UserPreferences(private val context: Context) {
    companion object {
        private val ACCESS_TOKEN = stringPreferencesKey("access_token")
        private val TOKEN_TIMESTAMP = longPreferencesKey("token_timestamp")
        private const val TOKEN_EXPIRATION_DAYS = 7 // Token berlaku selama 7 hari
    }

    // Ambil accessToken jika masih berlaku
    val accessToken: Flow<String?> = context.dataStore.data.map { prefs ->
        val token = prefs[ACCESS_TOKEN]
        val timestamp = prefs[TOKEN_TIMESTAMP] ?: 0L
        val isTokenValid = System.currentTimeMillis() - timestamp < TimeUnit.DAYS.toMillis(
            TOKEN_EXPIRATION_DAYS.toLong()
        )

        if (token != null && isTokenValid) {
            token
        } else {
            null // Jika token expired, hapus token
        }
    }

    // Simpan accessToken dengan timestamp lokal
    suspend fun saveAccessToken(token: String) {
        context.dataStore.edit { prefs ->
            prefs[ACCESS_TOKEN] = token
            prefs[TOKEN_TIMESTAMP] = System.currentTimeMillis() // Simpan waktu saat token disimpan
        }
    }

    // Hapus accessToken
    suspend fun clearAccessToken() {
        context.dataStore.edit { prefs ->
            prefs.remove(ACCESS_TOKEN)
            prefs.remove(TOKEN_TIMESTAMP)
        }
    }
}