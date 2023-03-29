package com.pleavinseven.alarmclockproject.settings

import com.pleavinseven.alarmclockproject.fragments.SettingsFragment
import android.content.Intent
import android.content.res.Configuration
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

        // set action bar
        setTheme()
    }

    private fun setTheme() {
        val actionBar = supportActionBar
        actionBar?.title = ""
        actionBar?.elevation = 20F
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
                window.setBackgroundDrawable(ColorDrawable(baseContext.getColor(R.color.background_dark)))
                actionBar?.setBackgroundDrawable(ColorDrawable(baseContext.getColor(R.color.background_dark)))
            }

            Configuration.UI_MODE_NIGHT_NO -> {
                window.setBackgroundDrawable(ColorDrawable(baseContext.getColor(R.color.background_light)))
                actionBar?.setBackgroundDrawable(ColorDrawable(baseContext.getColor(R.color.background_light)))
            }

            Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                window.setBackgroundDrawable(ColorDrawable(baseContext.getColor(R.color.background_light)))
                actionBar?.setBackgroundDrawable(ColorDrawable(baseContext.getColor(R.color.background_light)))
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