package com.dscreate_app.gpstracker.fragments

import android.graphics.Color
import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.Preference.OnPreferenceChangeListener
import androidx.preference.PreferenceFragmentCompat
import com.dscreate_app.gpstracker.R

class SettingsFragment: PreferenceFragmentCompat() {

    private lateinit var timePref: Preference
    private lateinit var colorPref: Preference

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.main_preference, rootKey)
        init()
    }

    private fun init() {
        timePref = findPreference(UPDATE_TIME_KEY)!!
        colorPref = findPreference(COLOR_KEY)!!
        val changeListener = onChangeListener()
        timePref.onPreferenceChangeListener = changeListener
        colorPref.onPreferenceChangeListener = changeListener
        initPreferences()
    }

    private fun onChangeListener(): OnPreferenceChangeListener {
        return OnPreferenceChangeListener {
            preference, value ->
            when (preference.key) {
                UPDATE_TIME_KEY -> onTimeChange(value.toString())
                COLOR_KEY -> preference.icon?.setTint(Color.parseColor(value.toString()))
            }
            true
        }
    }

    private fun initPreferences() {
        val pref = timePref.preferenceManager.sharedPreferences
        val nameArray = resources.getStringArray(R.array.location_time_update_name)
        val valueArray = resources.getStringArray(R.array.location_time_update_value)
        val title = timePref.title
        val position = valueArray.indexOf(pref?.getString(UPDATE_TIME_KEY, DEF_VALUE_TIME))
        timePref.title = "$title: ${nameArray[position]}"

        val trackColor = pref?.getString(COLOR_KEY, DEF_VALUE_COLOR)
        colorPref.icon?.setTint(Color.parseColor(trackColor))
    }

    private fun onTimeChange(value: String) {
        val nameArray = resources.getStringArray(R.array.location_time_update_name)
        val valueArray = resources.getStringArray(R.array.location_time_update_value)
        val title = timePref.title.toString().substringBefore(DELIMITER)
        val position = valueArray.indexOf(value)
        timePref.title = "$title: ${nameArray[position]}"
    }


    companion object {

        private const val UPDATE_TIME_KEY = "update_time_key"
        private const val COLOR_KEY = "color_key"
        private const val DELIMITER = ":"
        private const val DEF_VALUE_TIME = "3000"
        private const val DEF_VALUE_COLOR = "#03A9F4"
    }
}