package io.droidevs.counterapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.droidevs.counterapp.R
import io.droidevs.counterapp.ui.adapter.HomeCategoryAdapter
import io.droidevs.counterapp.ui.adapter.HomeCounterAdapter
import io.droidevs.counterapp.databinding.FragmentHomeBinding
import io.droidevs.counterapp.ui.listeners.OnCategoryClickListener
import io.droidevs.counterapp.ui.models.CounterUiModel
import io.droidevs.counterapp.ui.listeners.OnCounterClickListener
import io.droidevs.counterapp.ui.models.CategoryUiModel
import dagger.hilt.android.AndroidEntryPoint
import io.droidevs.counterapp.ui.vm.HomeViewModel
import io.droidevs.counterapp.ui.fragments.CounterListFragmentDirections
import io.droidevs.counterapp.ui.fragments.CategoryListFragmentDirections
import io.droidevs.counterapp.ui.navigation.AppNavigator
import io.droidevs.counterapp.ui.vm.actions.HomeAction
import io.droidevs.counterapp.ui.vm.events.HomeEvent
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModels()

    lateinit var binding : FragmentHomeBinding

    @Inject
    lateinit var appNavigator: AppNavigator

    var recentCountersRecycler : RecyclerView? = null
    var categoriesRecycler : RecyclerView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater)

        recentCountersRecycler = binding.recyclerRecentCounters
        categoriesRecycler = binding.recyclerCategories
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerViews()
        setUpButtons()
        observeViewModel()
    }

    private fun setUpRecyclerViews() {
        recentCountersRecycler?.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recentCountersRecycler?.adapter = HomeCounterAdapter(
            counters = mutableListOf(),
            listener = object : OnCounterClickListener {
                override fun onCounterClick(counter: CounterUiModel) {
                    viewModel.onAction(HomeAction.CounterClicked(counter))
                }
            },
            onAddCounter = {
                viewModel.onAction(HomeAction.AddCounterClicked)
            },
            onIncrement = { counter ->
                viewModel.onAction(HomeAction.IncrementCounter(counter.counter))
            },
            onDecrement = { counter ->
                viewModel.onAction(HomeAction.DecrementCounter(counter.counter))
            }
        )
        categoriesRecycler?.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        categoriesRecycler?.adapter = HomeCategoryAdapter(
            listener = object : OnCategoryClickListener {
                override fun onCategoryClick(category: CategoryUiModel) {
                    viewModel.onAction(HomeAction.CategoryClicked(category))
                }
            },
            onAdd = {
                viewModel.onAction(HomeAction.AddCategoryClicked)
            }
        )
    }

    private fun setUpButtons() {
        binding.txtViewAllCounters.setOnClickListener { _ ->
            viewModel.onAction(HomeAction.ViewAllCountersClicked)
        }

        binding.txtViewAllCategories.setOnClickListener { _ ->
            viewModel.onAction(HomeAction.ViewAllCategoriesClicked)
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.uiState.collect { state ->
                        (recentCountersRecycler?.adapter as? HomeCounterAdapter)?.updateCounters(state.recentCounters)
                        (categoriesRecycler?.adapter as? HomeCategoryAdapter)?.submitList(state.categories)
                        // You can update loading indicators or empty states here if needed
                        Log.d("HomeFragment", "Counters Count: ${state.countersCount}, Categories Count: ${state.categoriesCount}")
                    }
                }

                launch {
                    viewModel.event.collect { event ->
                        when (event) {
                            is HomeEvent.NavigateToCounterView -> {
                                appNavigator.navigate(R.id.action_to_counters_graph)
                                appNavigator.navigate(
                                    CounterListFragmentDirections.actionCounterListToCounterView(
                                        event.counterId
                                    )
                                )
                            }
                            HomeEvent.NavigateToCreateCounter -> {
                                appNavigator.navigate(R.id.action_to_counters_graph)
                                appNavigator.navigate(R.id.action_counterList_to_counterCreate)
                            }
                            is HomeEvent.NavigateToCategoryView -> {
                                appNavigator.navigate(R.id.action_to_categories_graph)
                                appNavigator.navigate(
                                    CategoryListFragmentDirections.actionCategoryListToCategoryView(
                                        event.categoryId
                                    )
                                )
                            }
                            HomeEvent.NavigateToCreateCategory -> {
                                appNavigator.navigate(R.id.action_to_categories_graph)
                                appNavigator.navigate(R.id.action_categoryList_to_categoryCreate)
                            }
                            HomeEvent.NavigateToCounterList -> {
                                appNavigator.navigate(R.id.action_to_counters_graph)
                            }
                            HomeEvent.NavigateToCategoryList -> {
                                appNavigator.navigate(R.id.action_to_categories_graph)
                            }
                            is HomeEvent.ShowMessage -> {
                                // Handle showing toast or snackbar
                            }
                        }
                    }
                }
            }
        }
    }

}
