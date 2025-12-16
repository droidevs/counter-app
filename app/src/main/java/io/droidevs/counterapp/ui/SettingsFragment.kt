package io.droidevs.counterapp.ui

import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import io.droidevs.counterapp.R
import io.droidevs.counterapp.data.PrefKeys

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import android.widget.Toast
import io.droidevs.counterapp.CounterApp
import io.droidevs.counterapp.data.Themes.Companion.getCurrent
import java.io.IOException

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {

            val sharedPrefs =
                PreferenceManager.getDefaultSharedPreferences(this.requireActivity())
            findPreference<Preference>(PrefKeys.THEME.key)
                ?.setSummary(
                    resources.getString(getCurrent(sharedPrefs).labelId)
                )


        } catch (e: NullPointerException) {
            Log.e(TAG, "Unable to retrieve one of the preferences", e)
        }
    }


    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }

    companion object {
        private val TAG: String = SettingsFragment::class.java.simpleName

    }
}