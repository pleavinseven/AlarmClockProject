package com.pleavinseven.alarmclockproject.fragments

import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
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
import com.pleavinseven.alarmclockproject.databinding.FragmentAlarmRingBinding
import com.pleavinseven.alarmclockproject.databinding.FragmentUpdateBinding
import java.util.*


class AlarmRingFragment : Fragment() {

    lateinit var binding: FragmentAlarmRingBinding
    var vibeOn = false

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAlarmRingBinding.inflate(inflater, container, false)

        val prefs = PreferenceManager.getDefaultSharedPreferences(requireContext())


        wakeScreen()
        val ring = MediaPlayer.create(requireContext(), R.raw.finch)
        ring.isLooping = true

        ring.start()

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

        binding.btnCancelAlarm.setOnClickListener {
            turnOffAlarm(ring, vibe)
        }

        binding.btnSnoozeAlarm.setOnClickListener {
            snoozeAlarm(prefs)
            turnOffAlarm(ring, vibe)
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

    fun turnOffAlarm(ring: MediaPlayer, vibe: Vibrator) {
        ring.stop()
        if (vibeOn)
            vibe.cancel()
        val intent = Intent(requireContext(), MainActivity::class.java)
        startActivity(intent)
    }

    fun snoozeAlarm(prefs: SharedPreferences) {
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
            recurring = false
        )
        Toast.makeText(
            requireContext(),
            "Alarm snoozed for $snoozeMins minutes",
            Toast.LENGTH_SHORT
        ).show()
        alarm.schedule(requireContext())
    }
}
