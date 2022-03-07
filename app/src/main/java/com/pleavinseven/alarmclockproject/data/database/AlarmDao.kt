package com.pleavinseven.alarmclockproject.data.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.pleavinseven.alarmclockproject.data.model.Alarm


@Dao
interface AlarmDao {

    @Insert()
    suspend fun addAlarm(alarm: Alarm)

    @Query("SELECT * FROM alarm_table ORDER BY  id ASC")
    fun readAlarmData(): LiveData<List<Alarm>>

    @Update
    suspend fun updateAlarm(alarm: Alarm)

    @Delete
    suspend fun deleteAlarm(alarm: Alarm)
}