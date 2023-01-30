package com.dscreate_app.gpstracker.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*
@SuppressLint("SimpleDateFormat")
object TimeUtils {

    private val timeFormatter = SimpleDateFormat("HH:mm:ss")
    private val dateFormatter = SimpleDateFormat("dd/MM/yyyy HH:mm")

    fun getTime(timeInMillis: Long): String {
        val calendar = Calendar.getInstance()
        timeFormatter.timeZone = TimeZone.getTimeZone("UTC")
        calendar.timeInMillis = timeInMillis
        return timeFormatter.format(calendar.time)
    }

    fun getDate(): String {
        val calendar = Calendar.getInstance()
        return dateFormatter.format(calendar.time)
    }
}