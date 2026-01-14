package io.droidevs.counterapp.ui.settings

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import io.droidevs.counterapp.R
import io.droidevs.counterapp.ui.vm.BackupPreferenceViewModel
import io.droidevs.counterapp.ui.vm.actions.BackupPreferenceAction
import io.droidevs.counterapp.ui.vm.events.BackupPreferenceEvent
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BackupPreferencesFragment : PreferenceFragmentCompat() {

    private val viewModel: BackupPreferenceViewModel by viewModels()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.backup_preferences, rootKey)

        val autoBackupPref = findPreference<SwitchPreferenceCompat>("auto_backup")
        val intervalPref = findPreference<ListPreference>("backup_interval")
        val exportPref = findPreference<Preference>("manual_export")

        // Observe ViewModel UI State
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    // Update preferences based on state
                    autoBackupPref?.isChecked = state.autoBackup
                    intervalPref?.value = state.backupInterval.toString()
                }
            }
        }

        // Observe events
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.event.collect { event ->
                    when (event) {
                        is BackupPreferenceEvent.ShowMessage -> {
                            // Show snackbar or toast
                            Snackbar.make(requireView(), event.message, Snackbar.LENGTH_SHORT).show()
                        }
                        BackupPreferenceEvent.ExportTriggered -> {
                            // Handle export
                        }
                    }
                }
            }
        }

        // User changes → ViewModel
        autoBackupPref?.setOnPreferenceChangeListener { _, newValue ->
            viewModel.onAction(BackupPreferenceAction.SetAutoBackup(newValue as Boolean))
            true
        }

        intervalPref?.setOnPreferenceChangeListener { _, newValue ->
            val hours = (newValue as? String)?.toLongOrNull() ?: 24L
            viewModel.onAction(BackupPreferenceAction.SetBackupInterval(hours))
            true
        }

        exportPref?.setOnPreferenceClickListener {
            viewModel.onAction(BackupPreferenceAction.TriggerManualExport)
            true
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}