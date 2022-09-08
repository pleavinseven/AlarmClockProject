package com.pleavinseven.alarmclockproject

import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.*
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
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

        val vibrate = intent?.extras?.getBoolean("vibrate")
        val shake = intent?.extras?.getBoolean("shake")!!
        val snooze = intent?.extras?.getInt("snooze")!!

        //create shake or non shake fragment and pass in vibrate boolean
        val fragmentTransaction = fragmentManager.beginTransaction()
        val fragObj = if (shake) ShakeAlarmRingFragment() else AlarmRingFragment()
        val bundle = Bundle()
        bundle.putBoolean("vibrate", vibrate!!)
        bundle.putInt("snooze", snooze)
        fragObj.arguments = bundle
        fragmentTransaction.add(R.id.fragmentContainerViewAlarm, fragObj)
        fragmentTransaction.commit()

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