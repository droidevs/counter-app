package io.droidevs.counterapp.ui.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import io.droidevs.counterapp.R
import io.droidevs.counterapp.ui.vm.SettingsViewModel
import io.droidevs.counterapp.ui.vm.factories.SettingsViewModelFactory
import io.droidevs.counterapp.CounterApp

class OtherPreferencesFragment : PreferenceFragmentCompat() {

    private val viewModel: SettingsViewModel by viewModels {
        SettingsViewModelFactory(
            repo = (requireContext().applicationContext as CounterApp).settingsRepository
        )
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.other_preferences, rootKey)

        setupActions()
        bindVersion()
    }

    private fun setupActions() {
        findPreference<Preference>("removeCounters")
            ?.setOnPreferenceClickListener {
                showWipeDialog()
                true
            }

        findPreference<Preference>("exportCounters")
            ?.setOnPreferenceClickListener {
                viewModel.exportCounters()
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

    private fun openUrl(url: String) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }

    private fun showWipeDialog() {
        // TODO implement a confirmation dialog
    }
}
