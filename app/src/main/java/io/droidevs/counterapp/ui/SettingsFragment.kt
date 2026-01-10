package io.droidevs.counterapp.ui

import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import io.droidevs.counterapp.R

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.preference.CheckBoxPreference
import androidx.preference.ListPreference
import androidx.preference.PreferenceCategory
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.droidevs.counterapp.CounterApp
import io.droidevs.counterapp.data.SettingKeys
import io.droidevs.counterapp.ui.vm.SettingsViewModel
import io.droidevs.counterapp.ui.vm.factories.SettingsViewModelFactory
import kotlinx.coroutines.launch

class SettingsFragment : PreferenceFragmentCompat() , Preference.OnPreferenceChangeListener {


    private val viewModel: SettingsViewModel by viewModels {
        SettingsViewModelFactory(
           repo = (requireContext().applicationContext as CounterApp).settingsRepository
        )
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val rv = view.findViewById<RecyclerView>(
            androidx.preference.R.id.recycler_view
        )

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.main_menu, menu)
                menu.findItem(R.id.menuSettings)?.isVisible = false
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return false
            }

        }, viewLifecycleOwner, Lifecycle.State.RESUMED)


        rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(rv: RecyclerView, dx: Int, dy: Int) {
                updateToolbarTitle(rv)
            }
        })

    }

    private fun updateToolbarTitle(rv: RecyclerView) {
        val layoutManager = rv.layoutManager as LinearLayoutManager
        val position = layoutManager.findFirstVisibleItemPosition()

        if (position == RecyclerView.NO_POSITION) {
            setTitle(getString(R.string.settings))
            return
        }
        when(position) {
            in 1..4 -> setTitle(getString(R.string.settings_controls))
            in 5..7 -> setTitle(getString(R.string.settings_display))
            else -> setTitle(getString(R.string.settings_other))
        }

    }


    private fun setTitle(title: String) {
        Log.i(TAG, "Setting title to $title")
        (requireActivity() as AppCompatActivity).supportActionBar
            ?.title = title
    }

    override fun onResume() {
        super.onResume()
        //setHasOptionsMenu(true) // deprecated
    }

    override fun onPause() {
        super.onPause()
        requireActivity().invalidateOptionsMenu()
    }

    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        //menu.findItem(R.id.menuSettings)?.isVisible = false
    }

    private val appVersion: String
        get() = try {
            requireContext().packageManager
                .getPackageInfo(requireContext().packageName, 0)
                .versionName ?: getString(R.string.unknown_version)
        } catch (e: Exception) {
            getString(R.string.unknown_version)
        }


    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        try {
            bindBooleans()
            bindTheme()
            bindActions()
            bindVersion()

        } catch (e: NullPointerException) {
            Log.e(TAG, "Unable to retrieve one of the preferences", e)
        }

    }

    private fun bindVersion() {
        findPreference<Preference>(KEY_VERSION)?.summary = appVersion
    }

    private fun bindActions() {

        findPreference<Preference>(KEY_REMOVE_COUNTERS)
            ?.setOnPreferenceClickListener {
                showWipeDialog()
                true
            }

        findPreference<Preference>(KEY_EXPORT_COUNTERS)
            ?.setOnPreferenceClickListener {
                viewModel.exportCounters()
                true
            }

        findPreference<Preference>(KEY_HOMEPAGE)
            ?.setOnPreferenceClickListener {
                openUrl("https://github.com/droidevs/counter-app")
                true
            }

        findPreference<Preference>(KEY_TIP)
            ?.setOnPreferenceClickListener {
                openUrl("https://ko-fi.com/mouadoumous3")
                true
            }
    }

    private fun bindTheme() {
        findPreference<ListPreference>(SettingKeys.THEME.key)?.apply {
            value = viewModel.theme.value
            setOnPreferenceChangeListener { _, newValue ->
                viewModel.setTheme(newValue.toString())
                true
            }
        }
    }


    private fun bindBooleans() {

        bindCheckListener(SettingKeys.HIDE_CONTROLS.key) {
            viewModel.setHideControls(it)
        }

        bindCheckListener(SettingKeys.HIDE_LAST_UPDATE.key) {
            viewModel.setHideLastUpdate(it)
        }
        bindCheckListener(SettingKeys.SOUNDS_ON.key) {
            viewModel.setSoundsOn(it)
        }

        bindCheckListener(SettingKeys.VIBRATION_ON.key) {
            viewModel.setVibrationOn(it)
        }
        bindCheckListener(SettingKeys.HARDWARE_BTN_CONTROL_ON.key) {
            viewModel.setHardControlOn(it)
        }

        bindCheckListener(SettingKeys.LABEL_CONTROL_ON.key) {
            viewModel.setLabelControlOn(it)
        }

        bindCheckListener(SettingKeys.KEEP_SCREEN_ON.key) {
            viewModel.setKeepScreenOn(it)
        }


        lifecycleScope.launch {
            viewModel.soundsOn.collect {
                bindCheckState(SettingKeys.SOUNDS_ON.key, it)
            }
        }

        lifecycleScope.launch {
            viewModel.vibrationOn.collect {
                bindCheckState(SettingKeys.VIBRATION_ON.key, it)
            }
        }

        lifecycleScope.launch {
            viewModel.hardControlOn.collect {
                bindCheckState(SettingKeys.HARDWARE_BTN_CONTROL_ON.key, it)
            }
        }

        lifecycleScope.launch {
            viewModel.labelControlOn.collect {
                bindCheckState(SettingKeys.LABEL_CONTROL_ON.key, it)
            }
        }

        lifecycleScope.launch {
            viewModel.keepScreenOn.collect {
                bindCheckState(SettingKeys.KEEP_SCREEN_ON.key, it)
            }
        }

        lifecycleScope.launch {
            viewModel.hideControls.collect {
                bindCheckState(SettingKeys.HIDE_CONTROLS.key, it)
            }
        }

        lifecycleScope.launch {
            viewModel.hideLastUpdate.collect {
                bindCheckState(SettingKeys.HIDE_LAST_UPDATE.key, it)
            }
        }
    }


        fun bindCheckListener(
            key: String,
            onChange: (Boolean) -> Unit
        ) {
            findPreference<CheckBoxPreference>(key)?.setOnPreferenceChangeListener { _, newValue ->
                onChange.invoke(newValue as Boolean)
                true
            }
        }

        fun bindCheckState(
            key: String,
            state: Boolean
        ) {
            findPreference<CheckBoxPreference>(key)?.apply {
                isChecked = state ?: false
            }
        }

        /* -------------------- Preference Change -------------------- */

        override fun onPreferenceChange(preference: Preference, newValue: Any): Boolean {

            return false
        }

        /* -------------------- Helpers -------------------- */


        private fun openUrl(url: String) {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
        }

        private fun showWipeDialog() {
            // TODO implement a confirmation dialog
        }

        private fun export() {
            // TODO "implement export logic"
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