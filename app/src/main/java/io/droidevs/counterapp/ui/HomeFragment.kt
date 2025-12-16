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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.droidevs.counterapp.CounterApp
import io.droidevs.counterapp.ui.vm.HomeViewModel
import io.droidevs.counterapp.ui.vm.HomeViewModelFactory
import io.droidevs.counterapp.R
import io.droidevs.counterapp.adapter.HomeCounterAdapter
import io.droidevs.counterapp.databinding.FragmentHomeBinding
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
        totalCountersText = binding.txtTotalCounters

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()
        setUpButtons()

        lifecycleScope.launch {
            viewModel.countersSnapshots.collect { counters ->
                (recycler?.adapter as HomeCounterAdapter).updateCounters(counters)
            }
            viewModel.countersNumber.collect { size ->
                totalCountersText?.text = "Total Counters: ${size}"
            }
        }
    }

    private fun setUpRecyclerView() {
        recycler?.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recycler?.adapter = HomeCounterAdapter(
            counters = mutableListOf()
        )
    }

    private fun setUpButtons() {
        var btnViewAll = binding.btnViewAll
        var btnCreate = binding.btnNewCounter

        btnViewAll.setOnClickListener { v ->
            findNavController().navigate(
                R.id.action_home_to_counterList
            )
        }

        btnCreate.setOnClickListener { v ->
            findNavController().navigate(R.id.action_home_to_counterCreate)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment()
    }
}