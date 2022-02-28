package com.pleavinseven.alarmclockproject.data

import androidx.lifecycle.LiveData

class AlarmRepository(private val alarmDao: AlarmDao) {

    val readAlarmData: LiveData<List<Alarm>> = alarmDao.readAlarmData()

    suspend fun addAlarm(alarm: Alarm) {
        alarmDao.addAlarm(alarm)
    }

    fun updateAlarm(alarm: Alarm) {
        alarmDao.updateAlarm(alarm)
    }

    fun deleteAlarm(alarm: Alarm) {
        alarmDao.deleteAlarm(alarm)
    }


}