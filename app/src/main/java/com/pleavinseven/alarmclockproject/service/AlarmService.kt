package com.pleavinseven.alarmclockproject.service


import android.app.Service
import android.content.Intent
import android.content.Context
import android.media.MediaPlayer
import android.os.IBinder
import android.os.Vibrator
import android.widget.Toast
import com.pleavinseven.alarmclockproject.AlarmRingActivity
import com.pleavinseven.alarmclockproject.R


class AlarmService : Service() {
    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    override fun onCreate() {
        super.onCreate()

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val intent = Intent(this, AlarmRingActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        return START_STICKY
    }
}