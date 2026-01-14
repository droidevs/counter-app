package io.droidevs.counterapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import io.droidevs.counterapp.databinding.FragmentCreateCounterBinding
import io.droidevs.counterapp.ui.vm.CreateCounterViewModel
import io.droidevs.counterapp.ui.vm.actions.CreateCounterAction
import io.droidevs.counterapp.ui.vm.events.CreateCounterEvent
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CreateCounterFragment : Fragment() {

    private lateinit var binding: FragmentCreateCounterBinding
    private val viewModel: CreateCounterViewModel by viewModels()
    private lateinit var categoryAdapter: ArrayAdapter<String>

    private val noCategoryString by lazy { "No Category" } // Define const

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
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.uiState.collect { state ->
                        if (binding.etCounterName.text.toString() != state.name) {
                            binding.etCounterName.setText(state.name)
                        }
                        binding.switchCanIncrease.isChecked = state.canIncrease
                        binding.switchCanDecrease.isChecked = state.canDecrease
                        binding.btnSave.isEnabled = !state.isSaving

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
                }

                launch {
                    viewModel.event.collect { event ->
                        when (event) {
                            is CreateCounterEvent.CounterCreated -> {
                                Toast.makeText(requireContext(), "Counter \"${event.name}\" saved", Toast.LENGTH_SHORT).show()
                            }
                            is CreateCounterEvent.ShowMessage -> {
                                binding.etCounterName.error = event.message
                            }
                            CreateCounterEvent.NavigateBack -> {
                                findNavController().popBackStack()
                            }
                        }
                    }
                }
            }
        }
    }
}
