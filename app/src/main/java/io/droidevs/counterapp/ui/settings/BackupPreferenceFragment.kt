package io.droidevs.counterapp.ui.settings

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import io.droidevs.counterapp.R
import io.droidevs.counterapp.ui.vm.BackupPreferenceViewModel
import kotlinx.coroutines.launch

class BackupPreferencesFragment : PreferenceFragmentCompat() {

    private val viewModel: BackupPreferenceViewModel by viewModels()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.backup_preferences, rootKey)

        val autoBackupPref = findPreference<SwitchPreferenceCompat>("auto_backup")
        val intervalPref = findPreference<ListPreference>("backup_interval")
        val exportPref = findPreference<Preference>("manual_export")

        // Observe ViewModel → UI
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.autoBackup.collect { enabled ->
                    autoBackupPref?.isChecked = enabled
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.backupInterval.collect { hours ->
                    intervalPref?.value = hours.toString()
                }
            }
        }

        // User changes → ViewModel
        autoBackupPref?.setOnPreferenceChangeListener { _, newValue ->
            viewModel.setAutoBackup(newValue as Boolean)
            true
        }

        intervalPref?.setOnPreferenceChangeListener { _, newValue ->
            val hours = (newValue as? String)?.toLongOrNull() ?: 24L
            viewModel.setBackupInterval(hours)
            true
        }

        // Optional manual export
        exportPref?.setOnPreferenceClickListener {
            viewModel.triggerManualExport()
            true
        }
    }
}