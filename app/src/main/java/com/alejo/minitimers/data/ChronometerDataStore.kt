package com.alejo.minitimers.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

//val Context.chronometerDataStore: DataStore<Preferences> by preferencesDataStore(name = "chronometer_prefs")
//class ChronometerDataStore(private val context: Context) {
//
//    val startTime: Flow<Long> = context.chronometerDataStore.data
//        .map { it[ChronometerPreferencesKeys.START_TIME] ?: 0L }
//
//    val isRunning: Flow<Boolean> = context.chronometerDataStore.data
//        .map { it[ChronometerPreferencesKeys.IS_RUNNING] ?: false }
//
//    val elapsedTime: Flow<Long> = context.chronometerDataStore.data
//        .map { it[ChronometerPreferencesKeys.ELAPSED_TIME] ?: 0L }
//
//    suspend fun saveStartTime(time: Long) {
//        context.chronometerDataStore.edit { it[ChronometerPreferencesKeys.START_TIME] = time }
//    }
//
//    suspend fun saveIsRunning(running: Boolean) {
//        context.chronometerDataStore.edit { it[ChronometerPreferencesKeys.IS_RUNNING] = running }
//    }
//
//    suspend fun saveElapsedTime(time: Long) {
//        context.chronometerDataStore.edit { it[ChronometerPreferencesKeys.ELAPSED_TIME] = time }
//    }
//
//    suspend fun clear() {
//        context.chronometerDataStore.edit { it.clear() }
//    }
//}
//
//object ChronometerPreferencesKeys {
//    val START_TIME = longPreferencesKey("start_time")
//    val IS_RUNNING = booleanPreferencesKey("is_running")
//    val ELAPSED_TIME = longPreferencesKey("elapsed_time")
//}

//1 identificar que necesito guardar -> starTime, isRunning y elapsedTime

//2. configurar las claves

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

