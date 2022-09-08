package com.pleavinseven.alarmclockproject.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.pleavinseven.alarmclockproject.service.AlarmRingService
import com.pleavinseven.alarmclockproject.service.RescheduleAlarmRingService
import kotlin.properties.Delegates


class AlarmReceiver : BroadcastReceiver() {

    var vibrate = false
    var shake = false
    var snooze = 0
    override fun onReceive(context: Context?, intent: Intent?) {

        if (intent != null) {
            vibrate = intent.extras?.getBoolean("vibrate")!!
            shake = intent.extras?.getBoolean("shake")!!
            snooze = intent.extras?.getInt("snooze")!!
            if (Intent.ACTION_BOOT_COMPLETED == intent.action) {
                startRescheduleAlarmRingService(context)
            } else {
                startAlarmRingService(context)
            }
        }
    }

    private fun startAlarmRingService(context: Context?) {
        val intent = Intent(context, AlarmRingService::class.java)
        intent.putExtra("vibrate", vibrate)
        intent.putExtra("snooze", snooze)
        context!!.startService(intent)
    }

    private fun startRescheduleAlarmRingService(context: Context?) {
        val intent = Intent(context, RescheduleAlarmRingService::class.java)
        context!!.startService(intent)
    }


}