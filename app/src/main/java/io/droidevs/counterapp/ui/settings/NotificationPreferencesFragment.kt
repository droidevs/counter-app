package io.droidevs.counterapp.ui.settings

import android.os.Bundle
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
    private var isInitializing = true

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.notification_preferences, rootKey)

        val limitNotifPref = findPreference<SwitchPreferenceCompat>("counter_limit_notification")
        val dailySummaryPref = findPreference<SwitchPreferenceCompat>("daily_summary_notification")
        val soundPref = findPreference<ListPreference>("notification_sound")
        val vibrationPref = findPreference<ListPreference>("vibration_pattern")


        // Observe ViewModel UI State → update UI
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    if (isInitializing) {
                        // Set initial values without triggering listeners
                        limitNotifPref?.isChecked = state.counterLimitNotification
                        dailySummaryPref?.isChecked = state.dailySummaryNotification
                        soundPref?.value = state.notificationSound
                        vibrationPref?.value = state.notificationVibrationPattern
                        isInitializing = false
                    }
                }
            }
        }

        // Observe events for showing messages
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.event.collect { event ->
                    when (event) {
                        is NotificationPreferenceEvent.ShowMessage -> {
                            // Show snackbar or toast
                            Snackbar.make(requireView(), event.message, Snackbar.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

        // User changes → send to ViewModel via onAction
        limitNotifPref?.setOnPreferenceChangeListener { _, newValue ->
            if (!isInitializing) {
                viewModel.onAction(
                    NotificationPreferenceAction.SetCounterLimitNotification(newValue as Boolean)
                )
            }
            true
        }

        dailySummaryPref?.setOnPreferenceChangeListener { _, newValue ->
            if (!isInitializing) {
                viewModel.onAction(
                    NotificationPreferenceAction.SetDailySummaryNotification(newValue as Boolean)
                )
            }
            true
        }

        soundPref?.setOnPreferenceChangeListener { _, newValue ->
            if (!isInitializing) {
                viewModel.onAction(
                    NotificationPreferenceAction.SetNotificationSound(newValue as String)
                )
            }
            true
        }

        vibrationPref?.setOnPreferenceChangeListener { _, newValue ->
            if (!isInitializing) {
                viewModel.onAction(
                    NotificationPreferenceAction.SetNotificationVibrationPattern(newValue as String)
                )
            }
            true
        }
    }
}