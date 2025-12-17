package io.droidevs.counterapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import io.droidevs.counterapp.R
import io.droidevs.counterapp.adapter.CategoryListAdapter
import io.droidevs.counterapp.databinding.FragmentCategoryListBinding

class CategoryListFragment : Fragment() {

    private lateinit var binding: FragmentCategoryListBinding
    private lateinit var adapter: CategoryListAdapter

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
        // Temporary fake data
        val categories = listOf(
            CounterCategoryUiModel("1", "Daily Habits", 5),
            CounterCategoryUiModel("2", "Fitness", 3),
            CounterCategoryUiModel("3", "Work", 8)
        )

        adapter.submitList(categories)
    }
}
