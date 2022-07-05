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

        setDarkMode()
        setVibrate()
    }

    private fun setVibrate() {
        val vibe =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager =
                    activity?.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
                vibratorManager.defaultVibrator
            } else {
                activity?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            }
        val spVibrate = findPreference<SwitchPreference>("sp_vibrate_on_off")
        spVibrate!!.setOnPreferenceClickListener {
            if (spVibrate.isChecked) {
                vibe.vibrate(VibrationEffect.createOneShot(200, 1))
                setSwitch(spVibrate, true)
            } else {
                setSwitch(spVibrate, false)
            }
            true
        }
    }

    private fun setDarkMode() {
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