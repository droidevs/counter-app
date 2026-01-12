package io.droidevs.counterapp.ui.settings

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.preference.EditTextPreference
import androidx.preference.PreferenceFragmentCompat
import com.google.android.material.snackbar.Snackbar
import io.droidevs.counterapp.R
import io.droidevs.counterapp.ui.vm.CounterPreferencesViewModel
import io.droidevs.counterapp.ui.vm.actions.CounterBehaviorPreferenceAction
import io.droidevs.counterapp.ui.vm.events.CounterBehaviorPreferenceEvent
import kotlinx.coroutines.launch

class CounterPreferencesFragment : PreferenceFragmentCompat() {

    private val viewModel: CounterPreferencesViewModel by viewModels()
    private var isInitializing = true

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.counter_preferences, rootKey)

        val incrementPref = findPreference<EditTextPreference>("increment_step")
        val decrementPref = findPreference<EditTextPreference>("decrement_step")
        val defaultPref = findPreference<EditTextPreference>("default_value")
        val minPref = findPreference<EditTextPreference>("minimum_value")
        val maxPref = findPreference<EditTextPreference>("maximum_value")

        // Observe ViewModel UI State → update UI
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    if (isInitializing) {
                        // Set initial values without triggering listeners
                        incrementPref?.text = state.counterIncrementStep.toString()
                        decrementPref?.text = state.counterDecrementStep.toString()
                        defaultPref?.text = state.defaultCounterValue.toString()
                        minPref?.text = state.minimumCounterValue?.toString() ?: ""
                        maxPref?.text = state.maximumCounterValue?.toString() ?: ""
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
                        is CounterBehaviorPreferenceEvent.ShowMessage -> {
                            // Show snackbar or toast
                            Snackbar.make(requireView(), event.message, Snackbar.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

        // User changes → send to ViewModel via onAction
        incrementPref?.setOnPreferenceChangeListener { _, newValue ->
            if (!isInitializing) {
                val step = (newValue as? String)?.toIntOrNull() ?: 1
                viewModel.onAction(
                    CounterBehaviorPreferenceAction.SetCounterIncrementStep(step.coerceAtLeast(1))
                )
            }
            true
        }

        decrementPref?.setOnPreferenceChangeListener { _, newValue ->
            if (!isInitializing) {
                val step = (newValue as? String)?.toIntOrNull() ?: 1
                viewModel.onAction(
                    CounterBehaviorPreferenceAction.SetCounterDecrementStep(step.coerceAtLeast(1))
                )
            }
            true
        }

        defaultPref?.setOnPreferenceChangeListener { _, newValue ->
            if (!isInitializing) {
                val value = (newValue as? String)?.toIntOrNull() ?: 0
                viewModel.onAction(
                    CounterBehaviorPreferenceAction.SetDefaultCounterValue(value)
                )
            }
            true
        }

        minPref?.setOnPreferenceChangeListener { _, newValue ->
            if (!isInitializing) {
                val min = (newValue as? String)?.toIntOrNull()
                viewModel.onAction(
                    CounterBehaviorPreferenceAction.SetMinimumCounterValue(min)
                )
            }
            true
        }

        maxPref?.setOnPreferenceChangeListener { _, newValue ->
            if (!isInitializing) {
                val max = (newValue as? String)?.toIntOrNull()
                viewModel.onAction(
                    CounterBehaviorPreferenceAction.SetMaximumCounterValue(max)
                )
            }
            true
        }
    }
}
