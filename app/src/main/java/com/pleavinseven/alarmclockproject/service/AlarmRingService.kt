package com.pleavinseven.alarmclockproject.service

import android.app.*
import android.content.Intent
import android.media.MediaPlayer
import android.os.*
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationManagerCompat
import androidx.preference.PreferenceManager
import com.pleavinseven.alarmclockproject.AlarmRingActivity
import com.pleavinseven.alarmclockproject.MainActivity
import com.pleavinseven.alarmclockproject.R

class AlarmRingService : Service() {

    private var mPlayer: MediaPlayer? = null


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        createNotificationChannel()
        mPlayer = MediaPlayer.create(this, R.raw.finch)
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
            .setContentTitle("Alarm")
            .setContentText("this is where the notification text goes")
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
            val importance = NotificationManager.IMPORTANCE_LOW
            val channelId = "my channel"
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}

//    fun actionNotification(){
//
//        val intent = Intent(this, DetailsActivity::class.java).apply {
//            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//        }
//        val pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)
//
//
//        var builder = NotificationCompat.Builder(this, "my channel")
//            .setSmallIcon(R.drawable.ic_launcher_background)
//            .setContentTitle("Bonjour")
//            //.setContentText("this is where the notification text goes")
//            .setStyle(NotificationCompat.BigTextStyle().bigText("can write a longer text here and it wont turn into ellipses... but will keep going instead"))
//            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//            .setContentIntent(pendingIntent)
//            .addAction(R.mipmap.ic_launcher_round, "click here", pendingIntent)
//            .addAction(R.mipmap.ic_launcher_round, "click now", pendingIntent)
//            .addAction(R.mipmap.ic_launcher_round, "click next", pendingIntent)
//
//        NotificationManagerCompat.from(this).notify(1, builder.build())
//
//    }
//
//    fun replyNotification(){
//        val KEY_REPLY = "key_reply"
//        val label = "label"
//        val remoteInput = androidx.core.app.RemoteInput.Builder(KEY_REPLY).run {
//            setLabel(label)
//            build()
//        }
//
//        val intent = Intent(this, DetailsActivity::class.java).apply {
//            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//        }
//        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0)
//        val messageIntent = PendingIntent.getBroadcast(applicationContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
//
//
//        val action = NotificationCompat.Action.Builder(R.mipmap.ic_launcher_round, label, messageIntent)
//            .addRemoteInput(remoteInput)
//            .build()
//
//        var builder = NotificationCompat.Builder(this, "my channel")
//            .setSmallIcon(R.drawable.ic_launcher_background)
//            .setContentTitle("Bonjour")
//            //.setContentText("this is where the notification text goes")
//            .setStyle(NotificationCompat.BigTextStyle().bigText("can write a longer text here and it wont turn into ellipses... but will keep going instead"))
//            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
////            .setContentIntent(pendingIntent)
//            .addAction(action)
//            .addAction(R.mipmap.ic_launcher_round, "click now", pendingIntent)
//            .addAction(R.mipmap.ic_launcher_round, "click next", pendingIntent)
//
//        NotificationManagerCompat.from(this).notify(1, builder.build())
//
//    }
//
//    fun progressNotification(){
//        var builder = NotificationCompat.Builder(this, "my channel")
//            .setSmallIcon(R.drawable.ic_launcher_background)
//            .setContentTitle("Download")
//            .setContentText("in progress...")
//            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//
//        val MAX = 100
//        val Current = 0
//        NotificationManagerCompat.from(this).apply {
//            builder.setProgress(MAX, Current, false)
//            notify(1, builder.build())
//
//            val t = Thread{
//                for(i in 0 .. 100) {
//                    builder.setProgress(MAX, i, false)
//                    Thread.sleep(50)
//                    notify(1, builder.build())
//                }
//            }
//
//            t.start()
//            t.join()
//
//            builder.setProgress(0, 0, false)
//                .setContentText("Download completed")
//            notify(1, builder.build())
//
//        }
//
////        NotificationManagerCompat.from(this).notify(1, builder.build())
//
//    }
//
//    fun expandableNotification(){
//        val image:Bitmap = BitmapFactory.decodeResource(resources, R.drawable.kitten_small)
//
//        var builder = NotificationCompat.Builder(this, "my channel")
//            .setSmallIcon(R.drawable.ic_launcher_background)
//            .setContentTitle("Titley bit")
//            .setContentText("more info")
//            .setLargeIcon(image)
//            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//            .setStyle(NotificationCompat.BigPictureStyle()
//                .bigPicture(image)
//                .bigLargeIcon(null))
//
//
//
//        NotificationManagerCompat.from(this).notify(1, builder.build())
//    }
//}