@file:Suppress("DEPRECATION")

package io.droidevs.counterapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
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
import io.droidevs.counterapp.databinding.ErrorStateLayoutBinding
import io.droidevs.counterapp.databinding.FragmentViewCategoryBinding
import io.droidevs.counterapp.databinding.LoadingStateLayoutBinding
import io.droidevs.counterapp.ui.adapter.CategoryCountersAdapter
import io.droidevs.counterapp.ui.navigation.AppNavigator
import io.droidevs.counterapp.ui.navigation.tabs.Tab
import io.droidevs.counterapp.ui.navigation.tabs.TabHost
import io.droidevs.counterapp.ui.vm.CategoryViewViewModel
import io.droidevs.counterapp.ui.vm.actions.CategoryViewAction
import io.droidevs.counterapp.ui.vm.events.CategoryViewEvent
import javax.inject.Inject
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ViewCategoryFragment : Fragment() {

    private lateinit var binding: FragmentViewCategoryBinding
    private lateinit var adapter: CategoryCountersAdapter

    private val viewModel: CategoryViewViewModel by viewModels()

    @Inject
    lateinit var appNavigator: AppNavigator

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

    private fun showLoading() {
        binding.stateContainer.visibility = View.VISIBLE
        binding.stateContainer.removeAllViews()
        val loading = LoadingStateLayoutBinding.inflate(layoutInflater, binding.stateContainer, false)
        binding.stateContainer.addView(loading.root)
    }

    private fun showError() {
        binding.stateContainer.visibility = View.VISIBLE
        binding.stateContainer.removeAllViews()
        val error = ErrorStateLayoutBinding.inflate(layoutInflater, binding.stateContainer, false)
        binding.stateContainer.addView(error.root)
    }

    private fun showEmptyState(onAction: () -> Unit) {
        binding.stateContainer.visibility = View.VISIBLE
        binding.stateContainer.removeAllViews()

        val empty = EmptyStateLayoutBinding.inflate(layoutInflater, binding.stateContainer, false)
        binding.stateContainer.addView(empty.root)
        empty.icon.setImageResource(R.drawable.ic_counter)
        empty.titleText.setText(R.string.empty_category_counters_title)
        empty.subtitleText.setText(R.string.empty_category_counters_message)
        empty.createButton.setText(R.string.action_create_counter)
        empty.createButton.setOnClickListener { onAction() }
        empty.root.isVisible = true
    }

    private fun hideState() {
        binding.stateContainer.removeAllViews()
        binding.stateContainer.visibility = View.GONE
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.uiState.collect { state ->
                        state.category?.let {
                            binding.tvCategoryName.text = it.name
                            binding.tvCountersCount.text = it.countersCount.toString()
                        }

                        adapter.submitList(state.counters)

                        when {
                            state.isLoading -> {
                                showLoading()
                                binding.rvCounters.visibility = View.GONE
                                binding.fabAddCounter.visibility = View.GONE
                            }

                            state.isError -> {
                                showError()
                                binding.rvCounters.visibility = View.GONE
                                binding.fabAddCounter.visibility = View.GONE
                            }

                            state.showEmptyState -> {
                                binding.rvCounters.visibility = View.GONE
                                binding.fabAddCounter.visibility = View.GONE
                                showEmptyState {
                                    viewModel.onAction(CategoryViewAction.AddCounterClicked)
                                }
                            }

                            else -> {
                                hideState()
                                binding.rvCounters.visibility = View.VISIBLE
                                binding.fabAddCounter.visibility = View.VISIBLE
                            }
                        }
                    }
                }

                launch {
                    viewModel.event.collect { event ->
                        when (event) {
                            CategoryViewEvent.NavigateBack -> {
                                appNavigator.back()
                            }

                            is CategoryViewEvent.NavigateToCreateCounter -> {
                                // Cross-tab: still must use TabHost.
                                (activity as? TabHost)?.switchToTabAndNavigate(
                                    tab = Tab.COUNTERS,
                                    destinationId = R.id.counterCreateFragment,
                                    args = Bundle().apply {
                                        putString("categoryId", event.categoryId)
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.findItem(R.id.menuSettings).isVisible = false
        inflater.inflate(R.menu.menu_category_view, menu)
    }

    @Deprecated("Deprecated in Java")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_delete_category -> {
                viewModel.onAction(CategoryViewAction.DeleteCategoryClicked)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}