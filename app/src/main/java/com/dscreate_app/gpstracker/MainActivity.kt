package com.dscreate_app.gpstracker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dscreate_app.gpstracker.databinding.ActivityMainBinding
import com.dscreate_app.gpstracker.fragments.MainFragment
import com.dscreate_app.gpstracker.fragments.SettingsFragment
import com.dscreate_app.gpstracker.fragments.TracksFragment
import com.dscreate_app.gpstracker.utils.openFragment

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        onBottomNavClicks()
        openFragment(MainFragment.newInstance())
    }

    private fun onBottomNavClicks() {
        binding.bNavView.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.id_home -> {
                    openFragment(MainFragment.newInstance())
                }
                R.id.id_tracks -> {
                    openFragment(TracksFragment.newInstance())
                }
                R.id.id_settings -> {
                    openFragment(SettingsFragment())
                }
            }
            true
        }
    }
}