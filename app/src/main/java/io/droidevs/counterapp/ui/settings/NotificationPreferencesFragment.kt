package io.droidevs.counterapp.ui.settings

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import io.droidevs.counterapp.R
import io.droidevs.counterapp.ui.vm.NotificationsPreferencesViewModel
import io.droidevs.counterapp.ui.vm.actions.NotificationPreferenceAction
import io.droidevs.counterapp.ui.vm.events.NotificationPreferenceEvent
import kotlinx.coroutines.launch


@AndroidEntryPoint
class NotificationPreferencesFragment : PreferenceFragmentCompat() {

    private val viewModel: NotificationsPreferencesViewModel by viewModels()

    private var limitNotifPref: SwitchPreferenceCompat? = null
    private var dailySummaryPref: SwitchPreferenceCompat? = null
    private var soundPref: ListPreference? = null
    private var vibrationPref: ListPreference? = null

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.notification_preferences, rootKey)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        findPreferences()
        observeUiState()
        observeEvents()
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
                    limitNotifPref?.isChecked = state.counterLimitNotification
                    dailySummaryPref?.isChecked = state.dailySummaryNotification
                    soundPref?.value = state.notificationSound
                    vibrationPref?.value = state.notificationVibrationPattern
                }
            }
        }
    }

    private fun observeEvents() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.event.collect { event ->
                    when (event) {
                        is NotificationPreferenceEvent.ShowMessage -> {
                            Snackbar.make(requireView(), event.message, Snackbar.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }
}