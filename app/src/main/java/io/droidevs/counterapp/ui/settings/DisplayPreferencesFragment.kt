package io.droidevs.counterapp.ui.settings

import android.os.Bundle
import android.view.View
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

    private var themePref: ListPreference? = null
    private var hideControlsPref: SwitchPreferenceCompat? = null
    private var hideLastUpdatePref: SwitchPreferenceCompat? = null
    private var keepScreenOnPref: SwitchPreferenceCompat? = null

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.display_preferences, rootKey)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        findPreferences()
        observeUiState()
        setupPreferenceListeners()
    }

    private fun findPreferences() {
        themePref = findPreference("theme")
        hideControlsPref = findPreference("hide_controls")
        hideLastUpdatePref = findPreference("hide_last_update")
        keepScreenOnPref = findPreference("keep_screen_on")
    }

    private fun setupPreferenceListeners() {
        themePref?.setOnPreferenceChangeListener { _, newValue ->
            val themeString = newValue as String
            val theme = Theme.getCurrent(id = themeString) ?: Theme.SYSTEM
            viewModel.onAction(DisplayPreferenceAction.SetTheme(theme))
            true
        }

        hideControlsPref?.setOnPreferenceChangeListener { _, newValue ->
            viewModel.onAction(DisplayPreferenceAction.SetHideControls(newValue as Boolean))
            true
        }

        hideLastUpdatePref?.setOnPreferenceChangeListener { _, newValue ->
            viewModel.onAction(DisplayPreferenceAction.SetHideLastUpdate(newValue as Boolean))
            true
        }

        keepScreenOnPref?.setOnPreferenceChangeListener { _, newValue ->
            viewModel.onAction(DisplayPreferenceAction.SetKeepScreenOn(newValue as Boolean))
            true
        }
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    themePref?.value = when (state.theme) {
                        Theme.SYSTEM -> "system"
                        Theme.LIGHT -> "light"
                        Theme.DARK -> "dark"
                        Theme.AUTO -> "auto"
                    }
                    hideControlsPref?.isChecked = state.hideControls
                    hideLastUpdatePref?.isChecked = state.hideLastUpdate
                    keepScreenOnPref?.isChecked = state.keepScreenOn
                }
            }
        }
    }

}