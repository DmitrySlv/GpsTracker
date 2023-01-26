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
import com.dscreate_app.gpstracker.MainActivity
import com.dscreate_app.gpstracker.R
import com.google.android.gms.location.*
import com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY


class LocationService: Service() {

    private lateinit var locationProvider: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private var lastLocation: Location? = null
    private var distance = 0.0f

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
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
                if (currentLocation.speed > 0.2) distance += lastLocation?.distanceTo(currentLocation) ?: 0.0f
            }
            lastLocation = currentLocation
            Log.d("MyLog", "Distance: $distance")
        }
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
            0
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
        locationRequest = LocationRequest.create()
        locationRequest.interval = 5000
        locationRequest.fastestInterval = 5000
        locationRequest.priority = PRIORITY_HIGH_ACCURACY

        locationProvider = LocationServices.getFusedLocationProviderClient(baseContext)
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

        var isRunning = false
        var startTime = 0L
    }
}