package com.pleavinseven.alarmclockproject.data

import androidx.lifecycle.LiveData
import androidx.room.*


// TEST: TODO: update

@Dao
interface AlarmDao {

    @Insert()
    fun addAlarm(alarm: Alarm)

    @Query("SELECT * FROM alarm_table ORDER BY  id ASC")
    fun readAlarmData(): LiveData<List<Alarm>>

    @Update
    fun updateAlarm(alarm: Alarm)

    @Delete
    fun deleteAlarm(alarm: Alarm)
}