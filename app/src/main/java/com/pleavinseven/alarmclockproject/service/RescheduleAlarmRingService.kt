package com.pleavinseven.alarmclockproject.service


import android.content.Intent
import android.os.IBinder
import androidx.lifecycle.LifecycleService
import com.pleavinseven.alarmclockproject.alarmmanager.AlarmManager
import com.pleavinseven.alarmclockproject.data.database.AlarmsDatabase
import com.pleavinseven.alarmclockproject.data.model.Alarm
import com.pleavinseven.alarmclockproject.data.repository.AlarmRepository

class RescheduleAlarmRingService : LifecycleService() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        val alarmDao = AlarmsDatabase.getDatabase(application).alarmDao()
        val alarmRepository = AlarmRepository(alarmDao)
        val alarms = alarmRepository.readAlarmData as List<Alarm>
        for (alarm in alarms) {
            if (alarm.started) {
                val alarmManager = AlarmManager(
                    alarm.id,
                    alarm.hour,
                    alarm.minute,
                    true,
                    alarm.repeat
                )
                alarmManager.schedule(applicationContext)
            }
        }
        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        super.onBind(intent)
        return null
    }
}