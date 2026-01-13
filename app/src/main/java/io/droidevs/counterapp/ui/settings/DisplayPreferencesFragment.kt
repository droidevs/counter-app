package io.droidevs.counterapp.ui.settings

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import io.droidevs.counterapp.R
import io.droidevs.counterapp.data.Theme
import io.droidevs.counterapp.ui.vm.DisplayPreferencesViewModel
import io.droidevs.counterapp.ui.vm.actions.DisplayPreferenceAction
import io.droidevs.counterapp.ui.vm.events.DisplayPreferenceEvent
import kotlinx.coroutines.launch


@AndroidEntryPoint
class DisplayPreferencesFragment : PreferenceFragmentCompat() {

    private val viewModel: DisplayPreferencesViewModel by viewModels()
    private var isInitializing = true

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.display_preferences, rootKey)

        val themePref = findPreference<ListPreference>("theme")
        val hideControlsPref = findPreference<SwitchPreferenceCompat>("hide_controls")
        val hideLastUpdatePref = findPreference<SwitchPreferenceCompat>("hide_last_update")
        val keepScreenOnPref = findPreference<SwitchPreferenceCompat>("keep_screen_on")
        val showLabelsPref = findPreference<SwitchPreferenceCompat>("show_labels")

        // Observe ViewModel UI State → update UI
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    if (isInitializing) {
                        // Set initial values without triggering listeners
                        themePref?.value = when (state.theme) {
                            Theme.SYSTEM -> "system"
                            Theme.LIGHT  -> "light"
                            Theme.DARK   -> "dark"
                            Theme.AUTO -> "auto"
                        }
                        hideControlsPref?.isChecked = state.hideControls
                        hideLastUpdatePref?.isChecked = state.hideLastUpdate
                        keepScreenOnPref?.isChecked = state.keepScreenOn
                        showLabelsPref?.isChecked = state.showLabels
                        isInitializing = false
                    }
                }
            }
        }

        // Observe events for showing messages
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.event.collect { event ->
                    when (event) {
                        is DisplayPreferenceEvent.ShowMessage -> {
                            // Show snackbar or toast
                            Snackbar.make(requireView(), event.message, Snackbar.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

        // User changes → send to ViewModel via onAction
        themePref?.setOnPreferenceChangeListener { _, newValue ->
            if (!isInitializing) {
                val themeString = newValue as String
                val theme = Theme.getCurrent(id = themeString) ?: Theme.SYSTEM
                viewModel.onAction(DisplayPreferenceAction.SetTheme(theme))
            }
            true
        }

        hideControlsPref?.setOnPreferenceChangeListener { _, newValue ->
            if (!isInitializing) {
                viewModel.onAction(DisplayPreferenceAction.SetHideControls(newValue as Boolean))
            }
            true
        }

        hideLastUpdatePref?.setOnPreferenceChangeListener { _, newValue ->
            if (!isInitializing) {
                viewModel.onAction(DisplayPreferenceAction.SetHideLastUpdate(newValue as Boolean))
            }
            true
        }

        keepScreenOnPref?.setOnPreferenceChangeListener { _, newValue ->
            if (!isInitializing) {
                viewModel.onAction(DisplayPreferenceAction.SetKeepScreenOn(newValue as Boolean))
            }
            true
        }

        showLabelsPref?.setOnPreferenceChangeListener { _, newValue ->
            if (!isInitializing) {
                viewModel.onAction(DisplayPreferenceAction.SetShowLabels(newValue as Boolean))
            }
            true
        }
    }
}