@file:Suppress("DEPRECATION")

package io.droidevs.counterapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import io.droidevs.counterapp.R
import io.droidevs.counterapp.databinding.EmptyStateLayoutBinding
import io.droidevs.counterapp.databinding.ErrorStateLayoutBinding
import io.droidevs.counterapp.databinding.FragmentCounterListBinding
import io.droidevs.counterapp.databinding.LoadingStateLayoutBinding
import io.droidevs.counterapp.ui.adapter.ListCounterAdapter
import io.droidevs.counterapp.ui.listeners.OnCounterClickListener
import io.droidevs.counterapp.ui.models.CounterUiModel
import io.droidevs.counterapp.ui.navigation.AppNavigator
import io.droidevs.counterapp.ui.vm.CountersListViewModel
import io.droidevs.counterapp.ui.vm.actions.CounterListAction
import io.droidevs.counterapp.ui.vm.events.CounterListEvent
import javax.inject.Inject
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import io.droidevs.counterapp.domain.display.DisplayPreferences
import io.droidevs.counterapp.domain.display.DisplayPreferencesProvider
import io.droidevs.counterapp.domain.result.dataOr

@AndroidEntryPoint
@Suppress("DEPRECATION")
class CounterListFragment : Fragment(), OnCounterClickListener {

    private var _binding: FragmentCounterListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CountersListViewModel by viewModels()

    private lateinit var listAdapter: ListCounterAdapter

    @Inject
    lateinit var appNavigator: AppNavigator

    @Inject
    lateinit var displayPreferencesProvider: DisplayPreferencesProvider

    @Suppress("DEPRECATION")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCounterListBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupFab()
        observeUiState()
        observeEvents()
        observeDisplayPreferences()
    }

    private fun setupRecyclerView() {
        listAdapter = ListCounterAdapter(
            listener = this,
            onIncrement = { counter ->
                viewModel.onAction(CounterListAction.IncrementCounter(counter))
            },
            onDecrement = { counter ->
                viewModel.onAction(CounterListAction.DecrementCounter(counter))
            },
        )

        binding.rvCounters.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = listAdapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(rv: RecyclerView, dx: Int, dy: Int) {
                    val lm = rv.layoutManager as LinearLayoutManager
                    val first = lm.findFirstVisibleItemPosition()
                    val last = lm.findLastVisibleItemPosition()

                    if (first == -1 || last == -1) return

                    val visibleItems = (first..last).mapNotNull { pos ->
                        listAdapter.getCounterAt(pos)
                    }.toSet()

                    if (visibleItems.isNotEmpty()) {
                        viewModel.onAction(CounterListAction.VisibleItemsChanged(visibleItems))
                    }
                }
            })
        }
    }

    private fun setupFab() {
        binding.fabAddCounter.setOnClickListener {
            viewModel.onAction(CounterListAction.AddCounterClicked)
        }
    }

    private fun observeUiState() {
        viewModel.uiState.onEach { uiState ->
            when {
                uiState.isLoading -> {
                    showLoadingState()
                    binding.rvCounters.isVisible = false
                    binding.fabAddCounter.isVisible = false
                }

                uiState.isError -> {
                    showErrorState()
                    binding.rvCounters.isVisible = false
                    binding.fabAddCounter.isVisible = false
                }

                uiState.counters.isEmpty() -> {
                    binding.stateContainer.isVisible = true
                    binding.rvCounters.isVisible = false
                    binding.fabAddCounter.isVisible = false
                    showEmptyState {
                        viewModel.onAction(CounterListAction.AddCounterClicked)
                    }
                }

                else -> {
                    binding.stateContainer.removeAllViews()
                    binding.stateContainer.isVisible = false
                    binding.rvCounters.isVisible = true
                    binding.fabAddCounter.isVisible = true
                    listAdapter.updateCounters(uiState.counters)
                }
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun observeEvents() {
        viewModel.event.onEach { event ->
            when (event) {
                is CounterListEvent.NavigateToCreateCounter -> {
                    appNavigator.navigate(
                        CounterListFragmentDirections.actionCounterListToCounterCreate(null)
                    )
                }

                is CounterListEvent.NavigateToCounterView -> {
                    appNavigator.navigate(
                        CounterListFragmentDirections.actionCounterListToCounterView(
                            event.counterId
                        )
                    )
                }
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun observeDisplayPreferences() {
        val defaults = DisplayPreferences(
            hideControls = false,
            hideLastUpdate = false,
            hideCounterCategoryLabel = false
        )

        displayPreferencesProvider.preferences()
            .onEach { result ->
                val prefs = result.dataOr { defaults }
                listAdapter.updateDisplayPreferences(prefs)
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun showLoadingState() {
        binding.stateContainer.removeAllViews()
        LoadingStateLayoutBinding.inflate(layoutInflater, binding.stateContainer, true)
        binding.stateContainer.isVisible = true
    }

    private fun showErrorState() {
        binding.stateContainer.removeAllViews()
        ErrorStateLayoutBinding.inflate(layoutInflater, binding.stateContainer, true)
        binding.stateContainer.isVisible = true
    }

    private fun showEmptyState(onAction: () -> Unit) {
        binding.stateContainer.removeAllViews()
        val emptyStateBinding = EmptyStateLayoutBinding.inflate(
            layoutInflater,
            binding.stateContainer,
            true
        )
        emptyStateBinding.icon.setImageResource(R.drawable.ic_counter)
        emptyStateBinding.titleText.setText(R.string.no_counters_message)
        emptyStateBinding.subtitleText.setText(R.string.counter_notes_hint)
        emptyStateBinding.createButton.setText(R.string.create_counter_title)
        emptyStateBinding.createButton.setOnClickListener { onAction() }
    }

    override fun onStop() {
        super.onStop()
        viewModel.onAction(CounterListAction.FlushAllPendingReorders)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @Deprecated("Deprecated in Java")
    @Suppress("DEPRECATION")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.findItem(R.id.menu_settings)?.isVisible = true
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onCounterClick(counter: CounterUiModel) {
        viewModel.onAction(CounterListAction.CounterClicked(counter))
    }
}
