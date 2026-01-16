package io.droidevs.counterapp.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.preference.EditTextPreference
import androidx.preference.PreferenceFragmentCompat
import dagger.hilt.android.AndroidEntryPoint
import io.droidevs.counterapp.R
import io.droidevs.counterapp.ui.vm.CounterPreferencesViewModel
import io.droidevs.counterapp.ui.vm.actions.CounterBehaviorPreferenceAction
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CounterPreferencesFragment : PreferenceFragmentCompat() {

    private val viewModel: CounterPreferencesViewModel by viewModels()

    private var incrementPref: EditTextPreference? = null
    private var decrementPref: EditTextPreference? = null
    private var defaultPref: EditTextPreference? = null
    private var minPref: EditTextPreference? = null
    private var maxPref: EditTextPreference? = null

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
        setPreferencesFromResource(R.xml.counter_preferences, rootKey)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        findPreferences()
        observeUiState()
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

                    incrementPref?.text = state.counterIncrementStep.toString()
                    decrementPref?.text = state.counterDecrementStep.toString()
                    defaultPref?.text = state.defaultCounterValue.toString()
                    minPref?.text = state.minimumCounterValue?.toString() ?: ""
                    maxPref?.text = state.maximumCounterValue?.toString() ?: ""
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
