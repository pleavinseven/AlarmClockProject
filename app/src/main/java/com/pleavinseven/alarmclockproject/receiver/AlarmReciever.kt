package com.pleavinseven.alarmclockproject.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.pleavinseven.alarmclockproject.service.AlarmRingService
import com.pleavinseven.alarmclockproject.service.RescheduleAlarmRingService


class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {

        if (intent != null) {
            if (Intent.ACTION_BOOT_COMPLETED == intent.action) {
                startRescheduleAlarmRingService(context)
            } else {
                startAlarmRingService(context)
            }
        }
    }

    private fun startAlarmRingService(context: Context?) {
        val intent = Intent(context, AlarmRingService::class.java)
        context!!.startService(intent)
    }

    private fun startRescheduleAlarmRingService(context: Context?) {
        val intent = Intent(context, RescheduleAlarmRingService::class.java)
        context!!.startService(intent)
    }


}