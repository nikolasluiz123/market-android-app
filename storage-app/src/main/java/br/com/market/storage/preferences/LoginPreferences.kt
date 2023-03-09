package br.com.market.storage.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "login")

object PreferencesKey{
    val TOKEN = stringPreferencesKey("token")
}

suspend fun DataStore<Preferences>.getToken(): String? {
    return this.data.first()[PreferencesKey.TOKEN]
}