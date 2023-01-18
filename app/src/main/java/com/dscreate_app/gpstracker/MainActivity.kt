package com.dscreate_app.gpstracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.dscreate_app.gpstracker.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        onBottomNavClicks()
    }

    private fun onBottomNavClicks() {
        binding.bNavView.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.id_home -> {
                    Toast.makeText(this, "home", Toast.LENGTH_SHORT).show()
                }
                R.id.id_tracks -> {
                    Toast.makeText(this, "tracks", Toast.LENGTH_SHORT).show()
                }
                R.id.id_settings -> {
                    Toast.makeText(this, "settings", Toast.LENGTH_SHORT).show()
                }
            }
            true
        }
    }
}