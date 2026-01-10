package io.droidevs.counterapp.ui.settings

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import io.droidevs.counterapp.R
import io.droidevs.counterapp.data.Theme
import io.droidevs.counterapp.ui.vm.DisplayPreferencesViewModel
import kotlinx.coroutines.launch

class DisplayPreferencesFragment : PreferenceFragmentCompat() {

    private val viewModel: DisplayPreferencesViewModel by viewModels()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.display_preferences, rootKey)

        val themePref = findPreference<ListPreference>("theme")
        val hideControlsPref = findPreference<SwitchPreferenceCompat>("hide_controls")
        val hideLastUpdatePref = findPreference<SwitchPreferenceCompat>("hide_last_update")
        val keepScreenOnPref = findPreference<SwitchPreferenceCompat>("keep_screen_on")
        val showLabelsPref = findPreference<SwitchPreferenceCompat>("show_labels")

        // Observe ViewModel → UI (two-way binding)
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.theme.collect { theme ->
                    themePref?.value = when (theme) {
                        Theme.SYSTEM -> "system"
                        Theme.LIGHT  -> "light"
                        Theme.DARK   -> "dark"
                        Theme.AUTO -> "auto"
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.hideControls.collect { hide ->
                    hideControlsPref?.isChecked = hide
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.hideLastUpdate.collect { hide ->
                    hideLastUpdatePref?.isChecked = hide
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.keepScreenOn.collect { keep ->
                    keepScreenOnPref?.isChecked = keep
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.showLabels.collect { show ->
                    showLabelsPref?.isChecked = show
                }
            }
        }

        // User changes → ViewModel
        themePref?.setOnPreferenceChangeListener { _, newValue ->
            viewModel.setTheme(Theme.valueOf(newValue as String))
            true
        }

        hideControlsPref?.setOnPreferenceChangeListener { _, newValue ->
            viewModel.setHideControls(newValue as Boolean)
            true
        }

        hideLastUpdatePref?.setOnPreferenceChangeListener { _, newValue ->
            viewModel.setHideLastUpdate(newValue as Boolean)
            true
        }

        keepScreenOnPref?.setOnPreferenceChangeListener { _, newValue ->
            viewModel.setKeepScreenOn(newValue as Boolean)
            true
        }

        showLabelsPref?.setOnPreferenceChangeListener { _, newValue ->
            viewModel.setShowLabels(newValue as Boolean)
            true
        }
    }
}