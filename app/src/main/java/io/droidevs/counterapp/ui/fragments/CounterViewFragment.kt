package io.droidevs.counterapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import io.droidevs.counterapp.CounterApp
import io.droidevs.counterapp.R
import io.droidevs.counterapp.databinding.FragmentCounterViewBinding
import io.droidevs.counterapp.ui.models.CounterUiModel
import io.droidevs.counterapp.ui.CounterSnapshotParcelable
import io.droidevs.counterapp.ui.listeners.VolumeKeyHandler
import io.droidevs.counterapp.ui.toParcelable
import io.droidevs.counterapp.ui.toUiModel
import io.droidevs.counterapp.ui.vm.CounterViewViewModel
import io.droidevs.counterapp.ui.vm.factories.CounterViewModelFactory
import kotlinx.coroutines.launch

class CounterViewFragment : Fragment(), VolumeKeyHandler {

    lateinit var binding : FragmentCounterViewBinding
    private lateinit var viewModel: CounterViewViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val counter = (arguments?.getParcelable<CounterSnapshotParcelable>(ARG_COUNTER) as CounterSnapshotParcelable)
            .toUiModel()
        viewModel = ViewModelProvider(
            this,
            CounterViewModelFactory(
                counter = counter,
                counterUseCases = (requireActivity().application as CounterApp).useCases.counterUseCases
            )
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

        val btnIncrease = binding.ivIncrement
        val btnDecrease = binding.ivDecrement

        lifecycleScope.launch {
            viewModel.counter.collect { counter ->
                Log.i("CounterViewFragment", "Counter: $counter")
                counter?.name?.let { name ->
                    (activity as? AppCompatActivity)?.supportActionBar?.title = name
                }

                counter?.let { c ->
                    tvName.text = c.name
                    updateCount(tvCount,c.currentCount)
                    tvCreatedAt.text = "Created at: ${c.createdAt}"
                    tvLastUpdatedAt.text = "Last updated: ${c.lastUpdatedAt}"

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

    fun updateCount(textView: TextView, value: Int) {
        textView.text = value.toString()
        textView.startAnimation(
            AnimationUtils.loadAnimation(textView.context, R.anim.count_change)
        )
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
                //Toast.makeText(requireContext(), "Edit", Toast.LENGTH_SHORT).show()
                findNavController().navigate(
                    R.id.action_counterView_to_counterEdit,
                    Bundle().apply {
                        putParcelable(CounterEditFragment.ARG_COUNTER, viewModel.getCounter()!!.toParcelable())
                    }
                )
                true
            }
            R.id.menu_reset -> {
                //Toast.makeText(requireContext(), "Reset", Toast.LENGTH_SHORT).show()
                viewModel.reset()
                true
            }
            R.id.menu_delete -> {
                viewModel.delete()
                Toast.makeText(requireContext(), "Counter Deleted", Toast.LENGTH_SHORT).show()

                findNavController().popBackStack()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onVolumeUp(): Boolean {
//        Toast.makeText(
//            requireContext(),
//            "Volume up",
//            Toast.LENGTH_SHORT
//        ).show()
        // todo : add a flag that is in the settings
        viewModel.increment()
        return true
    }

    override fun onVolumeDown(): Boolean {
//        Toast.makeText(
//            requireContext(),
//            "Volume down",
//            Toast.LENGTH_SHORT
//        ).show()
        // todo : add a flag that is in the settings
        viewModel.decrement()
        return true
    }

    companion object {

        internal const val ARG_COUNTER = "counter"

        @JvmStatic
        fun newInstance(counter: CounterUiModel) : CounterViewFragment {
            val fragment = CounterViewFragment()
            val bundle = Bundle()
            bundle.putParcelable(ARG_COUNTER, counter.toParcelable())
            fragment.arguments = bundle
            return fragment
        }
    }
}