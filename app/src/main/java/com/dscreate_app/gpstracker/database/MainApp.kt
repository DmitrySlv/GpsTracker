package com.dscreate_app.gpstracker.database

import android.app.Application
import com.dscreate_app.gpstracker.database.MainDb

class MainApp: Application() {
    val database by lazy { MainDb.getInstanceDb(this) }
}