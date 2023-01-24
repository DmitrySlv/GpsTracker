package com.dscreate_app.gpstracker.location

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.dscreate_app.gpstracker.MainActivity
import com.dscreate_app.gpstracker.R


class LocationService: Service() {

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        Log.d("MyLog", "onCreate")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startNotification()
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("MyLog", "onDestroy")
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

    companion object {
        private const val CHANNEL_ID = "channel_1"
        private const val CHANNEL_NAME = "Location Service"
        private const val REQUEST_CODE = 10
        private const val NOTIFICATION_ID = 99
    }
}