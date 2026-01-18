package io.droidevs.counterapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import io.droidevs.counterapp.R
import io.droidevs.counterapp.databinding.EmptyStateLayoutBinding
import io.droidevs.counterapp.databinding.ErrorStateLayoutBinding
import io.droidevs.counterapp.databinding.FragmentCategoryListBinding
import io.droidevs.counterapp.databinding.LoadingStateLayoutBinding
import io.droidevs.counterapp.domain.result.Result
import io.droidevs.counterapp.domain.result.recoverWith
import io.droidevs.counterapp.ui.adapter.CategoryListAdapter
import io.droidevs.counterapp.ui.listeners.OnCategoryClickListener
import io.droidevs.counterapp.ui.models.CategoryUiModel
import io.droidevs.counterapp.ui.navigation.AppNavigator
import io.droidevs.counterapp.ui.vm.CategoryListViewModel
import io.droidevs.counterapp.ui.vm.actions.CategoryListAction
import io.droidevs.counterapp.ui.vm.events.CategoryListEvent
import io.droidevs.counterapp.ui.vm.states.CategoryListUiState
import io.droidevs.counterapp.ui.permission.AppSettingsNavigator
import io.droidevs.counterapp.ui.vm.PermissionViewModel
import io.droidevs.counterapp.ui.vm.actions.PermissionAction
import io.droidevs.counterapp.ui.vm.events.PermissionEvent
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class CategoryListFragment : Fragment() {

    private lateinit var binding: FragmentCategoryListBinding
    private lateinit var adapter: CategoryListAdapter
    private val viewModel: CategoryListViewModel by viewModels()
    private val permissionViewModel: PermissionViewModel by viewModels()

    @Inject
    lateinit var appNavigator: AppNavigator

    private val requestPermissionsLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { results: Map<String, Boolean> ->
        // Detect permanently denied: denied AND rationale is false.
        val permanentlyDenied = results.entries.any { (permission, granted) ->
            !granted && !ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), permission)
        }

        permissionViewModel.onAction(PermissionAction.PermissionsResult(results))

        if (permanentlyDenied) {
            showPermanentlyDeniedDialog()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCategoryListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val isSystem = arguments?.let { CategoryListFragmentArgs.fromBundle(it).isSystem } ?: false
        viewModel.onAction(CategoryListAction.SetSystemMode(isSystem))

        setupRecyclerView()
        observeViewModel()

        if (isSystem) {
            viewLifecycleOwner.lifecycleScope.launch {
                permissionViewModel.ensureSystemCategoriesPermissions()
                    .recoverWith {
                        // On internal failure, show the same UX as blocked permissions.
                        showPermanentlyDeniedDialog()
                        Result.Success(Unit)
                    }
            }
        }
    }

    private fun setupRecyclerView() {
        adapter = CategoryListAdapter(
            listener = object : OnCategoryClickListener {
                override fun onCategoryClick(category: CategoryUiModel) {
                    viewModel.onAction(CategoryListAction.CategoryClicked(category))
                }
            }
        )

        binding.rvCategories.layoutManager = LinearLayoutManager(requireContext())
        binding.rvCategories.adapter = adapter
        binding.rvCategories.setHasFixedSize(true)
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.uiState.collect { state ->
                        updateUi(state)
                    }
                }

                launch {
                    viewModel.event.collect { event ->
                        handleEvent(event)
                    }
                }

                launch {
                    permissionViewModel.event.collect { event ->
                        handlePermissionEvent(event)
                    }
                }
            }
        }
    }

    private fun updateUi(state: CategoryListUiState) {
        when {
            state.isLoading -> showLoading()
            state.isError -> showError()
            state.categories.isEmpty() -> showEmptyState { viewModel.onAction(CategoryListAction.CreateCategoryClicked) }
            else -> {
                hideStateContainer()
                adapter.submitList(state.categories)
            }
        }
    }

    private fun showLoading() {
        binding.rvCategories.visibility = View.GONE
        binding.stateContainer.visibility = View.VISIBLE
        binding.stateContainer.removeAllViews()

        val loadingBinding = LoadingStateLayoutBinding.inflate(layoutInflater, binding.stateContainer, false)
        binding.stateContainer.addView(loadingBinding.root)
    }

    private fun showError() {
        binding.rvCategories.visibility = View.GONE
        binding.stateContainer.visibility = View.VISIBLE
        binding.stateContainer.removeAllViews()

        val errorBinding = ErrorStateLayoutBinding.inflate(layoutInflater, binding.stateContainer, false)
        binding.stateContainer.addView(errorBinding.root)
    }

    private fun showEmptyState(onAction: () -> Unit) {
        binding.rvCategories.visibility = View.GONE
        binding.stateContainer.visibility = View.VISIBLE
        binding.stateContainer.removeAllViews()

        val emptyBinding = EmptyStateLayoutBinding.inflate(layoutInflater, binding.stateContainer, false)
        emptyBinding.icon.setImageResource(R.drawable.ic_category)
        emptyBinding.titleText.setText(R.string.empty_categories_title)
        emptyBinding.subtitleText.setText(R.string.empty_categories_message)
        emptyBinding.createButton.setText(R.string.action_create_category)
        emptyBinding.createButton.setOnClickListener { onAction() }

        binding.stateContainer.addView(emptyBinding.root)
    }

    private fun hideStateContainer() {
        binding.stateContainer.removeAllViews()
        binding.rvCategories.visibility = View.VISIBLE
        binding.stateContainer.visibility = View.GONE
    }

    private fun handleEvent(event: CategoryListEvent) {
        when (event) {
            is CategoryListEvent.NavigateToCategoryView -> {
                appNavigator.navigate(
                    CategoryListFragmentDirections.actionCategoryListToCategoryView(
                        event.categoryId
                    )
                )
            }

            is CategoryListEvent.NavigateToCreateCategory -> {
                appNavigator.navigate(
                    CategoryListFragmentDirections.actionCategoryListToCategoryCreate()
                )
            }
        }
    }

    private fun handlePermissionEvent(event: PermissionEvent) {
        when (event) {
            is PermissionEvent.RequestPermissions -> {
                val requested = event.permissions
                    .filter { it.isNotBlank() }
                    .distinct()

                if (requested.isNotEmpty()) {
                    requestPermissionsLauncher.launch(requested.toTypedArray())
                }
            }

            is PermissionEvent.ShowPermanentlyDeniedDialog -> {
                showPermanentlyDeniedDialog()
            }
        }
    }

    private fun showPermanentlyDeniedDialog() {
        val dialogView = layoutInflater.inflate(
            R.layout.dialog_permission_required,
            null,
            false
        )

        MaterialAlertDialogBuilder(requireContext())
            .setView(dialogView)
            .setTitle(R.string.permissions_permanently_denied_title)
            .setMessage(R.string.permissions_permanently_denied_message)
            .setPositiveButton(R.string.open_app_settings) { _, _ ->
                AppSettingsNavigator.openAppSettings(requireContext())
            }
            .setNegativeButton(android.R.string.cancel, null)
            .show()
    }
}
