package io.droidevs.counterapp.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import io.droidevs.counterapp.CounterApp
import io.droidevs.counterapp.ui.vm.HomeViewModel
import io.droidevs.counterapp.ui.vm.HomeViewModelFactory
import io.droidevs.counterapp.R
import io.droidevs.counterapp.adapter.HomeCategoryAdapter
import io.droidevs.counterapp.adapter.HomeCounterAdapter
import io.droidevs.counterapp.databinding.FragmentHomeBinding
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlin.math.log

class HomeFragment : Fragment() {


    private val viewModel: HomeViewModel by viewModels {
        HomeViewModelFactory(
            (requireActivity().application as CounterApp)
                .counterRepository
        )
    }

    lateinit var binding : FragmentHomeBinding

    var recycler : RecyclerView? = null
    var categoryRecycler : RecyclerView? = null

    var totalCountersText : TextView? = null



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
        totalCountersText = binding.txtTotalCounters

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
                totalCountersText?.text = "Total Counters: ${size}"
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
        categoryRecycler?.adapter = HomeCategoryAdapter() // todo : implement click listener

        val categories = listOf(
            CounterCategoryUiModel("1", "Fitness", 8),
            CounterCategoryUiModel("2", "Habits", 5),
            CounterCategoryUiModel("3", "Work", 3)
        )

        (categoryRecycler?.adapter as HomeCategoryAdapter).submitList(categories)

    }

    private fun setUpButtons() {
        var btnViewAll = binding.btnViewAllCounters
        var btnCreate = binding.btnNewCounter
        var btnViewCategories = binding.btnViewAllCategories

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

    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment()
    }
}