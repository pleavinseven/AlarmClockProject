package com.pleavinseven.alarmclockproject.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.pleavinseven.alarmclockproject.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
        sharedPreferences = context?.getSharedPreferences("nightModePrefs", Context.MODE_PRIVATE)!!
        val spDarkMode = findPreference<SwitchPreference>("sp_dark_mode")!!
        setSwitch(spDarkMode)
        setDarkMode(spDarkMode)
    }

    private fun setDarkMode(switchPref: SwitchPreference) {
        switchPref.setOnPreferenceClickListener {
            when (switchPref.isChecked) {
                true -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                false -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            saveNightModeState(switchPref.isChecked)
            setSwitch(switchPref)
            true
        }
    }

    private fun saveNightModeState(night: Boolean) {
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putBoolean("isNight", night)
        editor.apply()
    }

    private fun setSwitch(switchPref: SwitchPreference) {
        val isChecked = sharedPreferences.getBoolean("isNight", false)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putBoolean(switchPref.key, isChecked)
        editor.apply()
    }
}