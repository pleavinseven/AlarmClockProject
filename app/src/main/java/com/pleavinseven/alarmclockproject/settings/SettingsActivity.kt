package com.pleavinseven.alarmclockproject.settings

import com.pleavinseven.alarmclockproject.fragments.SettingsFragment
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.*
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import com.pleavinseven.alarmclockproject.R
import com.pleavinseven.alarmclockproject.databinding.ActivitySettingsBinding


class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportFragmentManager.beginTransaction()
            .replace(R.id.settings_container, SettingsFragment())
            .commit()

        // set action bar, status bar and nav to fit theme
        backGroundColour()
    }

    private fun backGroundColour() {
        val actionBar = supportActionBar
        actionBar?.title = ""
        actionBar?.elevation = 0F
        WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS
        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        val nightModeFlags: Int = this.resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK
        when (nightModeFlags) {
            Configuration.UI_MODE_NIGHT_YES -> {
                window.setBackgroundDrawableResource(R.drawable.dark_gradient_background)
                window.statusBarColor = Color.parseColor("#133936")
                actionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#133936")))
                window.navigationBarColor = Color.parseColor("#715c54")
            }
            Configuration.UI_MODE_NIGHT_NO -> {
                window.setBackgroundDrawableResource(R.drawable.gradient_background)
                window.statusBarColor = Color.parseColor("#248e8a")
                actionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#248e8a")))
                window.navigationBarColor = Color.parseColor("#e3b6a2")
            }
            Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                window.setBackgroundDrawableResource(R.drawable.gradient_background)
                window.statusBarColor = Color.parseColor("#248e8a")
                actionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#248e8a")))
                window.navigationBarColor = Color.parseColor("#e3b6a2")
            }
        }
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