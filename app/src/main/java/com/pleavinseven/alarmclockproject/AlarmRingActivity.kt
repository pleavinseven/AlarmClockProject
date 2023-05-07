package com.pleavinseven.alarmclockproject

import android.content.res.Configuration
import android.graphics.drawable.ColorDrawable
import android.os.*
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.pleavinseven.alarmclockproject.databinding.ActivityAlarmRingBinding
import com.pleavinseven.alarmclockproject.fragments.AlarmRingFragment
import com.pleavinseven.alarmclockproject.fragments.ShakeAlarmRingFragment

class AlarmRingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAlarmRingBinding
    private val fragmentManager = supportFragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlarmRingBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

        backGroundColour()
    }

    private fun backGroundColour() {
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
                actionBar?.setBackgroundDrawable(ColorDrawable(baseContext.getColor(R.color.background_dark)))
                window.decorView.setBackgroundColor(baseContext.getColor(R.color.background_dark))
            }
            Configuration.UI_MODE_NIGHT_NO -> {
                actionBar?.setBackgroundDrawable(ColorDrawable(baseContext.getColor(R.color.background_light)))
                window.decorView.setBackgroundColor(baseContext.getColor(R.color.background_light))
            }
            Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                actionBar?.setBackgroundDrawable(ColorDrawable(baseContext.getColor(R.color.background_light)))
            }
        }
    }

    override fun onBackPressed() {
        // back not allowed
    }
}