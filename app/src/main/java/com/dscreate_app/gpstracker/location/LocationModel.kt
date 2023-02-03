package com.dscreate_app.gpstracker.location

import org.osmdroid.util.GeoPoint
import java.io.Serializable

data class LocationModel(
    val speed: Float = 0.0f,
    val distance: Float = 0.0f,
    val geoPointsList: ArrayList<GeoPoint>
): Serializable
