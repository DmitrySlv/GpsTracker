package com.dscreate_app.gpstracker.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

object TimeUtils {

    @SuppressLint("SimpleDateFormat")
    private val timeFormatter = SimpleDateFormat("HH:mm:ss")

    fun getTime(timeInMillis: Long): String {
        val calendar = Calendar.getInstance()
        timeFormatter.timeZone = TimeZone.getTimeZone("UTC")
        calendar.timeInMillis = timeInMillis
        return timeFormatter.format(calendar.time)
    }
}