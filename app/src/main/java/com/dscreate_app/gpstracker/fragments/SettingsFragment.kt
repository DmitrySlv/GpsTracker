package com.dscreate_app.gpstracker.fragments

import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.Preference.OnPreferenceChangeListener
import androidx.preference.PreferenceFragmentCompat
import com.dscreate_app.gpstracker.R

class SettingsFragment: PreferenceFragmentCompat() {

    private lateinit var timePref: Preference

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.main_preference, rootKey)
        init()
    }

    private fun init() {
        timePref = findPreference(UPDATE_TIME_KEY)!!
        val changeListener = onChangeListener()
        timePref.onPreferenceChangeListener = changeListener
    }

    private fun onChangeListener(): OnPreferenceChangeListener {
        return OnPreferenceChangeListener {
            preference, value ->
            val nameArray = resources.getStringArray(R.array.location_time_update_name)
            val valueArray = resources.getStringArray(R.array.location_time_update_value)
            val title = preference.title.toString().substringBefore(":")
            val position = valueArray.indexOf(value)
            preference.title = "$title: ${nameArray[position]}"
            true
        }
    }


    companion object {

        private const val UPDATE_TIME_KEY = "update_time_key"
    }
}