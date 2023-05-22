package com.pleavinseven.alarmclockproject.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.pleavinseven.alarmclockproject.R
import com.pleavinseven.alarmclockproject.Theme.ThemeViewModel

class SettingsFragment : PreferenceFragmentCompat() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var themeViewModel: ThemeViewModel

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
        themeViewModel = ViewModelProvider(this)[ThemeViewModel::class.java]
        sharedPreferences = context?.getSharedPreferences("nightModePrefs", Context.MODE_PRIVATE)!!
        val spDarkMode = findPreference<SwitchPreference>("sp_dark_mode")!!
        setSwitch(spDarkMode)
        setDarkMode(spDarkMode)
    }

    private fun setDarkMode(switchPref: SwitchPreference) {
        switchPref.setOnPreferenceClickListener {
            when (switchPref.isChecked) {
                true -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    themeViewModel.updateNightMode(true)
                }

                false -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    themeViewModel.updateNightMode(false)
                }
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