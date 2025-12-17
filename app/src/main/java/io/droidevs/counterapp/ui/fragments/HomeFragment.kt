package io.droidevs.counterapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.droidevs.counterapp.CounterApp
import io.droidevs.counterapp.R
import io.droidevs.counterapp.adapter.HomeCategoryAdapter
import io.droidevs.counterapp.adapter.HomeCounterAdapter
import io.droidevs.counterapp.databinding.FragmentHomeBinding
import io.droidevs.counterapp.ui.listeners.OnCategoryClickListener
import io.droidevs.counterapp.ui.models.CounterSnapshot
import io.droidevs.counterapp.ui.listeners.OnCounterClickListener
import io.droidevs.counterapp.ui.models.CategoryUiModel
import io.droidevs.counterapp.ui.toParcelable
import io.droidevs.counterapp.ui.vm.HomeViewModel
import io.droidevs.counterapp.ui.vm.factories.HomeViewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {


    private val viewModel: HomeViewModel by viewModels {
        HomeViewModelFactory(
            repository = (requireActivity().application as CounterApp)
                .counterRepository,
            categoryRepository = (requireActivity().application as CounterApp)
                .categoryRepository
        )
    }

    lateinit var binding : FragmentHomeBinding

    var recycler : RecyclerView? = null
    var categoryRecycler : RecyclerView? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //Log.d("NAV", "Current destination = ${findNavController().currentDestination?.id}")
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater)

        recycler = binding.recyclerLastCounters
        categoryRecycler = binding.recyclerTopCategories

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()
        setUpButtons()

        var totalCountersText = binding.txtTotalCounters
        var totalCategoriesText = binding.txtTotalCategories

        lifecycleScope.launch {
            viewModel.countersSnapshots.collect { counters ->
                Log.e("HOME 2", "Total counters: ${counters.size}")
                (recycler?.adapter as HomeCounterAdapter).updateCounters(counters)
            }
        }

        lifecycleScope.launch {
            viewModel.countersNumber.collect { size ->
                totalCountersText.text = "Total Counters: ${size}"
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.categories.collectLatest { categories ->
                (categoryRecycler?.adapter as HomeCategoryAdapter).submitList(categories)
            }
        }
        lifecycleScope.launch {
            viewModel.categoriesCount.collectLatest { count ->
                totalCategoriesText.text = "Total Categories: ${count}"
            }
        }

    }

    private fun setUpRecyclerView() {
        recycler?.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recycler?.adapter = HomeCounterAdapter(
            counters = mutableListOf(),
            listener = object : OnCounterClickListener {
                override fun onCounterClick(counter: CounterSnapshot) {
                    //Toast.makeText(requireContext(), "Counter clicked: ${counter.name}", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(
                        R.id.action_home_to_counterView,
                        Bundle().apply {
                            putParcelable(CounterViewFragment.ARG_COUNTER, counter.toParcelable())
                        }
                    )
                }
            }
        )
        categoryRecycler?.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        categoryRecycler?.adapter = HomeCategoryAdapter(
            listener = object : OnCategoryClickListener {
                override fun onCategoryClick(category: CategoryUiModel) {
                    //Toast.makeText(requireContext(), "Category clicked: ${category.name}", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(
                        R.id.action_home_to_categoryView
                    )
                }
            }
        )

    }

    private fun setUpButtons() {
        var btnViewAll = binding.btnViewAllCounters
        var btnCreate = binding.btnNewCounter
        var btnViewCategories = binding.btnViewAllCategories
        var btnCreateCategory = binding.btnCreateCategory

        btnViewAll.setOnClickListener { v ->
            findNavController().navigate(
                R.id.action_home_to_counterList
            )
        }

        btnCreate.setOnClickListener { v ->
            findNavController().navigate(R.id.action_home_to_counterCreate)
        }

        btnViewCategories.setOnClickListener { v ->
            findNavController().navigate(R.id.action_home_to_categoryList)
        }
        btnCreateCategory.setOnClickListener { v ->
            findNavController().navigate(R.id.action_home_to_categoryCreate)
        }

    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment()
    }
}