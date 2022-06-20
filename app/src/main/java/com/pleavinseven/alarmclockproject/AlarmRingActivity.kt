package com.pleavinseven.alarmclockproject

import android.content.SharedPreferences
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.*
import android.view.View
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceManager
import com.pleavinseven.alarmclockproject.databinding.ActivityAlarmRingBinding
import com.pleavinseven.alarmclockproject.fragments.AlarmRingFragment
import com.pleavinseven.alarmclockproject.fragments.ShakeAlarmRingFragment

class AlarmRingActivity : AppCompatActivity() {

    lateinit var binding: ActivityAlarmRingBinding
    private val fragmentManager = supportFragmentManager


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlarmRingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = ""

        val prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)

        if(prefs.getBoolean("sp_shake", true)){
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.add(R.id.fragmentContainerViewAlarm, ShakeAlarmRingFragment())
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        } else {
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.add(R.id.fragmentContainerViewAlarm, AlarmRingFragment())
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }

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

    override fun onBackPressed() {
        // back not allowed
    }
}