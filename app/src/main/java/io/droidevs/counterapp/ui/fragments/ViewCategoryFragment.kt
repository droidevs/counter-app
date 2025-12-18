package io.droidevs.counterapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import io.droidevs.counterapp.CounterApp
import io.droidevs.counterapp.R
import io.droidevs.counterapp.adapter.CategoryCountersAdapter
import io.droidevs.counterapp.databinding.FragmentViewCategoryBinding
import io.droidevs.counterapp.ui.vm.CategoryViewViewModel
import io.droidevs.counterapp.ui.vm.factories.CategoryViewViewModelFactory
import kotlinx.coroutines.launch

class ViewCategoryFragment : Fragment() {

    private lateinit var binding: FragmentViewCategoryBinding
    private lateinit var adapter: CategoryCountersAdapter

    private lateinit var viewModel : CategoryViewViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val categoryId = requireArguments().getString(ARG_CATEGORY_ID)!!

        viewModel = ViewModelProvider(
            this,
            factory = CategoryViewViewModelFactory(
                categoryId = categoryId,
                repository = (requireActivity().application as CounterApp).categoryRepository
            )
        )[CategoryViewViewModel::class.java]

        setHasOptionsMenu(true)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentViewCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = CategoryCountersAdapter()
        binding.rvCounters.layoutManager = LinearLayoutManager(requireContext())
        binding.rvCounters.adapter = adapter


        lifecycleScope.launch {
            viewModel.category.collect { data ->
                binding.tvCategoryName.text = data.category.name
                binding.tvCountersCount.text = "Counters: ${data.category.countersCount}"
                adapter.submitList(data.counters)
            }
        }
        binding.fabAddCounter.setOnClickListener {
            findNavController().navigate(
                R.id.action_categoryView_to_counterCreate,
                Bundle().apply {
                    putString(ARG_CATEGORY_ID, viewModel.categoryId)
                }
            )
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.findItem(R.id.menuSettings).isVisible = false
        inflater.inflate(R.menu.menu_category_view, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.action_delete_category -> {
                viewModel.deleteCategory()
                findNavController().navigateUp()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        const val ARG_CATEGORY_ID = "categoryId"
    }
}
