package com.pleavinseven.alarmclockproject.service


import android.app.Service
import android.content.Intent
import android.content.Context
import android.media.MediaPlayer
import android.os.IBinder
import android.os.Vibrator
import com.pleavinseven.alarmclockproject.R


class AlarmService : Service() {
    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    override fun onCreate() {
        super.onCreate()
        val ring = MediaPlayer.create(this, R.raw.finch)
        ring.isLooping = true
        ring.start()
    }

}