package com.pleavinseven.alarmclockproject.fragments

import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.media.MediaPlayer
import android.os.*
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import androidx.preference.PreferenceManager
import com.pleavinseven.alarmclockproject.MainActivity
import com.pleavinseven.alarmclockproject.R
import com.pleavinseven.alarmclockproject.alarmmanager.AlarmManager
import com.pleavinseven.alarmclockproject.databinding.ActivityAlarmRingBinding
import com.pleavinseven.alarmclockproject.databinding.FragmentShakeAlarmRingBinding
import com.pleavinseven.alarmclockproject.databinding.FragmentUpdateBinding
import com.pleavinseven.alarmclockproject.service.AlarmRingService
import java.util.*
import kotlin.math.abs
import kotlin.math.sqrt


class ShakeAlarmRingFragment : Fragment() {

    private lateinit var sensorManager: SensorManager
    private lateinit var accelerometer: Sensor
    private lateinit var sensorEventListener: SensorEventListener
    var accelerometerPreviousVal = 0.0
    var vibeOn = false
    lateinit var binding: FragmentShakeAlarmRingBinding

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentShakeAlarmRingBinding.inflate(inflater, container, false)

        val prefs = PreferenceManager.getDefaultSharedPreferences(requireContext())

        when(resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK){
            Configuration.UI_MODE_NIGHT_YES -> container!!.setBackgroundResource(R.drawable.alarm_ring_dark_background)
            Configuration.UI_MODE_NIGHT_NO -> container!!.setBackgroundResource(R.drawable.alarm_ring_light_background)
        }

        wakeScreen()

        val vibe =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager =
                    activity?.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
                vibratorManager.defaultVibrator
            } else {
                activity?.getSystemService(AppCompatActivity.VIBRATOR_SERVICE) as Vibrator
            }

        if(prefs.getBoolean("sp_vibrate_on_off", true)) {
            vibeOn = true
            vibe.vibrate(VibrationEffect.createWaveform(longArrayOf(200, 1000, 500, 500), 0))
        }

        // accelerometer
        sensorManager = activity?.getSystemService(AppCompatActivity.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        var n = 0
        sensorEventListener = object : SensorEventListener {
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
                true
            }
            override fun onSensorChanged(event: SensorEvent?) {
                val x: Float = event!!.values[0]
                val y: Float = event.values[1]
                val z: Float = event.values[2]

                val accelerometerCurrentVal = sqrt((x * x + y * y + z * z).toDouble())

                var changeInAcceleration =
                    abs(accelerometerCurrentVal - accelerometerPreviousVal)
                accelerometerPreviousVal = accelerometerCurrentVal

                // shake phone to turn off alarm
                if (changeInAcceleration > 10f) {
                    n += 1
                    if (n > 200) {
                        // TODO: make a pop up
                        Toast.makeText(
                            requireContext(),
                            "buttons now work",
                            Toast.LENGTH_LONG
                        ).show()

                        binding.btnCancelAlarm.setOnClickListener {
                            turnOffAlarm(vibe)
                        }

                        binding.btnSnoozeAlarm.setOnClickListener {
                            snoozeAlarm(prefs)
                            turnOffAlarm(vibe)
                        }
                    }
                }
            }
        }

        return binding.root
    }
    private fun wakeScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

            activity?.setTurnScreenOn(true)
            activity?.setShowWhenLocked(true)

            (activity?.getSystemService(KeyguardManager::class.java) as KeyguardManager).requestDismissKeyguard(
                requireActivity(),
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
            activity?.window?.addFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN
                        or WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                        or WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                        or WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                        or WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
            )
        }
    }

    fun turnOffAlarm(vibe: Vibrator) {
        vibe.cancel()
        val intent = Intent(requireContext(), AlarmRingService::class.java)
        requireContext().stopService(intent)
    }

    fun snoozeAlarm(prefs: SharedPreferences){
        val alarmId = Random().nextInt(Integer.MAX_VALUE)
        val calendar: Calendar = Calendar.getInstance()
        val snoozeMins = prefs.getString("lp_snooze", "-1")!!.toInt()
        calendar.timeInMillis = System.currentTimeMillis()
        calendar.add(Calendar.MINUTE, snoozeMins)
        val alarm = AlarmManager(
            alarmId,
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            started = true,
            recurring = false,
        )
        Toast.makeText(
            requireContext(),
            "Alarm snoozed for $snoozeMins minutes",
            Toast.LENGTH_SHORT
        ).show()
        alarm.schedule(requireContext())
    }
}