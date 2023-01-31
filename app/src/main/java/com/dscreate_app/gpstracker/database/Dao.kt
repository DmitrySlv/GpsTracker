package com.dscreate_app.gpstracker.database

import androidx.room.Dao
import androidx.room.Insert

@Dao
interface Dao {
    @Insert
    suspend fun insertTrack(trackItem: TrackItem)
}