package io.droidevs.counterapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import io.droidevs.counterapp.R
import io.droidevs.counterapp.databinding.FragmentCounterViewBinding

class CounterViewFragment : Fragment() {

    lateinit var binding : FragmentCounterViewBinding

    private var counter: CounterSnapshot? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        counter = arguments?.getParcelable(ARG_COUNTER)
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

        counter?.let { c ->
            tvName.text = c.name
            tvCount.text = c.currentCount.toString()
            tvCreatedAt.text = "Created at: ${c.createdAt}"
            tvLastUpdatedAt.text = "Last updated: ${c.lastUpdatedAt}"
            tvCanIncrease.text = "Can increase: ${c.canIncrease}"
            tvCanDecrease.text = "Can decrease: ${c.canDecrease}"

            btnIncrease.isEnabled = c.canIncrease
            btnDecrease.isEnabled = c.canDecrease

            btnIncrease.setOnClickListener {
                //todo : handle increment
                Toast.makeText(requireContext(), "Increment", Toast.LENGTH_SHORT).show()
            }

            btnDecrease.setOnClickListener {
                //todo : handle decrement
                Toast.makeText(requireContext(), "Decrement", Toast.LENGTH_SHORT).show()
            }
        }

    }

    companion object {

        private const val ARG_COUNTER = "counter"

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