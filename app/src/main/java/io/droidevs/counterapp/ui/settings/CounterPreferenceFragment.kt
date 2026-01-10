package io.droidevs.counterapp.ui.settings

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.preference.EditTextPreference
import androidx.preference.PreferenceFragmentCompat
import io.droidevs.counterapp.R
import io.droidevs.counterapp.ui.vm.CounterPreferencesViewModel
import kotlinx.coroutines.launch

class CounterPreferencesFragment : PreferenceFragmentCompat() {

    private val viewModel: CounterPreferencesViewModel by viewModels()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.counter_preferences, rootKey)

        val incrementPref = findPreference<EditTextPreference>("increment_step")
        val defaultPref = findPreference<EditTextPreference>("default_value")
        val minPref = findPreference<EditTextPreference>("minimum_value")
        val maxPref = findPreference<EditTextPreference>("maximum_value")

        // Observe ViewModel → update UI
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.incrementStep.collect { value ->
                    incrementPref?.text = value.toString()
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.defaultValue.collect { value ->
                    defaultPref?.text = value.toString()
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.minimumValue.collect { value ->
                    minPref?.text = value?.toString() ?: ""
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.maximumValue.collect { value ->
                    maxPref?.text = value?.toString() ?: ""
                }
            }
        }

        // User changes → send to ViewModel
        incrementPref?.setOnPreferenceChangeListener { _, newValue ->
            val step = (newValue as? String)?.toIntOrNull() ?: 1
            viewModel.setIncrementStep(step.coerceAtLeast(1))
            true
        }

        defaultPref?.setOnPreferenceChangeListener { _, newValue ->
            val value = (newValue as? String)?.toIntOrNull() ?: 0
            viewModel.setDefaultValue(value)
            true
        }

        minPref?.setOnPreferenceChangeListener { _, newValue ->
            val min = (newValue as? String)?.toIntOrNull()
            viewModel.setMinimumValue(min)
            true
        }

        maxPref?.setOnPreferenceChangeListener { _, newValue ->
            val max = (newValue as? String)?.toIntOrNull()
            viewModel.setMaximumValue(max)
            true
        }
    }
}