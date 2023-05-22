package com.pleavinseven.alarmclockproject.data.viewmodel

import android.app.Application
import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import androidx.navigation.Navigation
import com.pleavinseven.alarmclockproject.R
import com.pleavinseven.alarmclockproject.data.database.AlarmsDatabase
import com.pleavinseven.alarmclockproject.data.model.Alarm
import com.pleavinseven.alarmclockproject.data.repository.AlarmRepository
import com.pleavinseven.alarmclockproject.fragments.HomeFragmentDirections
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

    fun update(alarm: Alarm) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateAlarm(alarm)
        }
    }

    fun delete(alarm: Alarm) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAlarm(alarm)
        }
    }

    fun handleShortClick(view: View, alarm: Alarm) {
        Navigation.findNavController(view)
            .navigate(HomeFragmentDirections.actionHomeFragmentToUpdateFragment(alarm))
    }

    fun handleLongClick(context: Context, alarm: Alarm) {
        deleteBuilderPopup(context, alarm)
    }

    private fun deleteBuilderPopup(context: Context, alarm: Alarm) {
        val deleteBuilder = AlertDialog.Builder(context, R.style.PopUpMenuStyle)
        deleteBuilder.setPositiveButton(R.string.delete_builder_delete) { _, _ ->
            delete(alarm)
            Toast.makeText(
                context,
                context.getString(R.string.delete_builder_alarm_deleted),
                Toast.LENGTH_SHORT
            )
                .show()
        }
        deleteBuilder.setNegativeButton(R.string.cancel_alarm) { _, _ ->
            // do nothing
        }
        deleteBuilder.setTitle(R.string.title_delete)
        deleteBuilder.create().show()
    }

}