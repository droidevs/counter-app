package io.droidevs.counterapp.ui.settings

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
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
import io.droidevs.counterapp.ui.vm.ExportViewModel
import io.droidevs.counterapp.ui.vm.ImportViewModel
import io.droidevs.counterapp.ui.vm.actions.BackupPreferenceAction
import io.droidevs.counterapp.ui.vm.actions.ExportAction
import io.droidevs.counterapp.ui.vm.actions.ImportAction
import io.droidevs.counterapp.ui.vm.events.ExportEvent
import io.droidevs.counterapp.ui.vm.events.ImportEvent
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BackupPreferenceFragment : PreferenceFragmentCompat() {

    private val backupViewModel: BackupPreferenceViewModel by viewModels()
    private val exportViewModel: ExportViewModel by viewModels()
    private val importViewModel: ImportViewModel by viewModels()

    private var autoBackupPref: SwitchPreferenceCompat? = null
    private var intervalPref: ListPreference? = null
    private var exportPref: Preference? = null
    private var importPref: Preference? = null
    private var exportFormatDialog: AlertDialog? = null

    private val importFileResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                importViewModel.onAction(ImportAction.Import(uri))
            }
        }
    }

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
        importPref = findPreference("manual_import")
    }

    private fun setupPreferenceListeners() {
        autoBackupPref?.setOnPreferenceChangeListener { _, newValue ->
            backupViewModel.onAction(BackupPreferenceAction.SetAutoBackup(newValue as Boolean))
            true
        }

        intervalPref?.setOnPreferenceChangeListener { _, newValue ->
            val hours = (newValue as? String)?.toLongOrNull() ?: 24L
            backupViewModel.onAction(BackupPreferenceAction.SetBackupInterval(hours))
            true
        }

        exportPref?.setOnPreferenceClickListener {
            exportViewModel.onAction(ExportAction.RequestExport)
            true
        }

        importPref?.setOnPreferenceClickListener {
            importViewModel.onAction(ImportAction.RequestImport)
            true
        }
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                backupViewModel.uiState.collect { state ->
                    autoBackupPref?.isChecked = state.autoBackup
                    intervalPref?.value = state.backupInterval.toString()
                }
            }
        }
    }

    private fun observeEvents() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                exportViewModel.event.collect(::handleExportEvent)
                importViewModel.event.collect(::handleImportEvent)
            }
        }
    }

    private fun handleExportEvent(event: ExportEvent) {
        when (event) {
            is ExportEvent.ShowExportFormatDialog -> showExportFormatDialog(event.formats)
            is ExportEvent.ShareExportFile -> shareExportFile(event)
            is ExportEvent.ShowMessage -> showMessage(event.message)
        }
    }

    private fun handleImportEvent(event: ImportEvent) {
        when (event) {
            is ImportEvent.ShowImportFileChooser -> showImportFileChooser()
            is ImportEvent.ShowMessage -> showMessage(event.message)
        }
    }

    private fun showExportFormatDialog(formats: List<ExportFormat>) {
        exportFormatDialog?.dismiss()
        val formatItems = formats.map { it.name }.toTypedArray()

        exportFormatDialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.export_counters_as))
            .setItems(formatItems) { _, which ->
                exportViewModel.onAction(ExportAction.Export(formats[which]))
            }
            .setOnDismissListener { exportFormatDialog = null }
            .show()
    }

    private fun shareExportFile(event: ExportEvent.ShareExportFile) {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "*/*"
            putExtra(Intent.EXTRA_STREAM, event.fileUri)
            putExtra(Intent.EXTRA_SUBJECT, "Counters Export")
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        }
        startActivity(Intent.createChooser(shareIntent, "Share Counters"))
    }

    private fun showImportFileChooser() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "*/*"
        }
        importFileResult.launch(intent)
    }

    private fun showMessage(message: String) {
        Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        exportFormatDialog?.dismiss()
        super.onDestroyView()
    }
}
