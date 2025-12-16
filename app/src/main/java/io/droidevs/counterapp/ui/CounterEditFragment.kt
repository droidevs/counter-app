package io.droidevs.counterapp.ui

import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import io.droidevs.counterapp.CounterApp
import io.droidevs.counterapp.R
import io.droidevs.counterapp.databinding.FragmentCounterEditBinding
import io.droidevs.counterapp.model.Counter
import io.droidevs.counterapp.ui.vm.CounterEditViewModel
import io.droidevs.counterapp.ui.vm.CounterEditViewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class CounterEditFragment : Fragment() {

    lateinit var binding : FragmentCounterEditBinding

    private lateinit var viewModel : CounterEditViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val counter = requireArguments()
            .getParcelable<CounterSnapshotParcelable>(ARG_COUNTER)!!
            .toUiModel()

        viewModel = ViewModelProvider(
            this,
            CounterEditViewModelFactory(
                initialCounter = counter,
                repository = (requireActivity().application as CounterApp).counterRepository
            )
        )[CounterEditViewModel::class.java]

        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCounterEditBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            viewModel.counter.collectLatest { counter ->
                with(binding) {
                    etName.setText(counter.name)
                    etCurrentCount.setText(counter.currentCount.toString())
                    switchCanIncrease.isChecked = counter.canIncrease
                    switchCanDecrease.isChecked = counter.canDecrease

                    tvCreatedAt.text = "Created at: ${counter.createdAt}"
                    tvLastUpdatedAt.text = "Last updated: ${counter.lastUpdatedAt}"
                }
            }
        }
        with(binding){
            etName.doAfterTextChanged {
                viewModel.updateName(it.toString())
            }

            etCurrentCount.doAfterTextChanged {
                it.toString().toIntOrNull()?.let { count -> viewModel.updateCurrentCount(count) }
            }

            switchCanIncrease.setOnCheckedChangeListener { _,checked ->
                viewModel.setCanIncrease(checked)
            }

            switchCanDecrease.setOnCheckedChangeListener { _,checked ->
                viewModel.setCanDecrease(checked)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.edit_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.action_save -> {
                save()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun save() {
        viewModel.save {
            Toast.makeText(requireContext(), "Saved!", Toast.LENGTH_SHORT).show()
        }
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