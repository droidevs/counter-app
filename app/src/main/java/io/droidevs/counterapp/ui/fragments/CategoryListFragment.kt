package io.droidevs.counterapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import io.droidevs.counterapp.R
import io.droidevs.counterapp.databinding.EmptyStateLayoutBinding
import io.droidevs.counterapp.ui.adapter.CategoryListAdapter
import io.droidevs.counterapp.databinding.FragmentCategoryListBinding
import io.droidevs.counterapp.databinding.LoadingStateLayoutBinding
import io.droidevs.counterapp.ui.fragments.CategoryListFragmentArgs
import io.droidevs.counterapp.ui.fragments.CategoryListFragmentDirections
import io.droidevs.counterapp.ui.listeners.OnCategoryClickListener
import io.droidevs.counterapp.ui.models.CategoryUiModel
import io.droidevs.counterapp.ui.navigation.AppNavigator
import io.droidevs.counterapp.ui.vm.CategoryListViewModel
import io.droidevs.counterapp.ui.vm.actions.CategoryListAction
import io.droidevs.counterapp.ui.vm.events.CategoryListEvent
import io.droidevs.counterapp.ui.vm.states.CategoryListUiState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject 

@AndroidEntryPoint
class CategoryListFragment : Fragment() {

    private lateinit var binding: FragmentCategoryListBinding
    private lateinit var adapter: CategoryListAdapter
    private val viewModel: CategoryListViewModel by viewModels()

    @Inject
    lateinit var appNavigator: AppNavigator

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCategoryListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val isSystem = arguments?.let { CategoryListFragmentArgs.fromBundle(it).isSystem } ?: false

        // Set initial system mode in ViewModel
        viewModel.onAction(CategoryListAction.SetSystemMode(isSystem))

        setupRecyclerView()
        setupListeners()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        adapter = CategoryListAdapter(
            listener = object : OnCategoryClickListener {
                override fun onCategoryClick(category: CategoryUiModel) {
                    viewModel.onAction(CategoryListAction.CategoryClicked(category))
                }
            }
        )

        binding.rvCategories.layoutManager = LinearLayoutManager(requireContext())
        binding.rvCategories.adapter = adapter
        binding.rvCategories.setHasFixedSize(true)
    }

    private fun setupListeners() {
//        binding.fabAction.setOnClickListener {
//            viewModel.onAction(CategoryListAction.CreateCategoryClicked)
//        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                // Observe UI State
                launch {
                    viewModel.uiState.collect { state ->
                        updateUi(state)
                    }
                }

                // Observe Events
                launch {
                    viewModel.event.collect { event ->
                        handleEvent(event)
                    }
                }
            }
        }
    }

    private fun updateUi(state: CategoryListUiState) {
        when {
            state.isLoading -> {
                showLoading()
            }
            state.categories.isEmpty() -> {
                showEmptyState {
                    viewModel.onAction(CategoryListAction.CreateCategoryClicked)
                }
            }
            else -> {
                hideStateContainer()
                adapter.submitList(state.categories)
            }
        }
    }

    private fun showLoading() {
        binding.rvCategories.visibility = View.GONE
        binding.stateContainer.visibility = View.VISIBLE
        binding.stateContainer.removeAllViews()

        val loadingBinding = LoadingStateLayoutBinding.inflate(
            layoutInflater,
            binding.stateContainer,
            false
        )
        binding.stateContainer.addView(loadingBinding.root)
    }

    private fun showEmptyState(onAction: () -> Unit) {
        binding.rvCategories.visibility = View.GONE
        binding.stateContainer.visibility = View.VISIBLE
        binding.stateContainer.removeAllViews()

        val emptyBinding = EmptyStateLayoutBinding.inflate(
            layoutInflater,
            binding.stateContainer,
            false
        )
        emptyBinding.icon.setImageResource(R.drawable.ic_category)
        emptyBinding.titleText.setText(R.string.empty_categories_title)
        emptyBinding.subtitleText.setText(R.string.empty_categories_message)
        emptyBinding.createButton.setText(R.string.action_create_category)
        emptyBinding.createButton.setOnClickListener { onAction() }

        binding.stateContainer.addView(emptyBinding.root)
    }

    private fun hideStateContainer() {
        binding.stateContainer.removeAllViews()
        binding.rvCategories.visibility = View.VISIBLE
        binding.stateContainer.visibility = View.GONE
    }

    private fun handleEvent(event: CategoryListEvent) {
        when (event) {
            is CategoryListEvent.NavigateToCategoryView -> {
                appNavigator.navigate(
                    CategoryListFragmentDirections.actionCategoryListToCategoryView(
                        event.categoryId
                    )
                )
            }
            is CategoryListEvent.NavigateToCreateCategory -> {
                appNavigator.navigate(R.id.action_categoryList_to_categoryCreate)
            }

            is CategoryListEvent.ShowMessage -> {
                Toast.makeText(requireContext(), event.message, Toast.LENGTH_SHORT).show()
            }
        }
    }
}
