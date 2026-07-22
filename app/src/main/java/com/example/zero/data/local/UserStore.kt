package com.example.zero.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class UserStore (private val context: Context) {
    companion object {
        val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "sesion")
        val DATOS_USUARIO = stringPreferencesKey("datos_usuario")
    }
    val leerDatosUsuario: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[DATOS_USUARIO] ?: ""
    }
    suspend fun guardarDatosUsuario(datosUsuario: String) {
        context.dataStore.edit { preferences ->
            preferences[DATOS_USUARIO] = datosUsuario
        }
    }
}