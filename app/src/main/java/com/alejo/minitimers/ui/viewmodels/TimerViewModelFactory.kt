package com.alejo.minitimers.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.alejo.minitimers.data.TimersDataStore

class TimerViewModelFactory(private val timersDataStore: TimersDataStore) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TimerViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TimerViewModel(timersDataStore) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}