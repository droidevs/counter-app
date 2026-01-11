package io.droidevs.counterapp.ui.settings

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import io.droidevs.counterapp.R
import io.droidevs.counterapp.CounterApp
import io.droidevs.counterapp.ui.vm.OtherPreferencesViewModel
import io.droidevs.counterapp.ui.vm.actions.OtherPreferencesAction
import io.droidevs.counterapp.ui.vm.events.OtherPreferencesEvent
import kotlinx.coroutines.launch

class OtherPreferencesFragment : PreferenceFragmentCompat() {


    private val viewModel: OtherPreferencesViewModel by viewModels()
    private var removeCountersDialog: AlertDialog? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.other_preferences, rootKey)

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
                viewModel.onAction(OtherPreferencesAction.ExportCounters)
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
                            is OtherPreferencesEvent.RemoveSuccess -> showMessage("Counters removed successfully")
                            is OtherPreferencesEvent.ExportSuccess -> shareExportFile(event.fileUri)
                            is OtherPreferencesEvent.Error -> showMessage(event.message)
                        }
                    }
                }

                // Observe loading state (optional)
                launch {
                    viewModel.state.collect { state ->
                        // Update UI based on loading state if needed
                        if (state.isLoading) {
                            // Show loading indicator
                        } else {
                            // Hide loading indicator
                        }
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

    private fun showMessage(message: String) {
        Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        removeCountersDialog?.dismiss()
        super.onDestroyView()
    }
}
