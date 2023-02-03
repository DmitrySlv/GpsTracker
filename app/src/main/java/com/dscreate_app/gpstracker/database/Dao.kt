package com.dscreate_app.gpstracker.database

import androidx.room.*
import androidx.room.Dao
import kotlinx.coroutines.flow.Flow

@Dao
interface Dao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(trackItem: TrackItem)

    @Query("SELECT * FROM track")
    fun getAllTracks(): Flow<List<TrackItem>>

    @Delete
    suspend fun deleteTrack(trackItem: TrackItem)
}