package io.droidevs.counterapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import io.droidevs.counterapp.R
import io.droidevs.counterapp.databinding.ErrorStateLayoutBinding
import io.droidevs.counterapp.databinding.FragmentHomeBinding
import io.droidevs.counterapp.databinding.LoadingStateLayoutBinding
import io.droidevs.counterapp.ui.adapter.HomeCategoryAdapter
import io.droidevs.counterapp.ui.adapter.HomeCounterAdapter
import io.droidevs.counterapp.ui.listeners.OnCategoryClickListener
import io.droidevs.counterapp.ui.listeners.OnCounterClickListener
import io.droidevs.counterapp.ui.models.CategoryUiModel
import io.droidevs.counterapp.ui.models.CounterUiModel
import io.droidevs.counterapp.ui.navigation.AppNavigator
import io.droidevs.counterapp.ui.navigation.tabs.Tab
import io.droidevs.counterapp.ui.navigation.tabs.TabHost
import io.droidevs.counterapp.ui.vm.HomeViewModel
import io.droidevs.counterapp.ui.vm.actions.HomeAction
import io.droidevs.counterapp.ui.vm.events.HomeEvent
import io.droidevs.counterapp.domain.display.DisplayPreferencesProvider
import io.droidevs.counterapp.domain.result.Result
import io.droidevs.counterapp.domain.result.recoverWith
import javax.inject.Inject
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModels()

    lateinit var binding: FragmentHomeBinding

    var recentCountersRecycler: RecyclerView? = null
    var categoriesRecycler: RecyclerView? = null

    @Inject
    lateinit var appNavigator: AppNavigator

    @Inject
    lateinit var displayPreferencesProvider: DisplayPreferencesProvider

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
        observeDisplayPreferences()
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
        binding.txtViewAllCounters.setOnClickListener {
            viewModel.onAction(HomeAction.ViewAllCountersClicked)
        }

        binding.txtViewAllCategories.setOnClickListener {
            viewModel.onAction(HomeAction.ViewAllCategoriesClicked)
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
                            }

                            state.isError -> {
                                showError()
                            }

                            else -> {
                                hideState()
                            }
                        }

                        (recentCountersRecycler?.adapter as? HomeCounterAdapter)?.updateCounters(state.recentCounters)
                        (categoriesRecycler?.adapter as? HomeCategoryAdapter)?.submitUiModels(state.categories)
                        Log.d("HomeFragment", "Counters Count: ${state.countersCount}, Categories Count: ${state.categoriesCount}")
                    }
                }

                launch {
                    viewModel.event.collect { event ->
                        when (event) {
                            is HomeEvent.NavigateToCounterView -> {
                                // Ensure we are on the Counters tab, then navigate within its graph.
                                (activity as? TabHost)?.switchToTabAndNavigate(
                                    tab = Tab.COUNTERS,
                                    destinationId = R.id.counterViewFragment,
                                    args = bundleOf("counterId" to event.counterId)
                                )
                            }

                            HomeEvent.NavigateToCreateCounter -> {
                                (activity as? TabHost)?.switchToTabAndNavigate(
                                    tab = Tab.COUNTERS,
                                    destinationId = R.id.counterCreateFragment,
                                    args = null
                                )
                            }

                            is HomeEvent.NavigateToCategoryView -> {
                                (activity as? TabHost)?.switchToTabAndNavigate(
                                    tab = Tab.CATEGORIES,
                                    destinationId = R.id.categoryViewFragment,
                                    args = bundleOf("categoryId" to event.categoryId)
                                )
                            }

                            HomeEvent.NavigateToCreateCategory -> {
                                (activity as? TabHost)?.switchToTabAndNavigate(
                                    tab = Tab.CATEGORIES,
                                    destinationId = R.id.createCategoryFragment,
                                    args = null
                                )
                            }

                            HomeEvent.NavigateToCounterList -> {
                                (activity as? TabHost)?.switchToTabRoot(Tab.COUNTERS)
                            }

                            HomeEvent.NavigateToCategoryList -> {
                                // Ensure user categories list (not system mode) is shown.
                                (activity as? TabHost)?.switchToTabRoot(
                                    tab = Tab.CATEGORIES,
                                    args = bundleOf("isSystem" to false)
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    private fun observeDisplayPreferences() {
        displayPreferencesProvider.preferences()
            .recoverWith {
                // On preference read failure, keep safe defaults.
                Result.Success(
                    io.droidevs.counterapp.domain.display.DisplayPreferences(
                        hideControls = false,
                        hideLastUpdate = false,
                        hideCounterCategoryLabel = false
                    )
                )
            }
            .onEach { result ->
                val prefs = (result as Result.Success).data
                (recentCountersRecycler?.adapter as? HomeCounterAdapter)?.updateDisplayPreferences(prefs)
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

}
