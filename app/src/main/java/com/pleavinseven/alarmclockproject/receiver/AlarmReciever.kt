package com.pleavinseven.alarmclockproject.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.pleavinseven.alarmclockproject.AlarmRingActivity


class AlarmReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val intent = Intent(context, AlarmRingActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context!!.startActivity(intent)
    }


}