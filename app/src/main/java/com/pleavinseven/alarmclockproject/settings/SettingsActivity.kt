package com.pleavinseven.alarmclockproject.settings

import android.content.Context
import android.content.Context.VIBRATOR_SERVICE
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.*
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.pleavinseven.alarmclockproject.R
import com.pleavinseven.alarmclockproject.databinding.ActivitySettingsBinding


class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportFragmentManager.beginTransaction()
            .replace(R.id.settings_container, SettingsFragment())
            .commit()

        // set action bar, status bar and nav to fit theme
        backGroundColour()
    }

    private fun backGroundColour() {
        val actionBar = supportActionBar
        actionBar?.title = ""
        actionBar?.elevation = 0F
        WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS
        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        val nightModeFlags: Int = this.resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK
        when (nightModeFlags) {
            Configuration.UI_MODE_NIGHT_YES -> {
                window.setBackgroundDrawableResource(R.drawable.dark_gradient_background)
                window.statusBarColor = Color.parseColor("#133936")
                actionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#133936")))
                window.navigationBarColor = Color.parseColor("#715c54")
            }
            Configuration.UI_MODE_NIGHT_NO -> {
                window.setBackgroundDrawableResource(R.drawable.gradient_background)
                window.statusBarColor = Color.parseColor("#248e8a")
                actionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#248e8a")))
                window.navigationBarColor = Color.parseColor("#e3b6a2")
            }
            Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                window.setBackgroundDrawableResource(R.drawable.gradient_background)
                window.statusBarColor = Color.parseColor("#248e8a")
                actionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#248e8a")))
                window.navigationBarColor = Color.parseColor("#e3b6a2")
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                startActivity(Intent(this, SettingsActivity::class.java))
                true
            }
            else -> false
        }
    }
}

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
        val vibe =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager =
                    activity?.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
                vibratorManager.defaultVibrator
            } else {
                activity?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            }

        sharedPreferences = context?.getSharedPreferences("nightModePrefs", Context.MODE_PRIVATE)!!

        // dark mode on off switch
        val spDarkMode = findPreference<SwitchPreference>("sp_dark_mode")
        spDarkMode!!.setOnPreferenceClickListener {
            if (spDarkMode.isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                saveNightModeState(true)
                setSwitch(spDarkMode, true)

            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                saveNightModeState(false)
                setSwitch(spDarkMode, false)
            }
            true
        }

        val spVibrate = findPreference<SwitchPreference>("sp_vibrate_on_off")
        spVibrate!!.setOnPreferenceClickListener {
            if (spVibrate.isChecked) {
                vibe.vibrate(VibrationEffect.createOneShot(200, 1))
                setSwitch(spDarkMode, true)
            } else {
                setSwitch(spDarkMode, false)
            }
            true
        }


    }

    private fun saveNightModeState(Night: Boolean) {
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        if (Night){
            editor.putBoolean("isNight", true)
        } else {
            editor.putBoolean("isNight", false)
        }
        editor.apply()
    }

    private fun setSwitch(switch: SwitchPreference, isChecked: Boolean) {
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putBoolean(switch.key, isChecked)
        editor.apply()
    }
}