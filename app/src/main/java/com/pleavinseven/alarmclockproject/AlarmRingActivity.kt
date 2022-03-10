package com.pleavinseven.alarmclockproject

import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.media.MediaPlayer
import android.os.*
import android.util.Log
import android.view.WindowManager
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.annotation.RequiresApi
import androidx.preference.Preference
import com.pleavinseven.alarmclockproject.databinding.ActivityAlarmRingBinding
import com.pleavinseven.alarmclockproject.alarmmanager.AlarmManager
import com.pleavinseven.alarmclockproject.data.model.Alarm
import java.util.*
import kotlin.math.abs
import kotlin.math.sqrt

class AlarmRingActivity   : AppCompatActivity() {

    lateinit var binding: ActivityAlarmRingBinding
    private lateinit var sensorManager: SensorManager
    private lateinit var accelerometer: Sensor
    private lateinit var sensorEventListener: SensorEventListener
    var accelerometerPreviousVal = 0.0

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlarmRingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = ""


        wakeScreen()
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

        // accelerometer
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        var n = 0
        sensorEventListener = object : SensorEventListener{
            override fun onSensorChanged(event: SensorEvent?) {
                val x: Float = event!!.values[0]
                val y: Float = event.values[1]
                val z: Float = event.values[2]

                val accelerometerCurrentVal = sqrt((x * x + y * y + z * z).toDouble())

                var changeInAcceleration = abs(accelerometerCurrentVal - accelerometerPreviousVal)
                accelerometerPreviousVal = accelerometerCurrentVal

                // shake phone to turn off alarm
                if (changeInAcceleration > 10f) {
                    n += 1
                    if(n > 200)
                        turnOffAlarm(ring, vibe)
                }

            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
                true
            }

        }


        binding.btnCancelAlarm.setOnClickListener {
            turnOffAlarm(ring, vibe)
        }

        binding.btnSnoozeAlarm.setOnClickListener {
            val alarmId = Random().nextInt(Integer.MAX_VALUE)
            val calendar: Calendar = Calendar.getInstance()
            val snoozeMins = 10
            calendar.timeInMillis = System.currentTimeMillis()
            calendar.add(Calendar.MINUTE, snoozeMins) // TODO: set snooze length options
            val alarm = AlarmManager(
                alarmId,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                started = true,
                recurring = false
            )
            Toast.makeText(this, "Alarm snoozed for $snoozeMins minutes", Toast.LENGTH_SHORT).show()
            turnOffAlarm(ring, vibe)
            alarm.schedule(this)
        }
    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(sensorEventListener, accelerometer, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(sensorEventListener)
    }

    private fun wakeScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

            setTurnScreenOn(true)
            setShowWhenLocked(true)

            (getSystemService(KeyguardManager::class.java) as KeyguardManager).requestDismissKeyguard(
                this,
                object : KeyguardManager.KeyguardDismissCallback() {
                    override fun onDismissCancelled() {

                    }

                    override fun onDismissError() {

                    }

                    override fun onDismissSucceeded() {

                    }
                }
            )
        } else {
            window.addFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN
                        or WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                        or WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                        or WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                        or WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
            )
        }
    }

    private fun turnOffAlarm(ring: MediaPlayer, vibe:Vibrator){
        ring.stop()
        vibe.cancel()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}