package com.alejo.minitimers.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


// Extension para acceder a DataStore
val Context.dataStore by preferencesDataStore("timers_datastore")

class TimersDataStore(private val context: Context) {

    private val TIMER_LIST_KEY = longPreferencesKey("timer_list")

    // Recuperar la lista de temporizadores guardados
    val timersFlow: Flow<List<Long>> = context.dataStore.data
        .map { preferences ->
            preferences.asMap().filter { it.key.name.startsWith("timer_") }
                .map { it.value as Long }
        }

    // Guardar un nuevo temporizador
    suspend fun saveTimer(time: Long) {
        context.dataStore.edit { preferences ->
            val newKey = "timer_${System.currentTimeMillis()}"
            preferences[longPreferencesKey(newKey)] = time
        }
    }

    // Eliminar un temporizador
    suspend fun removeTimer(time: Long) {
        context.dataStore.edit { preferences ->
            preferences.asMap().filter { it.value == time }
                .keys.forEach { preferences.remove(it) }
        }
    }
}