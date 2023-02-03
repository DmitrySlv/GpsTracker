package com.dscreate_app.gpstracker.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dscreate_app.gpstracker.database.MainDb

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(private val db: MainDb): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(db) as T
        }
        throw IllegalAccessException("Unknown ViewModel class")
    }
}