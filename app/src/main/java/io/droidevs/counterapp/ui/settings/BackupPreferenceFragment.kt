package io.droidevs.counterapp.ui.settings

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import io.droidevs.counterapp.R
import io.droidevs.counterapp.domain.services.ExportFormat
import io.droidevs.counterapp.ui.vm.BackupPreferenceViewModel
import io.droidevs.counterapp.ui.vm.actions.BackupPreferenceAction
import io.droidevs.counterapp.ui.vm.events.BackupPreferenceEvent
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BackupPreferencesFragment : PreferenceFragmentCompat() {

    private val viewModel: BackupPreferenceViewModel by viewModels()

    private var autoBackupPref: SwitchPreferenceCompat? = null
    private var intervalPref: ListPreference? = null
    private var exportPref: Preference? = null
    private var exportFormatDialog: AlertDialog? = null

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.backup_preferences, rootKey)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        findPreferences()
        observeUiState()
        observeEvents()
        setupPreferenceListeners()
    }

    private fun findPreferences() {
        autoBackupPref = findPreference("auto_backup")
        intervalPref = findPreference("backup_interval")
        exportPref = findPreference("manual_export")
    }

    private fun setupPreferenceListeners() {
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

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    autoBackupPref?.isChecked = state.autoBackup
                    intervalPref?.value = state.backupInterval.toString()
                }
            }
        }
    }

    private fun observeEvents() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.event.collect { event ->
                    when (event) {
                        is BackupPreferenceEvent.ShowMessage -> {
                            Snackbar.make(requireView(), event.message, Snackbar.LENGTH_SHORT).show()
                        }
                        is BackupPreferenceEvent.ShowExportFormatDialog -> showExportFormatDialog(event.formats)
                        is BackupPreferenceEvent.ShareExportFile -> shareExportFile(event)
                    }
                }
            }
        }
    }

    private fun showExportFormatDialog(formats: List<ExportFormat>) {
        exportFormatDialog?.dismiss()
        val formatItems = formats.map { it.name }.toTypedArray()

        exportFormatDialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.export_counters_as))
            .setItems(formatItems) { _, which ->
                viewModel.onAction(BackupPreferenceAction.Export(formats[which]))
            }
            .setOnDismissListener { exportFormatDialog = null }
            .show()
    }

    private fun shareExportFile(event: BackupPreferenceEvent.ShareExportFile) {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "*/*"
            putExtra(Intent.EXTRA_STREAM, event.fileUri)
            putExtra(Intent.EXTRA_SUBJECT, "Counters Export")
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        }
        startActivity(Intent.createChooser(shareIntent, "Share Counters"))
    }

    override fun onDestroyView() {
        exportFormatDialog?.dismiss()
        super.onDestroyView()
    }
}