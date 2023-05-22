package com.pleavinseven.alarmclockproject.Theme

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class ThemeViewModel(application: Application) : AndroidViewModel(application) {

    private val _nightMode = MutableLiveData<Boolean>()
    val nightMode: LiveData<Boolean> get() = _nightMode

    fun updateNightMode(isNightMode: Boolean) {
        _nightMode.value = isNightMode
    }

}