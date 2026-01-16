package io.droidevs.counterapp.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import dagger.hilt.android.AndroidEntryPoint
import io.droidevs.counterapp.R
import io.droidevs.counterapp.ui.vm.NotificationPreferencesViewModel
import io.droidevs.counterapp.ui.vm.actions.NotificationPreferenceAction
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NotificationPreferencesFragment : PreferenceFragmentCompat() {

    private val viewModel: NotificationPreferencesViewModel by viewModels()

    private var limitNotifPref: SwitchPreferenceCompat? = null
    private var dailySummaryPref: SwitchPreferenceCompat? = null
    private var soundPref: ListPreference? = null
    private var vibrationPref: ListPreference? = null

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
        setPreferencesFromResource(R.xml.notification_preferences, rootKey)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        findPreferences()
        observeUiState()
        setupPreferenceListeners()
    }

    private fun findPreferences() {
        limitNotifPref = findPreference("counter_limit_notification")
        dailySummaryPref = findPreference("daily_summary_notification")
        soundPref = findPreference("notification_sound")
        vibrationPref = findPreference("vibration_pattern")
    }

    private fun setupPreferenceListeners() {
        limitNotifPref?.setOnPreferenceChangeListener { _, newValue ->
            viewModel.onAction(
                NotificationPreferenceAction.SetCounterLimitNotification(newValue as Boolean)
            )
            true
        }

        dailySummaryPref?.setOnPreferenceChangeListener { _, newValue ->
            viewModel.onAction(
                NotificationPreferenceAction.SetDailySummaryNotification(newValue as Boolean)
            )
            true
        }

        soundPref?.setOnPreferenceChangeListener { _, newValue ->
            viewModel.onAction(
                NotificationPreferenceAction.SetNotificationSound(newValue as String)
            )
            true
        }

        vibrationPref?.setOnPreferenceChangeListener { _, newValue ->
            viewModel.onAction(
                NotificationPreferenceAction.SetNotificationVibrationPattern(newValue as String)
            )
            true
        }
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
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

                    limitNotifPref?.isChecked = state.counterLimitNotification
                    dailySummaryPref?.isChecked = state.dailySummaryNotification
                    soundPref?.value = state.notificationSound
                    vibrationPref?.value = state.notificationVibrationPattern
                }
            }
        }
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
}
