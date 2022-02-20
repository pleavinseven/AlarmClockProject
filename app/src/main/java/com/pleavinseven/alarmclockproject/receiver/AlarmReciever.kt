package com.pleavinseven.alarmclockproject.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast

class AlarmReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
    val got = intent?.getBooleanExtra("recurring", false) ?: println("working")
        Toast.makeText(context, "got it", Toast.LENGTH_LONG).show()
        Log.d("TAG", "onReceive: broadcast received")
    }


}