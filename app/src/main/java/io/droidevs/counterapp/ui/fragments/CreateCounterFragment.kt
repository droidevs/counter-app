@file:Suppress("DEPRECATION")

package io.droidevs.counterapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import io.droidevs.counterapp.R
import io.droidevs.counterapp.databinding.FragmentCreateCounterBinding
import io.droidevs.counterapp.ui.navigation.AppNavigator
import io.droidevs.counterapp.ui.utils.NoCategoryUi
import io.droidevs.counterapp.ui.vm.CreateCounterViewModel
import io.droidevs.counterapp.ui.vm.actions.CreateCounterAction
import io.droidevs.counterapp.ui.vm.events.CreateCounterEvent
import javax.inject.Inject
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CreateCounterFragment : Fragment() {

    private lateinit var binding: FragmentCreateCounterBinding
    private val viewModel: CreateCounterViewModel by viewModels()
    private lateinit var categoryAdapter: ArrayAdapter<String>

    private val noCategoryString by lazy { getString(NoCategoryUi.labelRes()) } // Define const

    @Inject
    lateinit var appNavigator: AppNavigator

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateCounterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupCategorySpinner()
        setupListeners()
        observeViewModel()
    }

    private fun setupCategorySpinner() {
        categoryAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, mutableListOf(noCategoryString))
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerCategory.adapter = categoryAdapter
    }

    private fun setupListeners() {
        binding.etCounterName.doAfterTextChanged {
            viewModel.onAction(CreateCounterAction.NameChanged(it.toString()))
        }

        binding.etInitialValue.doAfterTextChanged {
            val value = it?.toString()?.toIntOrNull() ?: 0
            viewModel.onAction(CreateCounterAction.InitialValueChanged(value))
        }

        binding.switchCanIncrease.setOnCheckedChangeListener { _, checked ->
            viewModel.onAction(CreateCounterAction.CanIncreaseChanged(checked))
        }

        binding.switchCanDecrease.setOnCheckedChangeListener { _, checked ->
            viewModel.onAction(CreateCounterAction.CanDecreaseChanged(checked))
        }

        binding.btnSave.setOnClickListener {
            viewModel.onAction(CreateCounterAction.SaveClicked)
        }

        binding.spinnerCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedCategoryName = categoryAdapter.getItem(position)
                if (selectedCategoryName == noCategoryString) {
                    viewModel.onAction(CreateCounterAction.CategorySelected(null))
                } else {
                    val selectedCategory = viewModel.uiState.value.categories.find { it.name == selectedCategoryName }
                    viewModel.onAction(CreateCounterAction.CategorySelected(selectedCategory?.id))
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                viewModel.onAction(CreateCounterAction.CategorySelected(null))
            }
        }

        binding.switchUseDefaultBehavior.setOnCheckedChangeListener { _, checked ->
            viewModel.onAction(CreateCounterAction.UseDefaultBehaviorChanged(checked))
        }

        binding.etIncrementStep.doAfterTextChanged {
            viewModel.onAction(CreateCounterAction.IncrementStepChanged(it?.toString().orEmpty()))
        }
        binding.etDecrementStep.doAfterTextChanged {
            viewModel.onAction(CreateCounterAction.DecrementStepChanged(it?.toString().orEmpty()))
        }
        binding.etDefaultValue.doAfterTextChanged {
            viewModel.onAction(CreateCounterAction.DefaultValueChanged(it?.toString().orEmpty()))
        }
        binding.etMinValue.doAfterTextChanged {
            viewModel.onAction(CreateCounterAction.MinValueChanged(it?.toString().orEmpty()))
        }
        binding.etMaxValue.doAfterTextChanged {
            viewModel.onAction(CreateCounterAction.MaxValueChanged(it?.toString().orEmpty()))
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.uiState.collect { state ->
                        if (binding.etCounterName.text.toString() != state.name) {
                            binding.etCounterName.setText(state.name)
                        }

                        if (binding.etInitialValue.text.toString() != state.initialValue.toString()) {
                            binding.etInitialValue.setText(state.initialValue.toString())
                        }

                        binding.switchCanIncrease.isChecked = state.canIncrease
                        binding.switchCanDecrease.isChecked = state.canDecrease
                        binding.btnSave.isEnabled = !state.isSaving
                        binding.etInitialValue.isVisible = state.isInitialValueInputVisible
                        binding.tvInitialValueDesc.text = if (state.isInitialValueInputVisible) "Initial value must be greater than 0" else "The counter will start at 0"


                        if (viewModel.isCategoryFixed) {
                            binding.spinnerCategory.isVisible = false
                            binding.tvCategoryChip.isVisible = true
                            val category = state.categories.find { it.id == state.categoryId }
                            val label = category?.name ?: getString(NoCategoryUi.labelRes())
                            binding.tvCategoryChip.text = getString(
                                R.string.label_category_chip,
                                label
                            )
                        } else {
                            binding.spinnerCategory.isVisible = true
                            binding.tvCategoryChip.isVisible = false
                            // Update adapter data
                            val categoryNames = state.categories.map { it.name }
                            categoryAdapter.clear()
                            categoryAdapter.add(noCategoryString)
                            categoryAdapter.addAll(categoryNames)
                            categoryAdapter.notifyDataSetChanged()
                            // Set selection
                            val selectedCategory = state.categories.find { it.id == state.categoryId }
                            val selectionPosition = if (selectedCategory != null) {
                                categoryNames.indexOf(selectedCategory.name) + 1 // +1 for 'No Category'
                            } else {
                                0 // 'No Category'
                            }

                            if (binding.spinnerCategory.selectedItemPosition != selectionPosition) {
                                binding.spinnerCategory.setSelection(selectionPosition)
                            }
                        }

                        // Hide/show overrides group.
                        binding.groupBehaviorOverrides.isVisible = !state.useDefaultBehavior

                        // Keep switch synced.
                        if (binding.switchUseDefaultBehavior.isChecked != state.useDefaultBehavior) {
                            binding.switchUseDefaultBehavior.isChecked = state.useDefaultBehavior
                        }

                        // Only bind text fields when visible (when hidden they are cleared by VM but cached).
                        if (!state.useDefaultBehavior) {
                            if (binding.etIncrementStep.text.toString() != state.incrementStepInput) {
                                binding.etIncrementStep.setText(state.incrementStepInput)
                            }
                            if (binding.etDecrementStep.text.toString() != state.decrementStepInput) {
                                binding.etDecrementStep.setText(state.decrementStepInput)
                            }
                            if (binding.etDefaultValue.text.toString() != state.defaultValueInput) {
                                binding.etDefaultValue.setText(state.defaultValueInput)
                            }
                            if (binding.etMinValue.text.toString() != state.minValueInput) {
                                binding.etMinValue.setText(state.minValueInput)
                            }
                            if (binding.etMaxValue.text.toString() != state.maxValueInput) {
                                binding.etMaxValue.setText(state.maxValueInput)
                            }
                        }
                    }
                }

                launch {
                    viewModel.event.collect { event ->
                        when (event) {
                            CreateCounterEvent.NavigateBack -> {
                                appNavigator.back()
                            }
                        }
                    }
                }
            }
        }
    }
}
