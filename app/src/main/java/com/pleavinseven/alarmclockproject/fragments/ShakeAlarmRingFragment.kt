package com.pleavinseven.alarmclockproject.fragments

import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
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
import androidx.appcompat.app.AppCompatActivity
import com.pleavinseven.alarmclockproject.databinding.FragmentShakeAlarmRingBinding
import com.pleavinseven.alarmclockproject.service.AlarmRingService
import com.pleavinseven.alarmclockproject.R.id.alarm_bubble
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.abs
import kotlin.math.sqrt


class ShakeAlarmRingFragment : Fragment(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private lateinit var vibe: Vibrator
    var accelerometerPreviousVal = 0.0
    var vibeOn = false
    var n = 0
    lateinit var binding: FragmentShakeAlarmRingBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentShakeAlarmRingBinding.inflate(inflater, container, false)

        val bundle = arguments
        val vibrate = bundle?.getBoolean("vibrate")

        val vibe =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager =
                    activity?.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
                vibratorManager.defaultVibrator
            } else {
                activity?.getSystemService(AppCompatActivity.VIBRATOR_SERVICE) as Vibrator
            }

        wakeScreen()
        setBubble(true)
        setCurrentTimeText()

        if (vibrate == true) {
            vibe.vibrate(VibrationEffect.createWaveform(longArrayOf(200, 1000, 500, 500), 0))
        }
        setSensorManager()

        return binding.root
    }

    private fun setSensorManager() {
        sensorManager =
            activity?.getSystemService(AppCompatActivity.SENSOR_SERVICE) as SensorManager
        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)?.also {
            sensorManager.registerListener(
                this,
                it,
                SensorManager.SENSOR_DELAY_FASTEST
            )
        }
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

    private fun turnOffAlarm(vibe: Vibrator) {
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

    private fun setCurrentTimeText() {
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        val formattedTime = current.format(formatter)
        binding.currentTime.text = formattedTime
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
            val x: Float = event.values[0]
            val y: Float = event.values[1]
            val z: Float = event.values[2]

            val accelerometerCurrentVal = sqrt((x * x + y * y + z * z).toDouble())

            val changeInAcceleration =
                abs(accelerometerCurrentVal - accelerometerPreviousVal)
            accelerometerPreviousVal = accelerometerCurrentVal

            // shake phone to turn off alarm
            if (changeInAcceleration > 12f) {
                n += 1
                if (n > 100) {
                    turnOffAlarm(vibe)

                }
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        return
    }

    override fun onDestroy() {
        sensorManager.unregisterListener(this)
        super.onDestroy()
    }
}