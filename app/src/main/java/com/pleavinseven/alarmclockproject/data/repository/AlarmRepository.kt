package com.pleavinseven.alarmclockproject.data.repository

import androidx.lifecycle.LiveData
import com.pleavinseven.alarmclockproject.data.model.Alarm
import com.pleavinseven.alarmclockproject.data.database.AlarmDao

class AlarmRepository(private val alarmDao: AlarmDao) {

    val readAlarmData: LiveData<List<Alarm>> = alarmDao.readAlarmData()

    suspend fun addAlarm(alarm: Alarm) {
        alarmDao.addAlarm(alarm)
    }

    fun updateAlarm(alarm: Alarm) {
        alarmDao.updateAlarm(alarm)
    }


}