package com.pleavinseven.alarmclockproject.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.content.ContextCompat.startActivity
import com.pleavinseven.alarmclockproject.AlarmRingActivity
import com.pleavinseven.alarmclockproject.service.AlarmService


class AlarmReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
//        val intentService = Intent(context, AlarmService::class.java)
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            context!!.startForegroundService(intentService)
//        } else {
//            context!!.startService(intentService)
//        }
        val intent = Intent(context, AlarmRingActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context!!.startActivity(intent)
    }


}