package io.droidevs.counterapp.ui.settings

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.google.android.material.snackbar.Snackbar
import io.droidevs.counterapp.R
import io.droidevs.counterapp.ui.vm.HardwarePreferencesViewModel
import io.droidevs.counterapp.ui.vm.actions.HardwarePreferenceAction
import io.droidevs.counterapp.ui.vm.events.HardwarePreferenceEvent
import kotlinx.coroutines.launch

class HardwarePreferencesFragment : PreferenceFragmentCompat() {

    private val viewModel: HardwarePreferencesViewModel by viewModels()
    private var isInitializing = true

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.hardware_preferences, rootKey)

        val hardwareButtonPref = findPreference<SwitchPreferenceCompat>("hardware_button_control")
        val soundsPref = findPreference<SwitchPreferenceCompat>("sounds_on")
        val vibrationPref = findPreference<SwitchPreferenceCompat>("vibration_on")
        val labelsPref = findPreference<SwitchPreferenceCompat>("show_labels")

        // Observe ViewModel UI State → update UI
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    if (isInitializing) {
                        // Set initial values without triggering listeners
                        hardwareButtonPref?.isChecked = state.hardwareButtonControl
                        soundsPref?.isChecked = state.soundsOn
                        vibrationPref?.isChecked = state.vibrationOn
                        labelsPref?.isChecked = state.showLabels
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
                        is HardwarePreferenceEvent.ShowMessage -> {
                            // Show snackbar or toast
                            Snackbar.make(requireView(), event.message, Snackbar.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

        // User changes → send to ViewModel via onAction
        hardwareButtonPref?.setOnPreferenceChangeListener { _, newValue ->
            if (!isInitializing) {
                viewModel.onAction(
                    HardwarePreferenceAction.SetHardwareButtonControl(newValue as Boolean)
                )
            }
            true
        }

        soundsPref?.setOnPreferenceChangeListener { _, newValue ->
            if (!isInitializing) {
                viewModel.onAction(
                    HardwarePreferenceAction.SetSoundsOn(newValue as Boolean)
                )
            }
            true
        }

        vibrationPref?.setOnPreferenceChangeListener { _, newValue ->
            if (!isInitializing) {
                viewModel.onAction(
                    HardwarePreferenceAction.SetVibrationOn(newValue as Boolean)
                )
            }
            true
        }

        labelsPref?.setOnPreferenceChangeListener { _, newValue ->
            if (!isInitializing) {
                viewModel.onAction(
                    HardwarePreferenceAction.SetShowLabels(newValue as Boolean)
                )
            }
            true
        }
    }
}