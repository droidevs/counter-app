package io.droidevs.counterapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import io.droidevs.counterapp.databinding.FragmentCreateCounterBinding
import io.droidevs.counterapp.ui.fragments.CreateCounterFragmentArgs
import io.droidevs.counterapp.ui.models.CategoryUiModel
import io.droidevs.counterapp.ui.vm.CreateCounterViewModel
import io.droidevs.counterapp.ui.vm.actions.CreateCounterAction
import io.droidevs.counterapp.ui.vm.events.CreateCounterEvent
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CreateCounterFragment : Fragment() {

    private lateinit var binding: FragmentCreateCounterBinding

    private val viewModel : CreateCounterViewModel by viewModels()

    private var categoryAdapter: ArrayAdapter<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
        categoryAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            mutableListOf()
        )

        categoryAdapter?.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        binding.spinnerCategory.adapter = categoryAdapter

        binding.spinnerCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedCategoryName = binding.spinnerCategory.adapter.getItem(position) as String
                val selectedCategory = viewModel.uiState.value.categories.find { it.name == selectedCategoryName }
                viewModel.onAction(CreateCounterAction.CategorySelected(selectedCategory?.id))
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                viewModel.onAction(CreateCounterAction.CategorySelected(null))
            }
        }
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
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.uiState.collect { state ->
                        binding.etCounterName.setText(state.name)
                        binding.switchCanIncrease.isChecked = state.canIncrease
                        binding.switchCanDecrease.isChecked = state.canDecrease

                        binding.spinnerCategory.isVisible = state.categoryId == null
                        categoryAdapter?.clear()
                        categoryAdapter?.addAll(state.categories.map { it.name })
                        categoryAdapter?.notifyDataSetChanged()

                        state.categoryId?.let { id ->
                            val selectedCategory = state.categories.find { it.id == id }
                            val position = state.categories.indexOf(selectedCategory)
                            if (position != -1 && binding.spinnerCategory.selectedItemPosition != position) {
                                binding.spinnerCategory.setSelection(position)
                            }
                        }
                        binding.btnSave.isEnabled = !state.isSaving
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
