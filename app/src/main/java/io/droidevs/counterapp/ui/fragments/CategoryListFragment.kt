package io.droidevs.counterapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import io.droidevs.counterapp.R
import io.droidevs.counterapp.databinding.EmptyStateLayoutBinding
import io.droidevs.counterapp.ui.adapter.CategoryListAdapter
import io.droidevs.counterapp.databinding.FragmentCategoryListBinding
import io.droidevs.counterapp.ui.fragments.ViewCategoryFragment.Companion.ARG_CATEGORY_ID
import io.droidevs.counterapp.ui.listeners.OnCategoryClickListener
import io.droidevs.counterapp.ui.models.CategoryUiModel
import io.droidevs.counterapp.ui.vm.CategoryListViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CategoryListFragment : Fragment() {

    private lateinit var binding: FragmentCategoryListBinding
    private lateinit var adapter: CategoryListAdapter

    private val viewModel : CategoryListViewModel by viewModels()

    private var isSystem = false

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

        isSystem = arguments?.getBoolean("isSystem") == true

        setupRecyclerView()
        loadCategories()
    }

    private fun setupRecyclerView() {
        adapter = CategoryListAdapter(
            listener = object : OnCategoryClickListener {
                override fun onCategoryClick(category: CategoryUiModel) {
                    findNavController().navigate(R.id.action_to_categories_graph)
                    findNavController().navigate(
                        R.id.action_categoryList_to_categoryView,
                        Bundle().apply {
                            putString(ARG_CATEGORY_ID, category.id.toString())
                        }
                    )
                }
            }
        )

        binding.rvCategories.layoutManager = LinearLayoutManager(requireContext())
        binding.rvCategories.adapter = adapter
        binding.rvCategories.setHasFixedSize(true)
    }

    private fun loadCategories() {
        viewLifecycleOwner.lifecycleScope.launch {
            if (isSystem)
                viewModel.systemCategories.value.collectLatest { categories ->
                    adapter.submitList(categories)
                }
            else
                viewModel.categories.value.collectLatest { categories ->
                    if (categories.isEmpty()) {
                        showStateContainer()
                        showEmptyState {
                            findNavController().navigate(
                                R.id.action_categoryList_to_categoryCreate
                            )
                        }
                    } else {
                        hideStateContainer()
                        adapter.submitList(categories)
                    }
                }
        }
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
        binding.icon.setImageResource(R.drawable.ic_category)
        binding.titleText.setText(R.string.empty_categories_title)
        binding.subtitleText.setText(R.string.empty_categories_message)
        binding.createButton.setText(R.string.action_create_category)

        binding.createButton.setOnClickListener { onAction() }
        binding.root.isVisible = true
    }


    fun showStateContainer() {
        binding.rvCategories.visibility = View.GONE
        binding.stateContainer.visibility = View.VISIBLE
    }

    fun hideStateContainer() {
        binding.stateContainer.removeAllViews()
        binding.rvCategories.visibility = View.VISIBLE
        binding.stateContainer.visibility = View.GONE
    }

    companion object {
        const val IS_SYSTEM_CATEGORY = "isSystem"
    }
}