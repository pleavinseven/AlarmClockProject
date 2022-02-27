package com.pleavinseven.alarmclockproject

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.annotation.RequiresApi
import com.pleavinseven.alarmclockproject.databinding.ActivityAlarmRingBinding
import java.util.*

class AlarmRingActivity : AppCompatActivity() {

    lateinit var binding: ActivityAlarmRingBinding

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlarmRingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = ""


        val ring = MediaPlayer.create(this, R.raw.finch)
        ring.isLooping = true
        ring.start()
        val vibe =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager =
                    getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
                vibratorManager.defaultVibrator
            } else {
                getSystemService(VIBRATOR_SERVICE) as Vibrator
            }

        vibe.vibrate(VibrationEffect.createWaveform(longArrayOf(200, 1000, 500, 500), 0))
        // TODO: set vibrate pattern options?




        binding.btnCancelAlarm.setOnClickListener {
            ring.stop()
            vibe.cancel()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        binding.btnSnoozeAlarm.setOnClickListener{
            val alarmId = Random().nextInt(Integer.MAX_VALUE)
            val calendar: Calendar = Calendar.getInstance()
            val snoozeMins = 10
            calendar.timeInMillis = System.currentTimeMillis()
            calendar.add(Calendar.MINUTE, snoozeMins) // TODO: set snooze length options
            val alarm = AlarmManager(
                alarmId,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                "Snooze",
                started = true,
                recurring = false
            )
            Toast.makeText(this, "Alarm snoozed for $snoozeMins minutes", Toast.LENGTH_SHORT).show()
            ring.stop()
            vibe.cancel()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            alarm.schedule(this)
        }
    }


}