package io.droidevs.counterapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import io.droidevs.counterapp.R
import io.droidevs.counterapp.databinding.FragmentCounterEditBinding
import io.droidevs.counterapp.ui.navigation.AppNavigator
import io.droidevs.counterapp.ui.vm.CounterEditViewModel
import io.droidevs.counterapp.ui.vm.actions.CounterEditAction
import io.droidevs.counterapp.ui.vm.events.CounterEditEvent
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class CounterEditFragment : Fragment() {

    private lateinit var binding : FragmentCounterEditBinding

    private val viewModel : CounterEditViewModel by viewModels()

    @Inject
    lateinit var appNavigator: AppNavigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCounterEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListeners()
        observeViewModel()
    }

    private fun setupListeners() {
        with(binding) {
            etName.doAfterTextChanged {
                viewModel.onAction(CounterEditAction.UpdateName(it.toString()))
            }

            etCurrentCount.doAfterTextChanged {
                it.toString().toIntOrNull()?.let { count ->
                    viewModel.onAction(CounterEditAction.UpdateCurrentCount(count))
                }
            }

            switchCanIncrease.setOnCheckedChangeListener { _, checked ->
                viewModel.onAction(CounterEditAction.SetCanIncrease(checked))
            }

            switchCanDecrease.setOnCheckedChangeListener { _, checked ->
                viewModel.onAction(CounterEditAction.SetCanDecrease(checked))
            }
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.uiState.collect { state ->
                        state.counter?.let { counter ->
                            if (binding.etName.text.toString() != counter.name) {
                                binding.etName.setText(counter.name)
                            }
                            if (binding.etCurrentCount.text.toString() != counter.currentCount.toString()) {
                                binding.etCurrentCount.setText(counter.currentCount.toString())
                            }

                            if (binding.switchCanIncrease.isChecked != counter.canIncrease) {
                                binding.switchCanIncrease.isChecked = counter.canIncrease
                            }
                            if (binding.switchCanDecrease.isChecked != counter.canDecrease) {
                                binding.switchCanDecrease.isChecked = counter.canDecrease
                            }

                            binding.tvCreatedAt.text = getString(R.string.created_at_label, counter.createdAt.toString())
                            binding.tvLastUpdatedAt.text = getString(R.string.last_updated_label, counter.lastUpdatedAt.toString())
                        }
                    }
                }

                launch {
                    viewModel.event.collect { event ->
                        when (event) {
                            CounterEditEvent.NavigateBack -> {
                                appNavigator.back()
                            }
                        }
                    }
                }
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.findItem(R.id.menuSettings).isVisible = false
        inflater.inflate(R.menu.edit_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.action_save -> {
                viewModel.onAction(CounterEditAction.SaveClicked)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
