package io.droidevs.counterapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import io.droidevs.counterapp.CounterApp
import io.droidevs.counterapp.R
import io.droidevs.counterapp.ui.adapter.CategoryListAdapter
import io.droidevs.counterapp.databinding.FragmentCategoryListBinding
import io.droidevs.counterapp.ui.fragments.ViewCategoryFragment.Companion.ARG_CATEGORY_ID
import io.droidevs.counterapp.ui.listeners.OnCategoryClickListener
import io.droidevs.counterapp.ui.models.CategoryUiModel
import io.droidevs.counterapp.ui.vm.CategoryListViewModel
import io.droidevs.counterapp.ui.vm.factories.CategoryListViewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class CategoryListFragment : Fragment() {

    private lateinit var binding: FragmentCategoryListBinding
    private lateinit var adapter: CategoryListAdapter

    private val viewModel : CategoryListViewModel by viewModels {
        CategoryListViewModelFactory(
            repository = (requireActivity().application as CounterApp).categoryRepository
        )
    }

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

        isSystem = requireArguments().getBoolean("isSystem")

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
                viewModel.categories.value.collectLatest {
                    adapter.submitList(it)
                }
            else
                viewModel.systemCategories.value.collectLatest {
                    adapter.submitList(it)
                }
        }
    }
}