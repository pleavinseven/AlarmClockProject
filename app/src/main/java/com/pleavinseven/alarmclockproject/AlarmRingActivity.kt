package com.pleavinseven.alarmclockproject

import android.os.*
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
    }


    override fun onBackPressed() {
        // back not allowed
    }
}