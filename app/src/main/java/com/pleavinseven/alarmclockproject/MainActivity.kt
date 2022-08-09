package com.pleavinseven.alarmclockproject

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.pleavinseven.alarmclockproject.databinding.ActivityMainBinding
import com.pleavinseven.alarmclockproject.settings.SettingsActivity


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // set action bar, status bar and nav to fit theme
        setDayNight()
        actionBarColour()
        backGroundColour()
        navBarColour()
    }

    private fun actionBarColour() {
        val actionBar = supportActionBar
        actionBar?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        actionBar?.title = ""
        actionBar?.elevation = 0F
    }

    private fun backGroundColour() {
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = Color.parseColor("#F0EDFF")
        window.setBackgroundDrawable(ColorDrawable(Color.parseColor("#CDCCF0")))
    }

    private fun navBarColour() {
        val actionBar = supportActionBar
        actionBar?.title = ""
        actionBar?.elevation = 0F
        WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
        } else {
            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        }
        val nightModeFlags: Int = this.resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK
        when (nightModeFlags) {
            Configuration.UI_MODE_NIGHT_YES -> {
                actionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#56557B")))
                window.navigationBarColor = Color.parseColor("#F0EDFF")

            }
            Configuration.UI_MODE_NIGHT_NO -> {
                window.navigationBarColor = Color.parseColor("#F0EDFF")
            }
            Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                window.navigationBarColor = Color.parseColor("#F0EDFF")
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
