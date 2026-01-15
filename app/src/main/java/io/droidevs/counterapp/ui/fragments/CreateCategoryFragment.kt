package io.droidevs.counterapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import io.droidevs.counterapp.R
import io.droidevs.counterapp.ui.adapter.CategoryColorAdapter
import io.droidevs.counterapp.data.repository.CategoryColorProvider
import io.droidevs.counterapp.databinding.FragmentCreateCategoryBinding
import io.droidevs.counterapp.ui.decoration.GridSpacingItemDecoration
import io.droidevs.counterapp.ui.navigation.AppNavigator
import io.droidevs.counterapp.ui.vm.CreateCategoryViewModel
import io.droidevs.counterapp.ui.vm.actions.CreateCategoryAction
import io.droidevs.counterapp.ui.vm.events.CreateCategoryEvent
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class CreateCategoryFragment : Fragment() {

    private lateinit var binding: FragmentCreateCategoryBinding
    private val viewModel: CreateCategoryViewModel by viewModels()

    @Inject
    lateinit var appNavigator: AppNavigator

    private lateinit var adapter: CategoryColorAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupListeners()
        observeViewModel()

        if (viewModel.uiState.value.colors.isEmpty()) {
            val colors = CategoryColorProvider.generatePalette(
                context = requireContext(),
                numColors = 8
            )
            viewModel.onAction(CreateCategoryAction.LoadPalette(colors))
            
            val suggested = CategoryColorProvider.generateColorForCategory(
                context = requireContext(),
                categoryName = ""
            )
            viewModel.onAction(CreateCategoryAction.ColorSelected(suggested.colorInt))
        }
    }

    private fun setupRecyclerView() {
        adapter = CategoryColorAdapter(mutableListOf()) { color ->
            viewModel.onAction(CreateCategoryAction.ColorSelected(color))
        }

        binding.recyclerColors.layoutManager = GridLayoutManager(requireContext(), 5)
        binding.recyclerColors.adapter = adapter
        binding.recyclerColors.addItemDecoration(
            GridSpacingItemDecoration(resources.getDimensionPixelSize(R.dimen.color_spacing))
        )
    }

    private fun setupListeners() {
        binding.etCategoryName.doAfterTextChanged {
            val name = it?.toString().orEmpty()
            viewModel.onAction(CreateCategoryAction.NameChanged(name))
            
            val suggested = CategoryColorProvider.generateColorForCategory(
                context = requireContext(),
                categoryName = name.trim()
            )
            viewModel.onAction(CreateCategoryAction.ColorSelected(suggested.colorInt))
        }

        binding.btnCreateCategory.setOnClickListener {
            viewModel.onAction(CreateCategoryAction.CreateClicked)
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.uiState.collect { state ->
                        binding.tvPreviewName.text = state.name
                        binding.cardPreview.setCardBackgroundColor(state.selectedColor)
                        adapter.updateColors(state.colors)
                        binding.btnCreateCategory.isEnabled = !state.isSaving
                    }
                }

                launch {
                    viewModel.event.collect { event ->
                        when (event) {
                            is CreateCategoryEvent.CategoryCreated -> {
                                Toast.makeText(
                                    requireContext(),
                                    getString(R.string.category_created_message, event.name),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            is CreateCategoryEvent.ShowMessage -> {
                                binding.etCategoryName.error = event.message
                            }
                            CreateCategoryEvent.NavigateBack -> {
                                appNavigator.back()
                            }
                        }
                    }
                }
            }
        }
    }
}
