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
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import io.droidevs.counterapp.R
import io.droidevs.counterapp.databinding.EmptyStateLayoutBinding
import io.droidevs.counterapp.ui.adapter.CategoryCountersAdapter
import io.droidevs.counterapp.databinding.FragmentViewCategoryBinding
import io.droidevs.counterapp.ui.vm.CategoryViewViewModel
import io.droidevs.counterapp.ui.fragments.ViewCategoryFragmentArgs
import io.droidevs.counterapp.ui.fragments.ViewCategoryFragmentDirections
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


        lifecycleScope.launch {
            viewModel.category.collect { data ->
                binding.tvCategoryName.text = data.category.name
                binding.tvCountersCount.text = "Counters: ${data.category.countersCount}"
                if (data.counters.isEmpty()) {
                    binding.rvCounters.visibility = View.GONE
                    binding.fabAddCounter.visibility = View.GONE
                    binding.stateContainer.visibility = VISIBLE
                    showEmptyState {
                        findNavController().navigate(
                            ViewCategoryFragmentDirections.actionCategoryViewToCounterCreate(
                                viewModel.categoryId
                            )
                        )
                    }

                } else {
                    binding.rvCounters.visibility = VISIBLE
                    binding.fabAddCounter.visibility = VISIBLE
                    binding.stateContainer.visibility = View.GONE
                    adapter.submitList(data.counters)
                }
            }
        }
        binding.fabAddCounter.setOnClickListener {
            findNavController().navigate(
                ViewCategoryFragmentDirections.actionCategoryViewToCounterCreate(
                    viewModel.categoryId
                )
            )
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
                viewModel.deleteCategory()
                findNavController().navigateUp()
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
//        this.binding.stateContainer.removeAllViews()
        this.binding.stateContainer.addView(binding.root)
        binding.icon.setImageResource(R.drawable.ic_counter)
        binding.titleText.setText(R.string.empty_category_counters_title)
        binding.subtitleText.setText(R.string.empty_category_counters_message)
        binding.createButton.setText(R.string.action_create_counter)

        binding.createButton.setOnClickListener { onAction() }
        binding.root.isVisible = true
    }

}
