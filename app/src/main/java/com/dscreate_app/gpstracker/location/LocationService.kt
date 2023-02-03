package com.dscreate_app.gpstracker.location

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.preference.PreferenceManager
import com.dscreate_app.gpstracker.MainActivity
import com.dscreate_app.gpstracker.R
import com.google.android.gms.location.*
import com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY
import org.osmdroid.util.GeoPoint


class LocationService: Service() {

    private lateinit var locationProvider: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private var lastLocation: Location? = null
    private var distance = 0.0f
    private lateinit var geoPointsList: ArrayList<GeoPoint>
    private var isDebug = false

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        geoPointsList = ArrayList()
        initLocation()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startNotification()
        startLocationUpdates()
        isRunning = true
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        isRunning = false
        locationProvider.removeLocationUpdates(locationCallback)
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            val currentLocation = locationResult.lastLocation
            if (lastLocation != null && currentLocation != null) {
                if (currentLocation.speed > 0.4 || isDebug) {
                    distance += lastLocation?.distanceTo(currentLocation) ?: 0.0f
                    geoPointsList.add(GeoPoint(currentLocation.latitude, currentLocation.longitude))
                }
                val locModel = LocationModel(
                    currentLocation.speed,
                    distance,
                    geoPointsList
                )
                sendLocData(locModel)
            }
            lastLocation = currentLocation
        }
    }

    private fun sendLocData(locModel: LocationModel) {
        val intent = Intent(LOC_MODEL_INTENT)
        intent.putExtra(LOC_MODEL_INTENT, locModel)
        LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)
    }

    private fun startNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val nManager = getSystemService(NotificationManager::class.java) as NotificationManager
            nManager.createNotificationChannel(notificationChannel)
        }
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            REQUEST_CODE,
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        )
        val notification = NotificationCompat.Builder(
            this, CHANNEL_ID
        )
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("Gps Tracker running!")
            .setContentIntent(pendingIntent).build()
        startForeground(NOTIFICATION_ID, notification)
    }

    private fun initLocation() {
        val updateInterval = PreferenceManager.getDefaultSharedPreferences(
            this
        ).getString(SHARED_PREF_KEY, SHARED_PREF_DEF_VALUE)?.toLong() ?: SHARED_PREF_DEF_VALUE
        locationRequest = LocationRequest.create()
        locationRequest.interval = updateInterval as Long
        locationRequest.fastestInterval = updateInterval
        locationRequest.priority = PRIORITY_HIGH_ACCURACY

        locationProvider = LocationServices.getFusedLocationProviderClient(baseContext)
        Log.d("MyLog", "Interval: $updateInterval")
    }

    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        locationProvider.requestLocationUpdates(
            locationRequest, locationCallback, Looper.myLooper()
        )
    }

    companion object {
        private const val CHANNEL_ID = "channel_1"
        private const val CHANNEL_NAME = "Location Service"
        private const val REQUEST_CODE = 10
        private const val NOTIFICATION_ID = 99
        private const val SHARED_PREF_KEY = "update_time_key"
        private const val SHARED_PREF_DEF_VALUE = "3000"
        const val LOC_MODEL_INTENT = "loc_intent"

        var isRunning = false
        var startTime = 0L
    }
}