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


class SettingsDataStore(private val context: Context){
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("theme_settings")
        val IS_DARK_MODE = booleanPreferencesKey("is_dark_mode")
        val THEME_COLOR = stringPreferencesKey("theme_color")
    }

    val isDarkMode: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[IS_DARK_MODE] ?: false
        }

    suspend fun saveDarkMode(isDarkMode: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[IS_DARK_MODE] = isDarkMode
        }
    }

    val themeColor: Flow<String> = context.dataStore.data
        .map { preferences -> preferences[THEME_COLOR] ?: "Blue" }

    suspend fun saveThemeColor(colorName: String) {
        context.dataStore.edit { preferences ->
            preferences[THEME_COLOR] = colorName
        }
    }
}