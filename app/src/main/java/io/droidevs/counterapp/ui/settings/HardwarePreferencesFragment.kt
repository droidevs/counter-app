package io.droidevs.counterapp.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import dagger.hilt.android.AndroidEntryPoint
import io.droidevs.counterapp.R
import io.droidevs.counterapp.ui.vm.HardwarePreferencesViewModel
import io.droidevs.counterapp.ui.vm.actions.HardwarePreferenceAction
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HardwarePreferencesFragment : PreferenceFragmentCompat() {

    private val viewModel: HardwarePreferencesViewModel by viewModels()

    private var hardwareButtonPref: SwitchPreferenceCompat? = null
    private var soundsPref: SwitchPreferenceCompat? = null
    private var vibrationPref: SwitchPreferenceCompat? = null
    private var keepScreenOnPref: SwitchPreferenceCompat? = null

    private var loadingView: View? = null
    private var errorView: View? = null
    private var container: ViewGroup? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        this.container = view as? ViewGroup
        return view
    }

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
        keepScreenOnPref = findPreference("keep_screen_on")
        hardwareButtonPref = findPreference("hardware_button_control")
        soundsPref = findPreference("sounds_on")
        vibrationPref = findPreference("vibration_on")
    }

    private fun setupPreferenceListeners() {
        keepScreenOnPref?.setOnPreferenceChangeListener { _, newValue ->
            viewModel.onAction(HardwarePreferenceAction.SetKeepScreenOn(newValue as Boolean))
            true
        }

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
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    if (state.isLoading) {
                        showLoading()
                    } else {
                        hideLoading()
                    }

                    if (state.error) {
                        showError()
                    } else {
                        hideError()
                    }

                    preferenceScreen.isVisible = !state.isLoading && !state.error

                    hardwareButtonPref?.isChecked = state.hardwareButtonControl
                    soundsPref?.isChecked = state.soundsOn
                    vibrationPref?.isChecked = state.vibrationOn
                    keepScreenOnPref?.isChecked = state.keepScreenOn
                }
            }
        }
    }

    private fun showLoading() {
        hideError()
        if (loadingView == null) {
            loadingView = layoutInflater.inflate(R.layout.loading_state_layout, container, false)
        }
        loadingView?.let { container?.addView(it) }
    }

    private fun hideLoading() {
        loadingView?.let { container?.removeView(it) }
    }

    private fun showError() {
        hideLoading()
        if (errorView == null) {
            errorView = layoutInflater.inflate(R.layout.error_state_layout, container, false)
        }
        errorView?.let { container?.addView(it) }
    }

    private fun hideError() {
        errorView?.let { container?.removeView(it) }
    }
}
