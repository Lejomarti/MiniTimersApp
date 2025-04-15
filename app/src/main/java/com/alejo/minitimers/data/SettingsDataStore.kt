package com.alejo.minitimers.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


object SettingsDataStore {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("theme_settings")
    private val IS_DARK_MODE = booleanPreferencesKey("is_dark_mode")
    private val THEME_COLOR = stringPreferencesKey("theme_color")
    private val SELECTED_SOUND = stringPreferencesKey("selected_sound")

    fun isDarkMode(context: Context): Flow<Boolean> = context.dataStore.data.map { it[IS_DARK_MODE] ?: false }

    suspend fun saveDarkMode(context: Context, isDarkMode: Boolean) {
        context.dataStore.edit { it[IS_DARK_MODE] = isDarkMode }
    }

    fun themeColor(context: Context): Flow<String> = context.dataStore.data.map { it[THEME_COLOR] ?: "Blue" }

    suspend fun saveThemeColor(context: Context, colorName: String) {
        context.dataStore.edit {it[THEME_COLOR] = colorName}
    }

    fun selectedSound(context: Context): Flow<String> = context.dataStore.data.map { it[SELECTED_SOUND] ?: "timer" }

    suspend fun saveSelectedSound(context: Context, soundName: String) {
        context.dataStore.edit {it[SELECTED_SOUND] = soundName}
    }
}
