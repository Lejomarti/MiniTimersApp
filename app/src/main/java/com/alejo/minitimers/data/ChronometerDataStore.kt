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
