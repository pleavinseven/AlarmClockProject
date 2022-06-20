package com.pleavinseven.alarmclockproject

import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.pleavinseven.alarmclockproject.databinding.ActivityMainBinding
import com.pleavinseven.alarmclockproject.settings.SettingsActivity


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val decor = window.decorView
        decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)


        // set action bar, status bar and nav to fit theme
        actionBarColour()
        backGroundColour()
        navBarColour()
    }

    private fun actionBarColour() {
        val actionBar = supportActionBar
        actionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#018786")))
        actionBar?.title = ""
        actionBar?.elevation = 0F
    }

    private fun backGroundColour() {
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = Color.parseColor("#018786")
        window.setBackgroundDrawableResource(R.drawable.gradient_background)
    }

    private fun navBarColour() {
        WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS
        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        val nightModeFlags: Int = this.resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK
        when (nightModeFlags) {
            Configuration.UI_MODE_NIGHT_YES -> {
                window.navigationBarColor = ContextCompat.getColor(this, R.color.black)
            }
            Configuration.UI_MODE_NIGHT_NO -> {
                window.navigationBarColor =
                    ContextCompat.getColor(this, android.R.color.white)
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
            }
            Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                window.navigationBarColor =
                    ContextCompat.getColor(this, android.R.color.white)
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
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
