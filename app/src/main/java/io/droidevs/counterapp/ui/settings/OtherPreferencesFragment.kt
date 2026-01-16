package io.droidevs.counterapp.ui.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import io.droidevs.counterapp.R
import io.droidevs.counterapp.domain.services.ExportFormat
import io.droidevs.counterapp.ui.vm.ExportViewModel
import io.droidevs.counterapp.ui.vm.OtherPreferencesViewModel
import io.droidevs.counterapp.ui.vm.actions.ExportAction
import io.droidevs.counterapp.ui.vm.actions.OtherPreferencesAction
import io.droidevs.counterapp.ui.vm.events.ExportEvent
import io.droidevs.counterapp.ui.vm.events.OtherPreferencesEvent
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OtherPreferencesFragment : PreferenceFragmentCompat() {

    private val viewModel: OtherPreferencesViewModel by viewModels()
    private val exportViewModel: ExportViewModel by viewModels()
    private var removeCountersDialog: AlertDialog? = null
    private var exportFormatDialog: AlertDialog? = null

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
        setPreferencesFromResource(R.xml.other_preferences, rootKey)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupActions()
        bindVersion()
        observeViewModel()
    }

    private fun setupActions() {
        findPreference<Preference>("removeCounters")
            ?.setOnPreferenceClickListener {
                viewModel.onAction(OtherPreferencesAction.RemoveCounters)
                true
            }

        findPreference<Preference>("exportCounters")
            ?.setOnPreferenceClickListener {
                exportViewModel.onAction(ExportAction.RequestExport)
                true
            }

        findPreference<Preference>("homepage")
            ?.setOnPreferenceClickListener {
                openUrl("https://github.com/droidevs/counter-app")
                true
            }

        findPreference<Preference>("tip")
            ?.setOnPreferenceClickListener {
                openUrl("https://ko-fi.com/mouadoumous3")
                true
            }
    }

    private fun bindVersion() {
        val appVersion: String = try {
            requireContext().packageManager
                .getPackageInfo(requireContext().packageName, 0)
                .versionName ?: getString(R.string.unknown_version)
        } catch (e: Exception) {
            getString(R.string.unknown_version)
        }
        findPreference<Preference>("version")?.summary = appVersion
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                // Observe events
                launch {
                    viewModel.event.collect { event ->
                        when (event) {
                            is OtherPreferencesEvent.ShowRemoveConfirmation -> showRemoveConfirmationDialog()
                        }
                    }
                }
                launch {
                    exportViewModel.event.collect { event ->
                        when (event) {
                            is ExportEvent.ShowExportFormatDialog -> showExportFormatDialog(event.formats)
                            is ExportEvent.ShareExportFile -> shareExportFile(event.fileUri)
                        }
                    }
                }

                // Observe loading state (optional)
                launch {
                    viewModel.state.collect { state ->
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
                    }
                }
            }
        }
    }

    private fun showRemoveConfirmationDialog() {
        removeCountersDialog?.dismiss()

        removeCountersDialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.remove_counters))
            .setMessage(getString(R.string.remove_counters_confirmation))
            .setPositiveButton(getString(R.string.remove)) { _, _ ->
                viewModel.onAction(OtherPreferencesAction.ConfirmRemoveCounters)
            }
            .setNegativeButton(getString(android.R.string.cancel), null)
            .setOnDismissListener { removeCountersDialog = null }
            .show()
    }

    private fun showExportFormatDialog(formats: List<ExportFormat>) {
        exportFormatDialog?.dismiss()

        val items = formats.map { it.name }.toTypedArray()

        exportFormatDialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.export_as))
            .setItems(items) { _, which ->
                exportViewModel.onAction(ExportAction.Export(formats[which]))
            }
            .setOnDismissListener { exportFormatDialog = null }
            .show()
    }

    private fun shareExportFile(fileUri: Uri) {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/csv" // or "application/json" depending on your format
            putExtra(Intent.EXTRA_STREAM, fileUri)
            putExtra(Intent.EXTRA_SUBJECT, "Counters Export")
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        }
        startActivity(Intent.createChooser(shareIntent, "Share Counters"))
    }

    private fun openUrl(url: String) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
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

    override fun onDestroyView() {
        removeCountersDialog?.dismiss()
        exportFormatDialog?.dismiss()
        super.onDestroyView()
    }
}
