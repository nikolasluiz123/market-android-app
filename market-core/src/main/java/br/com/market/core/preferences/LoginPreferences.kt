package br.com.market.core.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

/**
 * Definindo acesso ao armazenamento local do Android por meio do Context.
 *
 * @author Nikolas Luiz Schmitt
 */
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "login")

/**
 * Objeto que contém as chaves usadas para recuperar e gravar informações
 * no DataStore.
 *
 * @author Nikolas Luiz Schmitt
 */
object PreferencesKey{
    val TOKEN = stringPreferencesKey("token")
    val USER = stringPreferencesKey("userId")
}

/**
 * Função criada exclusivamente para recuperar o token, não fica
 * observando as alterações com o collect, apenas pega o resultado vindo
 * do primeiro fluxo.
 *
 * @author Nikolas Luiz Schmitt
 */
suspend fun DataStore<Preferences>.getToken(): String? {
    return this.data.first()[PreferencesKey.TOKEN]
}
suspend fun DataStore<Preferences>.getUserId(): String? {
    return this.data.first()[PreferencesKey.USER]
}