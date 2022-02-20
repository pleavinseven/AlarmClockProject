package com.pleavinseven.alarmclockproject.util

import android.os.Build
import android.widget.TimePicker

class TimePickerUtil {

    fun getTimePickerHour(timePicker: TimePicker): Int {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return timePicker.getHour();
        } else {
            return timePicker.getCurrentHour();
        }
    }

    public fun getTimePickerMinute(timePicker: TimePicker): Int {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return timePicker.getMinute();
        } else {
            return timePicker.getCurrentMinute();
        }
    }
}
