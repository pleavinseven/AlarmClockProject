package com.pleavinseven.alarmclockproject.data.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.pleavinseven.alarmclockproject.data.database.Alarm
import com.pleavinseven.alarmclockproject.data.database.AlarmsDatabase
import com.pleavinseven.alarmclockproject.data.repository.AlarmRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class AlarmViewModel(application: Application) : AndroidViewModel(application) {

    val readAlarmData: LiveData<List<Alarm>>
    private val repository: AlarmRepository

    init {
        val alarmDao = AlarmsDatabase.getDatabase(application).alarmDao()
        repository = AlarmRepository(alarmDao)
        readAlarmData = repository.readAlarmData
    }

    fun addAlarm(alarm: Alarm) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addAlarm(alarm)
        }
    }
}