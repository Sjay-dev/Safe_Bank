package com.example.safebank.ViewModel

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

private val Context.themeDataStore by preferencesDataStore("appearance")
private val THEME_KEY = stringPreferencesKey("theme_mode")

enum class ThemeMode { SYSTEM, LIGHT, DARK }

@HiltViewModel
class ThemeViewModel @Inject constructor(@ApplicationContext private val context: Context) : ViewModel() {
    val themeMode = context.themeDataStore.data.map { preferences ->
        runCatching { ThemeMode.valueOf(preferences[THEME_KEY] ?: ThemeMode.SYSTEM.name) }.getOrDefault(ThemeMode.SYSTEM)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), ThemeMode.SYSTEM)

    fun setTheme(mode: ThemeMode) = viewModelScope.launch {
        context.themeDataStore.edit { it[THEME_KEY] = mode.name }
    }
}
