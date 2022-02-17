package com.pleavinseven.alarmclockproject

import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.pleavinseven.alarmclockproject.databinding.ActivityMainBinding
import com.pleavinseven.alarmclockproject.settings.SettingsActivity
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = ""

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
        Calendar.getInstance().apply {
            //pick a better theme/create a custom theme
            TimePickerDialog(
                this@MainActivity,
                android.R.style.Theme_Material_Dialog_NoActionBar, //TODO: set in drawable, so its changeable
                {_, hour, minute ->
                    this.set(Calendar.HOUR_OF_DAY, hour)
                    this.set(Calendar.MINUTE, minute)
                },
                this.get(Calendar.HOUR_OF_DAY),
                this.get(Calendar.MINUTE),
                true
            ).show()
        }
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
