package io.droidevs.counterapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import io.droidevs.counterapp.R
import io.droidevs.counterapp.databinding.EmptyStateLayoutBinding
import io.droidevs.counterapp.ui.adapter.CategoryCountersAdapter
import io.droidevs.counterapp.databinding.FragmentViewCategoryBinding
import io.droidevs.counterapp.ui.vm.CategoryViewViewModel
import io.droidevs.counterapp.ui.vm.actions.CategoryViewAction
import io.droidevs.counterapp.ui.vm.events.CategoryViewEvent
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ViewCategoryFragment : Fragment() {

    private lateinit var binding: FragmentViewCategoryBinding
    private lateinit var adapter: CategoryCountersAdapter

    private val viewModel : CategoryViewViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentViewCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = CategoryCountersAdapter()
        binding.rvCounters.layoutManager = LinearLayoutManager(requireContext())
        binding.rvCounters.adapter = adapter

        binding.fabAddCounter.setOnClickListener {
            viewModel.onAction(CategoryViewAction.AddCounterClicked)
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.uiState.collect { state ->
                        state.category?.let {
                            binding.tvCategoryName.text = it.name
                            binding.tvCountersCount.text = getString(R.string.counters_count_label, it.countersCount)
                        }
                        
                        adapter.submitList(state.counters)
                        
                        if (state.showEmptyState) {
                            binding.rvCounters.visibility = View.GONE
                            binding.fabAddCounter.visibility = View.GONE
                            binding.stateContainer.visibility = VISIBLE
                            showEmptyState {
                                viewModel.onAction(CategoryViewAction.AddCounterClicked)
                            }
                        } else {
                            binding.rvCounters.visibility = VISIBLE
                            binding.fabAddCounter.visibility = VISIBLE
                            binding.stateContainer.visibility = View.GONE
                            binding.stateContainer.removeAllViews()
                        }
                    }
                }

                launch {
                    viewModel.event.collect { event ->
                        when (event) {
                            CategoryViewEvent.NavigateBack -> {
                                findNavController().navigateUp()
                            }
                            is CategoryViewEvent.NavigateToCreateCounter -> {
                                findNavController().navigate(
                                    ViewCategoryFragmentDirections.actionCategoryViewToCounterCreate(
                                        event.categoryId
                                    )
                                )
                            }
                            is CategoryViewEvent.ShowMessage -> {
                                Toast.makeText(requireContext(), event.message, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.findItem(R.id.menuSettings).isVisible = false
        inflater.inflate(R.menu.menu_category_view, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.action_delete_category -> {
                viewModel.onAction(CategoryViewAction.DeleteCategoryClicked)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showEmptyState(
        onAction: () -> Unit,
    ) {
        val binding = EmptyStateLayoutBinding.inflate(
            layoutInflater,
            this.binding.stateContainer,
            false
        )
        this.binding.stateContainer.addView(binding.root)
        binding.icon.setImageResource(R.drawable.ic_counter)
        binding.titleText.setText(R.string.empty_category_counters_title)
        binding.subtitleText.setText(R.string.empty_category_counters_message)
        binding.createButton.setText(R.string.action_create_counter)

        binding.createButton.setOnClickListener { onAction() }
        binding.root.isVisible = true
    }
}
