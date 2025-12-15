package io.droidevs.counterapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.droidevs.counterapp.adapter.CounterAdapter
import io.droidevs.counterapp.databinding.FragmentHomeBinding
import io.droidevs.counterapp.model.CounterSnapshot
import kotlinx.coroutines.launch
import java.time.Instant


class HomeFragment : Fragment() {


    private val viewModel: HomeViewModel by viewModels()

    var binding : FragmentHomeBinding? = null
    var recycler : RecyclerView? = null
    var totalCountersText : TextView? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater)

        recycler = binding?.recyclerLastCounters
        totalCountersText = binding?.txtTotalCounters as TextView

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()
        setUpButtons()

        lifecycleScope.launch {
            viewModel.countersSnapshots.collect { counters ->
                (recycler?.adapter as CounterAdapter).updateCounters(counters)
                // TODO(add anew state for viewmodel to reference the number of the counters in the database)
                // temporary fix
                totalCountersText?.text = "Total Counters: ${counters.size}"
            }
        }
    }

    private fun setUpRecyclerView() {
        recycler?.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recycler?.adapter = CounterAdapter(mutableListOf())
    }

    private fun setUpButtons() {
        var btnViewAll = binding?.btnViewAll
        var btnCreate = binding?.btnNewCounter

        btnViewAll?.setOnClickListener { v -> {
            TODO("Open full List fragment")
        } }

        btnCreate?.setOnClickListener { v -> {
            TODO("open create counter screen")
        } }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment()
    }
}