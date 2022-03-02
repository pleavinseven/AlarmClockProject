package com.pleavinseven.alarmclockproject.util

import android.os.Build
import android.widget.TimePicker

class TimePickerUtil {

    fun getTimePickerHour(timePicker: TimePicker): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            timePicker.hour;
        } else {
            timePicker.currentHour;
        }
    }

    fun getTimePickerMinute(timePicker: TimePicker): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            timePicker.minute;
        } else {
            timePicker.currentMinute;
        }
    }
}
