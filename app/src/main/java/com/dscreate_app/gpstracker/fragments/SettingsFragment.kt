package com.dscreate_app.gpstracker.fragments

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.dscreate_app.gpstracker.R

class SettingsFragment: PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.main_preference, rootKey)
    }
}