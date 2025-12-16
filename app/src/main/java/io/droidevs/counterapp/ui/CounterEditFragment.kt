package io.droidevs.counterapp.ui

import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import io.droidevs.counterapp.R
import io.droidevs.counterapp.databinding.FragmentCounterEditBinding
import io.droidevs.counterapp.model.Counter

class CounterEditFragment : Fragment() {

    lateinit var binding : FragmentCounterEditBinding

    private lateinit var counter : CounterSnapshot

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        counter = requireArguments()
            .getParcelable<CounterSnapshotParcelable>(ARG_COUNTER)!!
            .toUiModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCounterEditBinding.inflate(inflater, container, false)

        with(binding) {
            etName.setText(counter.name)
            etCurrentCount.setText(counter.currentCount.toString())
            switchCanIncrease.isChecked = counter.canIncrease
            switchCanDecrease.isChecked = counter.canDecrease

            tvCreatedAt.text = "Created at: ${counter.createdAt}"
            tvLastUpdatedAt.text = "Last updated: ${counter.lastUpdatedAt}"
        }
        return binding.root
    }

    companion object {

        internal const val ARG_COUNTER = "counter"
        @JvmStatic
        fun newInstance(counter: CounterSnapshot) =
            CounterEditFragment().apply {
                arguments = bundleOf(ARG_COUNTER to counter.toParcelable())
            }
    }
}