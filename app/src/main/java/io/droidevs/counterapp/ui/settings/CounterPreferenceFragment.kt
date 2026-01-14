package io.droidevs.counterapp.ui.settings

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.preference.EditTextPreference
import androidx.preference.PreferenceFragmentCompat
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import io.droidevs.counterapp.R
import io.droidevs.counterapp.ui.vm.CounterPreferencesViewModel
import io.droidevs.counterapp.ui.vm.actions.CounterBehaviorPreferenceAction
import io.droidevs.counterapp.ui.vm.events.CounterBehaviorPreferenceEvent
import kotlinx.coroutines.launch


@AndroidEntryPoint
class CounterPreferencesFragment : PreferenceFragmentCompat() {

    private val viewModel: CounterPreferencesViewModel by viewModels()

    private var incrementPref: EditTextPreference? = null
    private var decrementPref: EditTextPreference? = null
    private var defaultPref: EditTextPreference? = null
    private var minPref: EditTextPreference? = null
    private var maxPref: EditTextPreference? = null

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.counter_preferences, rootKey)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        findPreferences()
        observeUiState()
        observeEvents()
        setupPreferenceListeners()
    }

    private fun findPreferences() {
        incrementPref = findPreference("increment_step")
        decrementPref = findPreference("decrement_step")
        defaultPref = findPreference("default_value")
        minPref = findPreference("minimum_value")
        maxPref = findPreference("maximum_value")
    }

    private fun setupPreferenceListeners() {
        incrementPref?.setOnPreferenceChangeListener { _, newValue ->
            val step = (newValue as? String)?.toIntOrNull() ?: 1
            viewModel.onAction(
                CounterBehaviorPreferenceAction.SetCounterIncrementStep(step.coerceAtLeast(1))
            )
            true
        }

        decrementPref?.setOnPreferenceChangeListener { _, newValue ->
            val step = (newValue as? String)?.toIntOrNull() ?: 1
            viewModel.onAction(
                CounterBehaviorPreferenceAction.SetCounterDecrementStep(step.coerceAtLeast(1))
            )
            true
        }

        defaultPref?.setOnPreferenceChangeListener { _, newValue ->
            val value = (newValue as? String)?.toIntOrNull() ?: 0
            viewModel.onAction(
                CounterBehaviorPreferenceAction.SetDefaultCounterValue(value)
            )
            true
        }

        minPref?.setOnPreferenceChangeListener { _, newValue ->
            val min = (newValue as? String)?.toIntOrNull()
            viewModel.onAction(
                CounterBehaviorPreferenceAction.SetMinimumCounterValue(min)
            )
            true
        }

        maxPref?.setOnPreferenceChangeListener { _, newValue ->
            val max = (newValue as? String)?.toIntOrNull()
            viewModel.onAction(
                CounterBehaviorPreferenceAction.SetMaximumCounterValue(max)
            )
            true
        }
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    incrementPref?.text = state.counterIncrementStep.toString()
                    decrementPref?.text = state.counterDecrementStep.toString()
                    defaultPref?.text = state.defaultCounterValue.toString()
                    minPref?.text = state.minimumCounterValue?.toString() ?: ""
                    maxPref?.text = state.maximumCounterValue?.toString() ?: ""
                }
            }
        }
    }

    private fun observeEvents() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.event.collect { event ->
                    when (event) {
                        is CounterBehaviorPreferenceEvent.ShowMessage -> {
                            Snackbar.make(requireView(), event.message, Snackbar.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }
}