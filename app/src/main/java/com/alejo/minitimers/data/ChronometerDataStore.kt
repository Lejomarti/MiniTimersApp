package com.alejo.minitimers.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

object ChronometerDataStore {
    private const val PREFERENCES_NAME = "chronometer_prefs"
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = PREFERENCES_NAME)

    val START_TIME = longPreferencesKey("start_time")
    val IS_RUNNING = booleanPreferencesKey("is_running")
    val ELAPSED_TIME = longPreferencesKey("elapsed_time")
    val WAS_INITIALIZED = booleanPreferencesKey("was_initialized")

    suspend fun saveStartTime(context: Context, time: Long) {
        context.dataStore.edit { preferences ->
            preferences[START_TIME] = time
        }
    }

    suspend fun saveIsRunning(context: Context, isRunning: Boolean) {
        context.dataStore.edit {preferences ->
            preferences[IS_RUNNING] = isRunning
        }
    }

    suspend fun saveElapsedTime(context: Context, time: Long) {
        context.dataStore.edit { preferences ->
            preferences[ELAPSED_TIME] = time
        }
    }

    suspend fun saveWasInitialized(context: Context, value: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[WAS_INITIALIZED] = value
        }
    }

    fun readStartTime(context: Context): Flow<Long> =
        context.dataStore.data.map {preferences -> preferences[START_TIME] ?: 0L }

    fun readIsRunning(context: Context): Flow<Boolean> =
        context.dataStore.data.map {preferences -> preferences[IS_RUNNING] ?: false }

    fun readElapsedTime(context: Context): Flow<Long> =
        context.dataStore.data.map {preferences -> preferences[ELAPSED_TIME] ?: 0L }

    fun readWasInitialized(context: Context): Flow<Boolean> =
        context.dataStore.data.map {preferences -> preferences[WAS_INITIALIZED] ?: false }

}

