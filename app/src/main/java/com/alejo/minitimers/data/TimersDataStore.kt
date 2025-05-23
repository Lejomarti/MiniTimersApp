package com.alejo.minitimers.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map


val Context.dataStore by preferencesDataStore("timers_datastore")

class TimersDataStore(private val context: Context) {

    val timersFlow: Flow<List<Pair<String,Long>>> = context.dataStore.data
        .map { preferences ->
            preferences.asMap()
                .filter { it.key.name.startsWith("timer_") }
                .map { it.key.name to (it.value as Long) }
        }

    suspend fun saveTimer(timerId: String, time: Long) {
        context.dataStore.edit { preferences ->
            preferences[longPreferencesKey(timerId)] = time
        }
    }

    suspend fun removeTimer(timerId: String) {
        context.dataStore.edit { preferences ->
            preferences.remove(longPreferencesKey(timerId))
        }
    }

    suspend fun removeAllTimers (){
        context.dataStore.edit { preferences ->
            preferences.asMap().keys
                .filter { it.name.startsWith("timer_") }
                .forEach { preferences.remove(it) }
        }
    }

    suspend fun getTimerById(timerId: String): Long? {
        val preferences = context.dataStore.data.first()
        return preferences[longPreferencesKey(timerId)]
    }

    suspend fun updateTimer(timerId: String, newTime: Long) {
        context.dataStore.edit { preferences ->
            preferences[longPreferencesKey(timerId)] = newTime
        }
    }

}
