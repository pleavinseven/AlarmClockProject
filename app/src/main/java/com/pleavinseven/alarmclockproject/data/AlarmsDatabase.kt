package com.pleavinseven.alarmclockproject.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Alarm::class], version = 1)
abstract class AlarmsDatabase: RoomDatabase() {

        abstract fun alarmDao(): AlarmDao
}