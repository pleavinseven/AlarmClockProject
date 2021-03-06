package com.pleavinseven.alarmclockproject.fragments

import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.*
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.pleavinseven.alarmclockproject.alarmmanager.AlarmManager
import com.pleavinseven.alarmclockproject.databinding.FragmentShakeAlarmRingBinding
import com.pleavinseven.alarmclockproject.service.AlarmRingService
import com.pleavinseven.alarmclockproject.R.id.alarm_bubble
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentShakeAlarmRingBinding.inflate(inflater, container, false)

        val prefs = PreferenceManager.getDefaultSharedPreferences(requireContext())

        wakeScreen()
        setBubble(true)
        setCurrentTimeText()

        val vibe =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager =
                    activity?.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
                vibratorManager.defaultVibrator
            } else {
                activity?.getSystemService(AppCompatActivity.VIBRATOR_SERVICE) as Vibrator
            }

        if (prefs.getBoolean("sp_vibrate_on_off", true)) {
            vibeOn = true
            vibe.vibrate(VibrationEffect.createWaveform(longArrayOf(200, 1000, 500, 500), 0))
        }

        // accelerometer
        sensorManager =
            activity?.getSystemService(AppCompatActivity.SENSOR_SERVICE) as SensorManager
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

                val changeInAcceleration =
                    abs(accelerometerCurrentVal - accelerometerPreviousVal)
                accelerometerPreviousVal = accelerometerCurrentVal

                // shake phone to turn off alarm
                if (changeInAcceleration > 10f) {
                    n += 1
                    if (n > 200) {
                        turnOffAlarm(vibe)
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
                    override fun onDismissCancelled() {}

                    override fun onDismissError() {}

                    override fun onDismissSucceeded() {}
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

    private fun setBubble(
        isLeftVisible: Boolean,
    ) {
        alarm_bubble.apply {
            binding.alarmBubble.visibility = if (isLeftVisible) View.VISIBLE else View.GONE
        }
    }

    fun setCurrentTimeText(){
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        val formattedTime = current.format(formatter)
        binding.currentTime.text = formattedTime
    }
}