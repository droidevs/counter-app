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
import io.droidevs.counterapp.data.Theme
import io.droidevs.counterapp.ui.vm.DisplayPreferencesViewModel
import io.droidevs.counterapp.ui.vm.actions.DisplayPreferenceAction
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DisplayPreferencesFragment : PreferenceFragmentCompat() {

    private val viewModel: DisplayPreferencesViewModel by viewModels()

    private var themePref: ListPreference? = null
    private var hideControlsPref: SwitchPreferenceCompat? = null
    private var hideLastUpdatePref: SwitchPreferenceCompat? = null
    private var hideCounterCategoryLabelPref: SwitchPreferenceCompat? = null

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
        setPreferencesFromResource(R.xml.display_preferences, rootKey)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        findPreferences()
        observeUiState()
        setupPreferenceListeners()
    }

    private fun findPreferences() {
        themePref = findPreference("theme")
        hideControlsPref = findPreference("hide_controls")
        hideLastUpdatePref = findPreference("hide_last_update")
        hideCounterCategoryLabelPref = findPreference("hide_counter_category_label")
    }

    private fun setupPreferenceListeners() {
        themePref?.setOnPreferenceChangeListener { _, newValue ->
            val themeString = newValue as String
            val theme = Theme.getCurrent(id = themeString)
            viewModel.onAction(DisplayPreferenceAction.SetTheme(theme))
            true
        }

        hideControlsPref?.setOnPreferenceChangeListener { _, newValue ->
            viewModel.onAction(DisplayPreferenceAction.SetHideControls(newValue as Boolean))
            true
        }

        hideLastUpdatePref?.setOnPreferenceChangeListener { _, newValue ->
            viewModel.onAction(DisplayPreferenceAction.SetHideLastUpdate(newValue as Boolean))
            true
        }

        hideCounterCategoryLabelPref?.setOnPreferenceChangeListener { _, newValue ->
            viewModel.onAction(DisplayPreferenceAction.SetHideCounterCategoryLabel(newValue as Boolean))
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

                    themePref?.value = when (state.theme) {
                        Theme.SYSTEM -> "system"
                        Theme.LIGHT -> "light"
                        Theme.DARK -> "dark"
                        Theme.AUTO -> "auto"
                    }
                    hideControlsPref?.isChecked = state.hideControls
                    hideLastUpdatePref?.isChecked = state.hideLastUpdate
                    hideCounterCategoryLabelPref?.isChecked = state.hideCounterCategoryLabel
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
