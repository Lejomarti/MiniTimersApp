package com.alejo.minitimers.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.alejo.minitimers.data.ChronometerDataStore

class ChronometerViewModelFactory(
    private val dataStore: ChronometerDataStore
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ChronometerViewModel(dataStore) as T
    }
}