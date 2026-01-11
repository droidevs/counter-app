package io.droidevs.counterapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import io.droidevs.counterapp.R
import io.droidevs.counterapp.databinding.EmptyStateLayoutBinding
import io.droidevs.counterapp.ui.adapter.ListCounterAdapter
import io.droidevs.counterapp.databinding.FragmentCounterListBinding
import io.droidevs.counterapp.ui.models.CounterUiModel
import io.droidevs.counterapp.ui.listeners.OnCounterClickListener
import io.droidevs.counterapp.ui.vm.CountersListViewModel
import io.droidevs.counterapp.ui.vm.actions.CounterListAction
import io.droidevs.counterapp.ui.vm.events.CounterListEvent
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class CounterListFragment : Fragment(), OnCounterClickListener {

    private var _binding: FragmentCounterListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CountersListViewModel by viewModels()

    private lateinit var listAdapter: ListCounterAdapter

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
    }

    private fun setupRecyclerView() {
        listAdapter = ListCounterAdapter(
            listener = this,
            onIncrement = { counter ->
                viewModel.onAction(CounterListAction.IncrementCounter(counter))
            },
            onDecrement = { counter ->
                viewModel.onAction(CounterListAction.DecrementCounter(counter))
            }
        )

        binding.rvCounters.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = listAdapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(rv: RecyclerView, dx: Int, dy: Int) {
                    val layoutManager = rv.layoutManager as LinearLayoutManager
                    val first = layoutManager.findFirstVisibleItemPosition()
                    val last = layoutManager.findLastVisibleItemPosition()

                    if (first == -1 || last == -1) return

                    val visibleItems = (first..last).mapNotNull {
                        listAdapter.counters.getOrNull(it)?.counter
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
            // todo : binding.progressBar.isVisible = uiState.isLoading
            if (!uiState.isLoading) {
                if (uiState.counters.isEmpty()) {
                    binding.rvCounters.isVisible = false
                    binding.fabAddCounter.isVisible = false
                    binding.stateContainer.isVisible = true
                    showEmptyState {
                        viewModel.onAction(CounterListAction.AddCounterClicked)
                    }
                } else {
                    binding.rvCounters.isVisible = true
                    binding.fabAddCounter.isVisible = true
                    binding.stateContainer.removeAllViews()
                    binding.stateContainer.isVisible = false
                    listAdapter.updateCounters(uiState.counters)
                }
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun observeEvents() {
        viewModel.event.onEach { event ->
            when (event) {
                is CounterListEvent.NavigateToCreateCounter -> {
                    findNavController().navigate(R.id.action_counterList_to_counterCreate)
                }

                is CounterListEvent.NavigateToCounterView -> {
                    findNavController().navigate(
                        CounterListFragmentDirections.actionCounterListToCounterView(
                            event.counterId
                        )
                    )
                }
                is CounterListEvent.ShowMessage -> {
                    Toast.makeText(requireContext(), event.message, Toast.LENGTH_SHORT).show()
                }
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.findItem(R.id.menu_settings)?.isVisible = true
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onCounterClick(counter: CounterUiModel) {
        viewModel.onAction(CounterListAction.CounterClicked(counter))
    }
}
