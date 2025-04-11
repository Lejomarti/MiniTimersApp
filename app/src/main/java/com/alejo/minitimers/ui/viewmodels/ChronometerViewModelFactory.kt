package com.alejo.minitimers.ui.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ChronometerViewModelFactory(
    private val application: Application
): ViewModelProvider.Factory{
    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChronometerViewModel::class.java)) {
            return ChronometerViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}