package com.pleavinseven.alarmclockproject.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AlarmViewModel(application: Application) : AndroidViewModel(application) {

    private val readAlarmData: LiveData<List<Alarm>>
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