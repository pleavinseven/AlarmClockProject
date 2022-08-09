package com.pleavinseven.alarmclockproject

import android.content.SharedPreferences
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.*
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.pleavinseven.alarmclockproject.databinding.ActivityAlarmRingBinding
import com.pleavinseven.alarmclockproject.fragments.AlarmRingFragment
import com.pleavinseven.alarmclockproject.fragments.ShakeAlarmRingFragment

class AlarmRingActivity : AppCompatActivity() {

    lateinit var binding: ActivityAlarmRingBinding
    private val fragmentManager = supportFragmentManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlarmRingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = ""

        val prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)

        if (prefs.getBoolean("sp_shake", true)) {
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
        backGroundColour()
    }

    private fun backGroundColour() {
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
                window.setBackgroundDrawable(ColorDrawable(Color.parseColor("#56557B")))
                window.statusBarColor = Color.parseColor("#F0EDFF")
                actionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#56557B")))
                window.navigationBarColor = Color.parseColor("#F0EDFF")
            }
            Configuration.UI_MODE_NIGHT_NO -> {
                window.setBackgroundDrawable(ColorDrawable(Color.parseColor("#CDCCF0")))
                window.statusBarColor = Color.parseColor("#F0EDFF")
                actionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#CDCCF0")))
                window.navigationBarColor = Color.parseColor("#F0EDFF")
            }
            Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                window.setBackgroundDrawable(ColorDrawable(Color.parseColor("#CDCCF0")))
                window.statusBarColor = Color.parseColor("#F0EDFF")
                actionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#CDCCF0")))
                window.navigationBarColor = Color.parseColor("#F0EDFF")
            }
        }
    }

    override fun onBackPressed() {
        // back not allowed
    }
}