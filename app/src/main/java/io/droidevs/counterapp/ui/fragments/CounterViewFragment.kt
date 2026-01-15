package io.droidevs.counterapp.ui.fragments

import android.os.Bundle
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
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import io.droidevs.counterapp.R
import io.droidevs.counterapp.databinding.FragmentCounterViewBinding
import io.droidevs.counterapp.ui.listeners.VolumeKeyHandler
import io.droidevs.counterapp.ui.navigation.AppNavigator
import io.droidevs.counterapp.ui.vm.CounterViewViewModel
import io.droidevs.counterapp.ui.vm.actions.CounterViewAction
import io.droidevs.counterapp.ui.vm.events.CounterViewEvent
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class CounterViewFragment : Fragment(), VolumeKeyHandler {

    lateinit var binding : FragmentCounterViewBinding
    private val viewModel: CounterViewViewModel by viewModels()

    @Inject
    lateinit var appNavigator: AppNavigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCounterViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ivIncrement.setOnClickListener {
            viewModel.onAction(CounterViewAction.IncrementCounter)
        }

        binding.ivDecrement.setOnClickListener {
            viewModel.onAction(CounterViewAction.DecrementCounter)
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.uiState.collect { state ->
                        state.counter?.let { c ->
                            (activity as? AppCompatActivity)?.supportActionBar?.title = c.name
                            binding.tvCounterName.text = c.name
                            updateCount(binding.tvCurrentCount, c.currentCount)
                            binding.tvCreatedAt.text = getString(R.string.created_at_label, c.createdAt.toString())
                            binding.tvLastUpdatedAt.text = getString(R.string.last_updated_label, c.lastUpdatedAt.toString())

                            binding.ivIncrement.isEnabled = c.canIncrease
                            binding.ivDecrement.isEnabled = c.canDecrease
                        }
                    }
                }

                launch {
                    viewModel.event.collect { event ->
                        when (event) {
                            is CounterViewEvent.NavigateToCounterEdit -> {
                                appNavigator.navigate(
                                    CounterViewFragmentDirections.actionCounterViewToCounterEdit(
                                        event.counterId
                                    )
                                )
                            }
                            CounterViewEvent.NavigateBack -> {
                                appNavigator.back()
                            }
                            is CounterViewEvent.ShowMessage -> {
                                Toast.makeText(requireContext(), event.message, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun updateCount(textView: TextView, value: Int) {
        if (textView.text != value.toString()) {
            textView.text = value.toString()
            textView.startAnimation(
                AnimationUtils.loadAnimation(textView.context, R.anim.count_change)
            )
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
                viewModel.onAction(CounterViewAction.EditCounterClicked)
                true
            }
            R.id.menu_reset -> {
                viewModel.onAction(CounterViewAction.ResetCounter)
                true
            }
            R.id.menu_delete -> {
                viewModel.onAction(CounterViewAction.DeleteCounter)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onVolumeUp(): Boolean {
        viewModel.onAction(CounterViewAction.IncrementCounter)
        return true
    }

    override fun onVolumeDown(): Boolean {
        viewModel.onAction(CounterViewAction.DecrementCounter)
        return true
    }
}
