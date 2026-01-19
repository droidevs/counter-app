@file:Suppress("DEPRECATION")

package io.droidevs.counterapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import io.droidevs.counterapp.R
import io.droidevs.counterapp.databinding.ErrorStateLayoutBinding
import io.droidevs.counterapp.databinding.FragmentCounterEditBinding
import io.droidevs.counterapp.databinding.LoadingStateLayoutBinding
import io.droidevs.counterapp.ui.navigation.AppNavigator
import io.droidevs.counterapp.ui.vm.CounterEditViewModel
import io.droidevs.counterapp.ui.vm.actions.CounterEditAction
import io.droidevs.counterapp.ui.vm.events.CounterEditEvent
import javax.inject.Inject
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CounterEditFragment : Fragment() {

    private lateinit var binding: FragmentCounterEditBinding

    private val viewModel: CounterEditViewModel by viewModels()

    @Inject
    lateinit var appNavigator: AppNavigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCounterEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListeners()
        observeViewModel()
    }

    private fun setupListeners() {
        with(binding) {
            etName.doAfterTextChanged {
                viewModel.onAction(CounterEditAction.UpdateName(it.toString()))
            }

            etCurrentCount.doAfterTextChanged {
                it.toString().toIntOrNull()?.let { count ->
                    viewModel.onAction(CounterEditAction.UpdateCurrentCount(count))
                }
            }

            switchCanIncrease.setOnCheckedChangeListener { _, checked ->
                viewModel.onAction(CounterEditAction.SetCanIncrease(checked))
            }

            switchCanDecrease.setOnCheckedChangeListener { _, checked ->
                viewModel.onAction(CounterEditAction.SetCanDecrease(checked))
            }

            switchUseDefaultBehavior.setOnCheckedChangeListener { _, checked ->
                viewModel.onAction(CounterEditAction.UseDefaultBehaviorChanged(checked))
            }

            etIncrementStep.doAfterTextChanged {
                viewModel.onAction(CounterEditAction.IncrementStepChanged(it?.toString().orEmpty()))
            }
            etDecrementStep.doAfterTextChanged {
                viewModel.onAction(CounterEditAction.DecrementStepChanged(it?.toString().orEmpty()))
            }
            etDefaultValue.doAfterTextChanged {
                viewModel.onAction(CounterEditAction.DefaultValueChanged(it?.toString().orEmpty()))
            }
            etMinValue.doAfterTextChanged {
                viewModel.onAction(CounterEditAction.MinValueChanged(it?.toString().orEmpty()))
            }
            etMaxValue.doAfterTextChanged {
                viewModel.onAction(CounterEditAction.MaxValueChanged(it?.toString().orEmpty()))
            }
        }
    }

    private fun showLoading() {
        binding.stateContainer.removeAllViews()
        LoadingStateLayoutBinding.inflate(layoutInflater, binding.stateContainer, true)
        binding.stateContainer.isVisible = true
    }

    private fun showError() {
        binding.stateContainer.removeAllViews()
        ErrorStateLayoutBinding.inflate(layoutInflater, binding.stateContainer, true)
        binding.stateContainer.isVisible = true
    }

    private fun hideState() {
        binding.stateContainer.removeAllViews()
        binding.stateContainer.isVisible = false
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.uiState.collect { state ->
                        when {
                            state.isLoading -> {
                                showLoading()
                                binding.content.isVisible = false
                            }

                            state.isError -> {
                                showError()
                                binding.content.isVisible = false
                            }

                            else -> {
                                hideState()
                                binding.content.isVisible = true

                                state.counter?.let { counter ->
                                    if (binding.etName.text.toString() != counter.name) {
                                        binding.etName.setText(counter.name)
                                    }
                                    if (binding.etCurrentCount.text.toString() != counter.currentCount.toString()) {
                                        binding.etCurrentCount.setText(counter.currentCount.toString())
                                    }

                                    if (binding.switchCanIncrease.isChecked != counter.canIncrease) {
                                        binding.switchCanIncrease.isChecked = counter.canIncrease
                                    }
                                    if (binding.switchCanDecrease.isChecked != counter.canDecrease) {
                                        binding.switchCanDecrease.isChecked = counter.canDecrease
                                    }

                                    // Hide/show override fields.
                                    binding.groupBehaviorOverrides.isVisible = !counter.useDefaultBehavior

                                    if (binding.switchUseDefaultBehavior.isChecked != counter.useDefaultBehavior) {
                                        binding.switchUseDefaultBehavior.isChecked = counter.useDefaultBehavior
                                    }

                                    if (!counter.useDefaultBehavior) {
                                        val incText = counter.incrementStep?.toString().orEmpty()
                                        val decText = counter.decrementStep?.toString().orEmpty()
                                        val defText = counter.defaultValue?.toString().orEmpty()
                                        val minText = counter.minValue?.toString().orEmpty()
                                        val maxText = counter.maxValue?.toString().orEmpty()

                                        if (binding.etIncrementStep.text.toString() != incText) binding.etIncrementStep.setText(incText)
                                        if (binding.etDecrementStep.text.toString() != decText) binding.etDecrementStep.setText(decText)
                                        if (binding.etDefaultValue.text.toString() != defText) binding.etDefaultValue.setText(defText)
                                        if (binding.etMinValue.text.toString() != minText) binding.etMinValue.setText(minText)
                                        if (binding.etMaxValue.text.toString() != maxText) binding.etMaxValue.setText(maxText)
                                    }

                                    binding.tvCreatedAt.text = getString(R.string.created_at_label, counter.createdTime.toString())
                                    binding.tvLastUpdatedAt.text = getString(R.string.last_updated_label, counter.editedTime.toString())
                                }
                            }
                        }
                    }
                }

                launch {
                    viewModel.event.collect { event ->
                        when (event) {
                            CounterEditEvent.NavigateBack -> {
                                appNavigator.back()
                            }
                        }
                    }
                }
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.findItem(R.id.menuSettings).isVisible = false
        inflater.inflate(R.menu.edit_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    @Deprecated("Deprecated in Java")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_save -> {
                viewModel.onAction(CounterEditAction.SaveClicked)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
