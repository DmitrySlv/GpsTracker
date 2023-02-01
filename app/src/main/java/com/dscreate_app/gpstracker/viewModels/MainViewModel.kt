package com.dscreate_app.gpstracker.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dscreate_app.gpstracker.database.MainDb
import com.dscreate_app.gpstracker.database.TrackItem
import com.dscreate_app.gpstracker.location.LocationModel
import kotlinx.coroutines.launch

class MainViewModel(db: MainDb): ViewModel() {
    private val dao = db.getDao()
    val locationUpdates = MutableLiveData<LocationModel>()
    val timeData = MutableLiveData<String>()
    val tracks = dao.getAllTracks().asLiveData()

    fun insertTrack(trackItem: TrackItem) = viewModelScope.launch {
        dao.insertTrack(trackItem)
    }

    fun deleteTrack(trackItem: TrackItem) = viewModelScope.launch {
        dao.deleteTrack(trackItem)
    }
}