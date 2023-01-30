package com.dscreate_app.gpstracker.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "track")
data class TrackItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    @ColumnInfo(name = "time")
    val time: String,
    @ColumnInfo(name = "date")
    val date: String,
    @ColumnInfo(name = "distance")
    val distance: String,
    @ColumnInfo(name = "speed")
    val speed: String,
    @ColumnInfo(name = "geo_points")
    val geoPoints: String
)
