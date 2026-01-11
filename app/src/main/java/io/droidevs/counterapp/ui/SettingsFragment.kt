package io.droidevs.counterapp.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import dagger.hilt.android.AndroidEntryPoint
import io.droidevs.counterapp.R
import io.droidevs.counterapp.ui.vm.SettingsViewModel
import io.droidevs.counterapp.ui.vm.actions.SettingsAction
import io.droidevs.counterapp.ui.vm.events.SettingsEvent
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SettingsFragment : PreferenceFragmentCompat() {

    private val viewModel: SettingsViewModel by viewModels()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
        setupNavigation()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
    }

    private fun setupNavigation() {
        findPreference<Preference>("hardware_preferences")
            ?.setOnPreferenceClickListener {
                viewModel.onAction(SettingsAction.HardwareClicked)
                true
            }

        findPreference<Preference>("display_preferences")
            ?.setOnPreferenceClickListener {
                viewModel.onAction(SettingsAction.DisplayClicked)
                true
            }

        findPreference<Preference>("counter_preferences")
            ?.setOnPreferenceClickListener {
                viewModel.onAction(SettingsAction.CounterClicked)
                true
            }

        findPreference<Preference>("notification_preferences")
            ?.setOnPreferenceClickListener {
                viewModel.onAction(SettingsAction.NotificationClicked)
                true
            }

        findPreference<Preference>("backup_preferences")
            ?.setOnPreferenceClickListener {
                viewModel.onAction(SettingsAction.BackupClicked)
                true
            }

        findPreference<Preference>("other_preferences")
            ?.setOnPreferenceClickListener {
                viewModel.onAction(SettingsAction.OtherClicked)
                true
            }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.event.collect { event ->
                    when (event) {
                        SettingsEvent.NavigateToHardware -> findNavController().navigate(R.id.action_to_hardware_preferences)
                        SettingsEvent.NavigateToDisplay -> findNavController().navigate(R.id.action_to_display_preferences)
                        SettingsEvent.NavigateToCounter -> findNavController().navigate(R.id.action_to_counter_preferences)
                        SettingsEvent.NavigateToNotification -> findNavController().navigate(R.id.action_to_notification_preferences)
                        SettingsEvent.NavigateToBackup -> findNavController().navigate(R.id.action_to_backup_preferences)
                        SettingsEvent.NavigateToOther -> findNavController().navigate(R.id.action_to_other_preferences)
                    }
                }
            }
        }
    }
}
