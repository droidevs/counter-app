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
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.ListPreference
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.droidevs.counterapp.data.Themes

class SettingsFragment : PreferenceFragmentCompat() , Preference.OnPreferenceChangeListener{

    private lateinit var sharedPrefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rv = view.findViewById<RecyclerView>(
            androidx.preference.R.id.recycler_view
        )

        rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val lm = rv.layoutManager as LinearLayoutManager
                //Log.i(TAG, "First visible item position: ${lm.findFirstVisibleItemPosition()}")
                when(lm.findFirstVisibleItemPosition()) {
                    in 1..3 ->setTitle("Controls")
                    in 4..7 ->setTitle("Display")
                    else -> setTitle("Other")
                }

            }
        })
    }


    private fun setTitle(title: String) {
        Log.i(TAG, "Setting title to $title")
        (requireActivity() as AppCompatActivity).supportActionBar
            ?.title = title
    }

    override fun onResume() {
        super.onResume()
        setHasOptionsMenu(true) // deprecated
    }

    override fun onPause() {
        super.onPause()
        requireActivity().invalidateOptionsMenu()
    }

    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.findItem(R.id.menuSettings)?.isVisible = false
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
            getString(Themes.getCurrent(
                sharedPrefs = sharedPrefs
            ).labelId)

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
                openUrl("https://ko-fi.com/mouadoumous3")
                true
            }
    }

    /* -------------------- Preference Change -------------------- */

    override fun onPreferenceChange(preference: Preference, newValue: Any): Boolean {
        if (preference.key == PrefKeys.THEME.key) {
            Themes.initCurrentTheme(
                identifier = newValue.toString(),
                sharedPrefs = sharedPrefs)
            preference.summary =
                getString(Themes.getCurrent(
                    id = newValue.toString(),
                    sharedPrefs = sharedPrefs
                ).labelId)
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