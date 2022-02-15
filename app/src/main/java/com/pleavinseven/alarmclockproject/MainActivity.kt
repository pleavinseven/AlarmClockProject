package com.pleavinseven.alarmclockproject

import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity
import com.pleavinseven.alarmclockproject.databinding.ActivityMainBinding
import com.pleavinseven.alarmclockproject.settings.SettingsActivity

class MainActivity : AppCompatActivity(), TimePickerDialog.OnTimeSetListener {

    lateinit var binding: ActivityMainBinding
    lateinit var time: String
    var hour = 0
    var minute = 0



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = ""
        var button:String = "" // pass this to onTimeSet

//        TODO: set a scroll view here and set btn_new_alarm to create new xml data


        binding.btnSetAlarm1.setOnClickListener {
                setTime(binding.btnSetAlarm1)
        }
        binding.btnSetAlarm2.setOnClickListener {
                setTime(binding.btnSetAlarm1)
        }
        binding.btnSetAlarm3.setOnClickListener {
                setTime(binding.btnSetAlarm1)
        }
    }


    private fun setTime(button: Button) {
        //pick a better theme/create a custom theme
        TimePickerDialog(
            this,
            android.R.style.Theme_Material_Dialog_NoActionBar, //TODO: set in drawable, so its changeable
            this,
            hour,
            minute,
            true
        ).show()
    }

    override fun onTimeSet(view: TimePicker?, hour: Int, minute: Int) {
        if (minute > 9)
            time = "$hour:$minute"
        else
            time = "$hour:0$minute"
        binding.tvNextAlarm.text = time
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.action_settings -> {
                startActivity(Intent(this, SettingsActivity::class.java))
                true
            }
            else -> false
        }
    }
}




// text to speech? choose your own morning message --> api
// barcode scanner?
// spotify api? --> probably not
// weather? --> api
//