package io.droidevs.counterapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import io.droidevs.counterapp.R
import io.droidevs.counterapp.adapter.ListCounterAdapter
import io.droidevs.counterapp.databinding.FragmentCounterListBinding

class CounterListFragment : Fragment() {

    lateinit var binding: FragmentCounterListBinding

    private lateinit var recyclerView: RecyclerView
    private lateinit var fabAdd: FloatingActionButton

    private val dummyCounters = listOf(
        "Counter 1", "Counter 2", "Counter 3",
        "Counter 4", "Counter 5", "Counter 6"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = binding.rvCounters
        fabAdd = binding.fabAddCounter

        recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)


        val adapter = ListCounterAdapter(dummyCounters)
        recyclerView.adapter = adapter

        fabAdd.setOnClickListener {
            Toast.makeText(requireContext(), "Add Counter clicked", Toast.LENGTH_SHORT).show()
            // TODO: open create counter screen
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCounterListBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CounterListFragment()
    }
}