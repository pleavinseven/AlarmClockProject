package com.pleavinseven.alarmclockproject.service

import android.app.*
import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.os.*
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationManagerCompat
import androidx.preference.PreferenceManager
import com.pleavinseven.alarmclockproject.AlarmRingActivity
import com.pleavinseven.alarmclockproject.MainActivity
import com.pleavinseven.alarmclockproject.R

class AlarmRingService : Service() {

    private var mPlayer: MediaPlayer? = null
    val alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        createNotificationChannel()
        mPlayer = MediaPlayer()
        mPlayer?.setAudioAttributes(
            AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_ALARM)
                .build()
        )
        mPlayer?.setDataSource(this, alarmUri)
        mPlayer?.prepare()
        mPlayer?.isLooping = true
        mPlayer?.start()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        val notificationIntent = Intent(this, AlarmRingActivity::class.java)
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)

        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0)

        var builder = Notification.Builder(this, "my channel")
        val notification = builder.setOngoing(true)
            .setSmallIcon(R.drawable.chaffinch_logo)
            .setContentTitle(applicationContext.getString(R.string.notification_title))
            .setContentText(applicationContext.getString(R.string.notification_content))
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(this).notify(1, builder.build())


        val ringIntent = Intent(this, AlarmRingActivity::class.java)
        ringIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(ringIntent)
        startForeground(1, notification)
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        mPlayer?.stop()
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "My Channel"
            val descriptionText = "short description"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channelId = "my channel"
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}