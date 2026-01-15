package io.droidevs.counterapp.ui.settings

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import io.droidevs.counterapp.R
import io.droidevs.counterapp.ui.vm.HardwarePreferencesViewModel
import io.droidevs.counterapp.ui.vm.actions.HardwarePreferenceAction
import io.droidevs.counterapp.ui.vm.events.HardwarePreferenceEvent
import kotlinx.coroutines.launch


@AndroidEntryPoint
class HardwarePreferencesFragment : PreferenceFragmentCompat() {

    private val viewModel: HardwarePreferencesViewModel by viewModels()

    private var hardwareButtonPref: SwitchPreferenceCompat? = null
    private var soundsPref: SwitchPreferenceCompat? = null
    private var vibrationPref: SwitchPreferenceCompat? = null
    private var labelsPref: SwitchPreferenceCompat? = null

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.hardware_preferences, rootKey)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        findPreferences()
        observeUiState()
        setupPreferenceListeners()
    }

    private fun findPreferences() {
        hardwareButtonPref = findPreference("hardware_button_control")
        soundsPref = findPreference("sounds_on")
        vibrationPref = findPreference("vibration_on")
        labelsPref = findPreference("show_labels")
    }

    private fun setupPreferenceListeners() {
        hardwareButtonPref?.setOnPreferenceChangeListener { _, newValue ->
            viewModel.onAction(
                HardwarePreferenceAction.SetHardwareButtonControl(newValue as Boolean)
            )
            true
        }

        soundsPref?.setOnPreferenceChangeListener { _, newValue ->
            viewModel.onAction(
                HardwarePreferenceAction.SetSoundsOn(newValue as Boolean)
            )
            true
        }

        vibrationPref?.setOnPreferenceChangeListener { _, newValue ->
            viewModel.onAction(
                HardwarePreferenceAction.SetVibrationOn(newValue as Boolean)
            )
            true
        }

        labelsPref?.setOnPreferenceChangeListener { _, newValue ->
            viewModel.onAction(
                HardwarePreferenceAction.SetShowLabels(newValue as Boolean)
            )
            true
        }
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    hardwareButtonPref?.isChecked = state.hardwareButtonControl
                    soundsPref?.isChecked = state.soundsOn
                    vibrationPref?.isChecked = state.vibrationOn
                    labelsPref?.isChecked = state.showLabels
                }
            }
        }
    }
}