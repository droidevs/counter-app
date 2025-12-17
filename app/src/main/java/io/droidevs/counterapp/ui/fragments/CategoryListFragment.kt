package io.droidevs.counterapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import io.droidevs.counterapp.CounterApp
import io.droidevs.counterapp.adapter.CategoryListAdapter
import io.droidevs.counterapp.databinding.FragmentCategoryListBinding
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

        setupRecyclerView()
        loadCategories()
    }

    private fun setupRecyclerView() {
        adapter = CategoryListAdapter()

        binding.rvCategories.layoutManager = LinearLayoutManager(requireContext())
        binding.rvCategories.adapter = adapter
    }

    private fun loadCategories() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.categories.collectLatest {
                adapter.submitList(it)
            }
        }
    }
}