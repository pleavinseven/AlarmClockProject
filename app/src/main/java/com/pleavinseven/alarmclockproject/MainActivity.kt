package com.pleavinseven.alarmclockproject

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.pleavinseven.alarmclockproject.databinding.ActivityMainBinding
import com.pleavinseven.alarmclockproject.settings.SettingsActivity


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPreferences: SharedPreferences
    private var nightModeFlags: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        nightModeFlags = this.resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK
        setDayNight()
        actionBarColour()
    }

    private fun actionBarColour() {
        val actionBar = supportActionBar
        actionBar?.title = ""
        actionBar?.elevation = 20F
        when (nightModeFlags) {
            Configuration.UI_MODE_NIGHT_YES -> {
                actionBar?.setBackgroundDrawable(ColorDrawable(baseContext.getColor(R.color.background_dark)))
            }
            Configuration.UI_MODE_NIGHT_NO -> {
                actionBar?.setBackgroundDrawable(ColorDrawable(baseContext.getColor(R.color.background_light)))
            }
            Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                actionBar?.setBackgroundDrawable(ColorDrawable(baseContext.getColor(R.color.background_light)))
            }
        }
    }

    private fun setDayNight() {
        sharedPreferences = getSharedPreferences("nightModePrefs", Context.MODE_PRIVATE)!!
        val nightMode = sharedPreferences.getBoolean("isNight", true)
        if (nightMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                startActivity(Intent(this, SettingsActivity::class.java))
                true
            }
            else -> false
        }
    }
}
