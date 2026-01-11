package io.droidevs.counterapp.ui

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import dagger.hilt.android.AndroidEntryPoint
import io.droidevs.counterapp.R
import io.droidevs.counterapp.ui.vm.SettingsViewModel

@AndroidEntryPoint
class SettingsFragment : PreferenceFragmentCompat() {

    private val viewModel: SettingsViewModel by viewModels()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
        setupNavigation()
    }

    private fun setupNavigation() {
        // Hardware/Controls preferences
        findPreference<Preference>("hardware_preferences")
            ?.setOnPreferenceClickListener {
                findNavController().navigate(R.id.action_to_hardware_preferences)
                true
            }

        // Display preferences
        findPreference<Preference>("display_preferences")
            ?.setOnPreferenceClickListener {
                findNavController().navigate(R.id.action_to_display_preferences)
                true
            }

        // Counter preferences
        findPreference<Preference>("counter_preferences")
            ?.setOnPreferenceClickListener {
                findNavController().navigate(R.id.action_to_counter_preferences)
                true
            }

        // Notification preferences
        findPreference<Preference>("notification_preferences")
            ?.setOnPreferenceClickListener {
                findNavController().navigate(R.id.action_to_notification_preferences)
                true
            }

        // Backup preferences
        findPreference<Preference>("backup_preferences")
            ?.setOnPreferenceClickListener {
                findNavController().navigate(R.id.action_to_backup_preferences)
                true
            }

        // Other preferences
        findPreference<Preference>("other_preferences")
            ?.setOnPreferenceClickListener {
                findNavController().navigate(R.id.action_to_other_preferences)
                true
            }
    }
}