package io.droidevs.counterapp.ui

import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import io.droidevs.counterapp.R
import io.droidevs.counterapp.data.PrefKeys

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.preference.ListPreference
import io.droidevs.counterapp.data.Themes

class SettingsFragment : PreferenceFragmentCompat() , Preference.OnPreferenceChangeListener{

    private lateinit var sharedPrefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    private val appVersion : String
        get() = try {
            requireContext().packageManager
                .getPackageInfo(requireContext().packageName, 0)
                .versionName ?:getString(R.string.unknown_version)
        } catch (e: Exception) {
            getString(R.string.unknown_version)
        }


    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(requireContext())

        try {

            setupThemePreference()
            setupVersionPreference()
            setupActions()

        } catch (e: NullPointerException) {
            Log.e(TAG, "Unable to retrieve one of the preferences", e)
        }

    }

    /* -------------------- Theme -------------------- */

    private fun setupThemePreference() {
        val themePref = findPreference<ListPreference>(PrefKeys.THEME.key)

        themePref?.summary =
            getString(Themes.getCurrent(sharedPrefs).labelId)

        themePref?.onPreferenceChangeListener = this
    }

    /* -------------------- Version -------------------- */

    private fun setupVersionPreference() {
        findPreference<Preference>(KEY_VERSION)?.summary = appVersion
    }

    /* -------------------- Action Preferences -------------------- */

    private fun setupActions() {

        findPreference<Preference>(KEY_REMOVE_COUNTERS)
            ?.setOnPreferenceClickListener {
                showWipeDialog()
                true
            }

        findPreference<Preference>(KEY_EXPORT_COUNTERS)
            ?.setOnPreferenceClickListener {
                export()
                true
            }

        findPreference<Preference>(KEY_HOMEPAGE)
            ?.setOnPreferenceClickListener {
                openUrl("https://counter.roman.zone")
                true
            }

        findPreference<Preference>(KEY_TIP)
            ?.setOnPreferenceClickListener {
                openUrl("https://counter.roman.zone/tip")
                true
            }
    }

    /* -------------------- Preference Change -------------------- */

    override fun onPreferenceChange(preference: Preference, newValue: Any): Boolean {
        if (preference.key == PrefKeys.THEME.key) {
            Themes.initCurrentTheme(sharedPrefs = sharedPrefs)
            preference.summary =
                getString(Themes.getCurrent(sharedPrefs).labelId)
        }
        return true
    }

    /* -------------------- Helpers -------------------- */


    private fun openUrl(url: String) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }

    private fun showWipeDialog() {
        TODO("implement a confirmation dialog")
    }

    private fun export() {
        TODO("implement export logic")
    }

    companion object {
        private val TAG: String = SettingsFragment::class.java.simpleName

        const val KEY_REMOVE_COUNTERS = "removeCounters"
        const val KEY_EXPORT_COUNTERS = "exportCounters"
        const val KEY_HOMEPAGE = "homepage"
        const val KEY_TIP = "tip"
        const val KEY_VERSION = "version"
    }
}