package io.droidevs.counterapp.ui.settings

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import io.droidevs.counterapp.R
import io.droidevs.counterapp.ui.vm.HardwarePreferencesViewModel
import kotlinx.coroutines.launch

class HardwarePreferencesFragment : PreferenceFragmentCompat() {

    private val viewModel: HardwarePreferencesViewModel by viewModels()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.hardware_preferences, rootKey)

        val hardwareButtonPref = findPreference<SwitchPreferenceCompat>("hardware_button_control")
        val soundsPref = findPreference<SwitchPreferenceCompat>("sounds_on")
        val vibrationPref = findPreference<SwitchPreferenceCompat>("vibration_on")
        val labelsPref = findPreference<SwitchPreferenceCompat>("show_labels")

        // Observe ViewModel → update UI
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.hardwareButtonControl.collect { enabled ->
                    hardwareButtonPref?.isChecked = enabled
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.soundsOn.collect { enabled ->
                    soundsPref?.isChecked = enabled
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.vibrationOn.collect { enabled ->
                    vibrationPref?.isChecked = enabled
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.showLabels.collect { show ->
                    labelsPref?.isChecked = show
                }
            }
        }

        // User changes → send to ViewModel
        hardwareButtonPref?.setOnPreferenceChangeListener { _, newValue ->
            viewModel.setHardwareButtonControl(newValue as Boolean)
            true
        }

        soundsPref?.setOnPreferenceChangeListener { _, newValue ->
            viewModel.setSoundsOn(newValue as Boolean)
            true
        }

        vibrationPref?.setOnPreferenceChangeListener { _, newValue ->
            viewModel.setVibrationOn(newValue as Boolean)
            true
        }

        labelsPref?.setOnPreferenceChangeListener { _, newValue ->
            viewModel.setShowLabels(newValue as Boolean)
            true
        }
    }
}