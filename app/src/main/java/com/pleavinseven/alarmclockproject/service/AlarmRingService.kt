package com.pleavinseven.alarmclockproject.service

import android.app.*
import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.os.*
import androidx.core.app.NotificationManagerCompat
import com.pleavinseven.alarmclockproject.AlarmRingActivity
import com.pleavinseven.alarmclockproject.MainActivity
import com.pleavinseven.alarmclockproject.R

class AlarmRingService : Service() {

    private var mPlayer: MediaPlayer? = null
    private val alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        startMediaPlayer()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        val vibrate = intent?.extras?.getBoolean("vibrate")
        val shake = intent?.extras?.getBoolean("shake")
        val snooze = intent?.extras?.getInt("snooze")

        val notificationIntent = Intent(this, AlarmRingActivity::class.java)
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        notificationIntent.putExtra("vibrate", vibrate)
        notificationIntent.putExtra("shake", shake)
        notificationIntent.putExtra("snooze", snooze)

        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE)

        val builder = Notification.Builder(this, "my channel")
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
        ringIntent.putExtra("vibrate", vibrate)
        ringIntent.putExtra("shake", shake)
        ringIntent.putExtra("snooze", snooze)

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

    private fun startMediaPlayer(){
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

    private fun createNotificationChannel() {
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