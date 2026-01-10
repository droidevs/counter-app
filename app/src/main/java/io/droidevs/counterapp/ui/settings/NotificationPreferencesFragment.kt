package io.droidevs.counterapp.ui.settings

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import io.droidevs.counterapp.R
import io.droidevs.counterapp.ui.vm.NotificationsPreferencesViewModel
import kotlinx.coroutines.launch

class NotificationPreferencesFragment : PreferenceFragmentCompat() {

    private val viewModel: NotificationsPreferencesViewModel by viewModels()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.notification_preferences, rootKey)

        val limitNotifPref = findPreference<SwitchPreferenceCompat>("counter_limit_notification")
        val dailySummaryPref = findPreference<SwitchPreferenceCompat>("daily_summary_notification")
        val soundPref = findPreference<ListPreference>("notification_sound")
        val vibrationPref = findPreference<ListPreference>("vibration_pattern")

        // Observe ViewModel → UI (two-way binding)
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.counterLimitNotification.collect { enabled ->
                    limitNotifPref?.isChecked = enabled
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.dailySummaryNotification.collect { enabled ->
                    dailySummaryPref?.isChecked = enabled
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.notificationSound.collect { sound ->
                    soundPref?.value = sound
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.vibrationPattern.collect { pattern ->
                    vibrationPref?.value = pattern
                }
            }
        }

        // User changes → ViewModel
        limitNotifPref?.setOnPreferenceChangeListener { _, newValue ->
            viewModel.setCounterLimitNotification(newValue as Boolean)
            true
        }

        dailySummaryPref?.setOnPreferenceChangeListener { _, newValue ->
            viewModel.setDailySummaryNotification(newValue as Boolean)
            true
        }

        soundPref?.setOnPreferenceChangeListener { _, newValue ->
            viewModel.setNotificationSound(newValue as String)
            true
        }

        vibrationPref?.setOnPreferenceChangeListener { _, newValue ->
            viewModel.setNotificationVibrationPattern(newValue as String)
            true
        }
    }
}