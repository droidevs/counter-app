package io.droidevs.counterapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import io.droidevs.counterapp.CounterApp
import io.droidevs.counterapp.R
import io.droidevs.counterapp.ui.adapter.ListCounterAdapter
import io.droidevs.counterapp.databinding.FragmentCounterListBinding
import io.droidevs.counterapp.domain.model.Counter
import io.droidevs.counterapp.ui.models.CounterUiModel
import io.droidevs.counterapp.ui.listeners.OnCounterClickListener
import io.droidevs.counterapp.ui.toParcelable
import io.droidevs.counterapp.ui.vm.CountersListViewModel
import io.droidevs.counterapp.ui.vm.factories.CountersListViewModelFactory
import kotlinx.coroutines.launch

class CounterListFragment : Fragment() , OnCounterClickListener {

    lateinit var binding: FragmentCounterListBinding

    private val viewModel : CountersListViewModel by viewModels {
        CountersListViewModelFactory(
            (requireActivity().application as CounterApp).counterRepository
        )
    }

    private lateinit var recyclerView: RecyclerView
    private lateinit var fabAdd: FloatingActionButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        setHasOptionsMenu(true)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = binding.rvCounters
        fabAdd = binding.fabAddCounter

        recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        val adapter = ListCounterAdapter(
            listener = this,
            onIncrement = {
                viewModel.increment(it)
            },
            onDecrement = {
                viewModel.decrement(it)
            }
        )
        recyclerView.adapter = adapter

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(rv: RecyclerView, dx: Int, dy: Int) {
                val layoutManager = rv.layoutManager as LinearLayoutManager

                val first = layoutManager.findFirstVisibleItemPosition()
                val last = layoutManager.findLastVisibleItemPosition()

                val visibleKeys = mutableSetOf<CounterUiModel>()

                for (i in first..last) {
                    val item = adapter.counters.getOrNull(i) ?: continue
                    visibleKeys += item.counter
                }

                viewModel.onVisibleItemsChanged(visibleKeys)
            }
        })

        lifecycleScope.launch {
            viewModel.counters.collect { counters->
                var adapter = recyclerView.adapter as ListCounterAdapter
                adapter.updateCounters(counters)
            }
        }

        fabAdd.setOnClickListener {
            //Toast.makeText(requireContext(), "Add Counter clicked", Toast.LENGTH_SHORT).show()
            findNavController().navigate(
                R.id.action_counterList_to_counterCreate
            )
        }
    }

    override fun onStop() {
        super.onStop()
        viewModel.flushAllPendingReorders()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCounterListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.findItem(R.id.menuSettings)?.isVisible = false // hack fix todo : implement a custom menu for the list page
    }

    override fun onCounterClick(counter: CounterUiModel) {
        // Toast.makeText(requireContext(), "Counter clicked: ${counter.name}", Toast.LENGTH_SHORT).show()

        val bundle = Bundle().apply {
            putParcelable(CounterViewFragment.Companion.ARG_COUNTER, counter.toParcelable())
        }

        findNavController().navigate(
            R.id.action_counterList_to_counterView,
            bundle
        )
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CounterListFragment()
    }
}