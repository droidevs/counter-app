package io.droidevs.counterapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import io.droidevs.counterapp.databinding.ErrorStateLayoutBinding
import io.droidevs.counterapp.databinding.FragmentHistoryBinding
import io.droidevs.counterapp.databinding.LoadingStateLayoutBinding
import io.droidevs.counterapp.ui.adapter.HistoryAdapter
import io.droidevs.counterapp.ui.vm.HistoryViewModel
import io.droidevs.counterapp.ui.vm.actions.HistoryViewAction
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HistoryViewModel by viewModels()
    private val historyAdapter = HistoryAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupClickListeners()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        binding.recyclerViewHistory.apply {
            adapter = historyAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setupClickListeners() {
        binding.buttonClearHistory.setOnClickListener {
            viewModel.onAction(HistoryViewAction.ClearHistory)
        }
    }

    private fun showLoading() {
        binding.stateContainer.removeAllViews()
        LoadingStateLayoutBinding.inflate(layoutInflater, binding.stateContainer, true)
        binding.stateContainer.isVisible = true
    }

    private fun showError() {
        binding.stateContainer.removeAllViews()
        ErrorStateLayoutBinding.inflate(layoutInflater, binding.stateContainer, true)
        binding.stateContainer.isVisible = true
    }

    private fun hideState() {
        binding.stateContainer.removeAllViews()
        binding.stateContainer.isVisible = false
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    when {
                        state.isLoading -> {
                            showLoading()
                            binding.recyclerViewHistory.isVisible = false
                            binding.buttonClearHistory.isVisible = false
                        }

                        state.isError -> {
                            showError()
                            binding.recyclerViewHistory.isVisible = false
                            binding.buttonClearHistory.isVisible = false
                        }

                        else -> {
                            hideState()
                            binding.recyclerViewHistory.isVisible = true
                            binding.buttonClearHistory.isVisible = true
                            historyAdapter.submitList(state.history)
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
