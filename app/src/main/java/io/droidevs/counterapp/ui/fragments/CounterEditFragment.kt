package io.droidevs.counterapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import io.droidevs.counterapp.R
import io.droidevs.counterapp.databinding.FragmentCounterEditBinding
import io.droidevs.counterapp.ui.models.CounterUiModel
import io.droidevs.counterapp.ui.CounterSnapshotParcelable
import io.droidevs.counterapp.ui.toParcelable
import io.droidevs.counterapp.ui.vm.CounterEditViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CounterEditFragment : Fragment() {

    lateinit var binding : FragmentCounterEditBinding

    private val viewModel : CounterEditViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        menu.findItem(R.id.menuSettings).isVisible = false
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
        fun newInstance(counter: CounterUiModel) =
            CounterEditFragment().apply {
                arguments = bundleOf(ARG_COUNTER to counter.toParcelable())
            }
    }
}