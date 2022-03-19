package com.pleavinseven.alarmclockproject.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.pleavinseven.alarmclockproject.data.model.Alarm


@Database(entities = [Alarm::class], version = 2)
abstract class AlarmsDatabase : RoomDatabase() {

    abstract fun alarmDao(): AlarmDao

    companion object {
        @Volatile
        var INSTANCE: AlarmsDatabase? = null

        fun getDatabase(context: Context): AlarmsDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AlarmsDatabase::class.java,
                    "alarm_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                return instance
            }
        }
    }
}
