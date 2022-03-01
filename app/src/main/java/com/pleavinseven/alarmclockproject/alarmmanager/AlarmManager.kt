package com.pleavinseven.alarmclockproject.alarmmanager

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast
import com.pleavinseven.alarmclockproject.receiver.AlarmReceiver
import java.util.*


class AlarmManager(
    private val alarmId: Int,
    private val hour: Int,
    private val minute: Int,
    private val title: String,
    var started: Boolean,
    val recurring: Boolean
) {

    fun schedule(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        intent.putExtra("recurring", recurring)
        intent.putExtra("title", title)
        val alarmPendingIntent = PendingIntent.getBroadcast(
            context,
            alarmId, intent, if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            } else {
                PendingIntent.FLAG_UPDATE_CURRENT
            }
        )
        val calendar: Calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)

        //if alarm time has already passed, increment day by 1
        if (calendar.timeInMillis <= System.currentTimeMillis()) {
            calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 1)
        }

        val toastTime = if (minute < 10) {
            "$hour:0$minute"
        } else {
            "$hour:$minute"
        }
        if (!recurring) {
            Toast.makeText(
                context,
                "One Time Alarm $title set for $toastTime",
                Toast.LENGTH_LONG
            ).show()
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                alarmPendingIntent
            )
        } else {
            Toast.makeText(context, "Alarm $title set daily at $toastTime", Toast.LENGTH_LONG)
                .show()
            val runDaily = (24 * 60 * 60 * 1000).toLong()
            alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                runDaily,
                alarmPendingIntent
            )
        }
        started = true
    }
}