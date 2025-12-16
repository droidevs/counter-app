package io.droidevs.counterapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import io.droidevs.counterapp.R
import io.droidevs.counterapp.databinding.FragmentCounterViewBinding
import io.droidevs.counterapp.ui.vm.CounterViewModelFactory
import io.droidevs.counterapp.ui.vm.CounterViewViewModel
import kotlinx.coroutines.launch

class CounterViewFragment : Fragment() {

    lateinit var binding : FragmentCounterViewBinding
    private lateinit var viewModel: CounterViewViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val counter = (arguments?.getParcelable<CounterSnapshotParcelable>(ARG_COUNTER) as CounterSnapshotParcelable)
            .toUiModel()
        viewModel = ViewModelProvider(
            this,
            CounterViewModelFactory(counter = counter)
        )[CounterViewViewModel::class.java]

        setHasOptionsMenu(true) // this is depricated todo : i will do it later the modern way

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCounterViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tvName = binding.tvCounterName
        val tvCount = binding.tvCurrentCount
        val tvCreatedAt = binding.tvCreatedAt
        val tvLastUpdatedAt = binding.tvLastUpdatedAt
        val tvCanIncrease = binding.tvCanIncrease
        val tvCanDecrease = binding.tvCanDecrease

        val btnIncrease = binding.btnIncrease
        val btnDecrease = binding.btnDecrease

        lifecycleScope.launch {
            viewModel.counter.collect { counter ->
                counter?.name?.let { name ->
                    (activity as? AppCompatActivity)?.supportActionBar?.title = name
                }

                counter?.let { c ->
                    tvName.text = c.name
                    tvCount.text = c.currentCount.toString()
                    tvCreatedAt.text = "Created at: ${c.createdAt}"
                    tvLastUpdatedAt.text = "Last updated: ${c.lastUpdatedAt}"
                    tvCanIncrease.text = "Can increase: ${c.canIncrease}"
                    tvCanDecrease.text = "Can decrease: ${c.canDecrease}"

                    btnIncrease.isEnabled = c.canIncrease
                    btnDecrease.isEnabled = c.canDecrease
                }
            }
        }

        btnIncrease.setOnClickListener {
            //Toast.makeText(requireContext(), "Increment", Toast.LENGTH_SHORT).show()
            viewModel.increment()
        }

        btnDecrease.setOnClickListener {
            //Toast.makeText(requireContext(), "Decrement", Toast.LENGTH_SHORT).show()
            viewModel.decrement()
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.counter_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.menu_edit -> {
                Toast.makeText(requireContext(), "Edit", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.menu_reset -> {
                //Toast.makeText(requireContext(), "Reset", Toast.LENGTH_SHORT).show()
                viewModel.reset()
                true
            }
            R.id.menu_delete -> {
                Toast.makeText(requireContext(), "Delete", Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {

        internal const val ARG_COUNTER = "counter"

        @JvmStatic
        fun newInstance(counter: CounterSnapshot) : CounterViewFragment {
            val fragment = CounterViewFragment()
            val bundle = Bundle()
            bundle.putParcelable(ARG_COUNTER, counter.toParcelable())
            fragment.arguments = bundle
            return fragment
        }
    }
}