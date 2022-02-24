package com.pleavinseven.alarmclockproject

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.*
import android.os.VibrationAttributes.USAGE_ALARM
import androidx.appcompat.app.AppCompatActivity
import androidx.annotation.RequiresApi
import com.pleavinseven.alarmclockproject.databinding.ActivityAlarmRingBinding

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

        vibe.vibrate(VibrationEffect.createWaveform(longArrayOf(200,1000,500,500),0))




        binding.btnCancelAlarm.setOnClickListener{
            ring.stop()
            vibe.cancel()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }


}