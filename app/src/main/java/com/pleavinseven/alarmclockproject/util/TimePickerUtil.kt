package com.pleavinseven.alarmclockproject.util

import android.os.Build
import android.widget.TimePicker

class TimePickerUtil {

    fun getTimePickerHour(timePicker: TimePicker): Int {
        return timePicker.hour
    }

    fun getTimePickerMinute(timePicker: TimePicker): Int {
        return timePicker.minute
    }
}
