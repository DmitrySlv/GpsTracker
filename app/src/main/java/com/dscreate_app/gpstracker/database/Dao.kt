package com.dscreate_app.gpstracker.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface Dao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(trackItem: TrackItem)

    @Query("SELECT * FROM track")
    fun getAllTracks(): Flow<List<TrackItem>>
}