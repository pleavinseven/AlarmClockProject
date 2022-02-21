package com.pleavinseven.alarmclockproject.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.util.Log
import android.widget.Toast
import com.pleavinseven.alarmclockproject.MainActivity
import com.pleavinseven.alarmclockproject.R

class AlarmReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
    val got = intent?.getBooleanExtra("recurring", false) ?: println("working")
        val ring = MediaPlayer.create(context, R.raw.finch)

        ring.start()
        Log.d("TAG", "onReceive: broadcast received")
    }


}