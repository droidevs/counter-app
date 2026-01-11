package io.droidevs.counterapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.droidevs.counterapp.R
import io.droidevs.counterapp.ui.adapter.HomeCategoryAdapter
import io.droidevs.counterapp.ui.adapter.HomeCounterAdapter
import io.droidevs.counterapp.databinding.FragmentHomeBinding
import io.droidevs.counterapp.ui.listeners.OnCategoryClickListener
import io.droidevs.counterapp.ui.models.CounterUiModel
import io.droidevs.counterapp.ui.listeners.OnCounterClickListener
import io.droidevs.counterapp.ui.models.CategoryUiModel
import io.droidevs.counterapp.ui.toParcelable
import dagger.hilt.android.AndroidEntryPoint
import io.droidevs.counterapp.ui.vm.HomeViewModel
import io.droidevs.counterapp.ui.fragments.CounterListFragmentDirections
import io.droidevs.counterapp.ui.fragments.CategoryListFragmentDirections
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModels()

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

        recycler = binding.recyclerRecentCounters
        categoryRecycler = binding.recyclerCategories
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()
        setUpButtons()


        lifecycleScope.launch {
            viewModel.countersSnapshots.collect { counters ->
                Log.e("HOME 2", "Total counters: ${counters.size}")
                (recycler?.adapter as HomeCounterAdapter).updateCounters(counters)
            }
        }

        lifecycleScope.launch {
            viewModel.countersNumber.collect { size ->
                // todo :
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.categories.collectLatest { categories ->
                (categoryRecycler?.adapter as HomeCategoryAdapter).submitList(categories)
            }
        }
        lifecycleScope.launch {
            viewModel.categoriesCount.collectLatest { count ->
                // todo:
            }
        }

    }

    private fun setUpRecyclerView() {
        recycler?.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recycler?.adapter = HomeCounterAdapter(
            counters = mutableListOf(),
            listener = object : OnCounterClickListener {
                override fun onCounterClick(counter: CounterUiModel) {
                    //Toast.makeText(requireContext(), "Counter clicked: ${counter.name}", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_to_counters_graph)
                    findNavController().navigate(
                        CounterListFragmentDirections.actionCounterListToCounterView(
                            counter.toParcelable()
                        )
                    )
                }
            },
            onAddCounter = {
                findNavController().navigate(R.id.action_to_counters_graph)
                findNavController().navigate(R.id.action_counterList_to_counterCreate)
            },
            onIncrement = { counter ->
                viewModel.incrementCounter(counter.counter)
            },
            onDecrement = { counter ->
                viewModel.decrementCounter(counter.counter)
            }
        )
        categoryRecycler?.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        categoryRecycler?.adapter = HomeCategoryAdapter(
            listener = object : OnCategoryClickListener {
                override fun onCategoryClick(category: CategoryUiModel) {
                    //Toast.makeText(requireContext(), "Category clicked: ${category.name}", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_to_categories_graph)
                    findNavController().navigate(
                        CategoryListFragmentDirections.actionCategoryListToCategoryView(
                            category.id
                        )
                    )
                }
            },
            onAdd = {
                findNavController().navigate(R.id.action_to_categories_graph)
                findNavController().navigate(R.id.action_categoryList_to_categoryCreate)
            }
        )

    }

    private fun setUpButtons() {
        var btnViewAll = binding.txtViewAllCounters
        //var btnCreate = binding.btnNewCounter
        var btnViewCategories = binding.txtViewAllCategories
        //var btnCreateCategory = binding.btnCreateCategory

        btnViewAll.setOnClickListener { v ->
            findNavController().navigate(
                R.id.action_to_counters_graph
            )

            findNavController().navigate(R.id.counterListFragment)
        }

//        btnCreate.setOnClickListener { v ->
//            findNavController().navigate(R.id.action_to_counters_graph)
//            findNavController().navigate(R.id.action_counterList_to_counterCreate)
//        }

        btnViewCategories.setOnClickListener { v ->
            findNavController().navigate(R.id.action_to_categories_graph)
        }
//        btnCreateCategory.setOnClickListener { v ->
//            findNavController().navigate(R.id.action_to_categories_graph)
//            findNavController().navigate(R.id.action_categoryList_to_categoryCreate)
//        }

    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment()
    }
}