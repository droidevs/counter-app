package io.droidevs.counterapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import io.droidevs.counterapp.CounterApp
import io.droidevs.counterapp.databinding.FragmentCreateCounterBinding
import io.droidevs.counterapp.ui.fragments.ViewCategoryFragment.Companion.ARG_CATEGORY_ID
import io.droidevs.counterapp.ui.models.CategoryUiModel
import io.droidevs.counterapp.ui.models.CounterUiModel
import io.droidevs.counterapp.ui.vm.CreateCounterViewModel
import io.droidevs.counterapp.ui.vm.factories.CreateCounterViewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.Instant
import java.util.UUID

class CreateCounterFragment : Fragment() {

    private lateinit var binding: FragmentCreateCounterBinding

    private val viewModel : CreateCounterViewModel by viewModels {
        CreateCounterViewModelFactory(
            repository = (requireActivity().application as CounterApp).counterRepository,
            categoryRepository = (requireActivity().application as CounterApp).categoryRepository
        )
    }

    private var categoryId : String? = null
    private var categories: List<CategoryUiModel> = emptyList()

    private var adapter: ArrayAdapter<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        categoryId = arguments?.getString(ARG_CATEGORY_ID)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCreateCounterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (categoryId == null) {
            setupCategorySpinner()
            // Dummy categories
            lifecycleScope.launch {
                viewModel.categories.collectLatest { categories ->
                    this@CreateCounterFragment.categories = categories
                    adapter?.clear()
                    adapter?.addAll(categories.map { it.name })
                    adapter?.notifyDataSetChanged()
                }
            }
        }
        else
            binding.spinnerCategory.isVisible = false

        binding.btnSave.setOnClickListener { v->
            saveCounter()
        }

    }

    fun setupCategorySpinner() {

        adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            mutableListOf()
        )


        adapter?.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        binding.spinnerCategory.adapter = adapter

        binding.spinnerCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                categoryId = categories.find { it.name == binding.spinnerCategory.adapter.getItem(position) as String }?.id
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                categoryId = null
            }
        }
    }

    fun saveCounter() {
        val name = binding.etCounterName.text.toString()

        if (name.isEmpty()) {
            binding.etCounterName.error = "Name is required"
            return
        }

        val canIncrease = binding.switchCanIncrease.isChecked
        val canDecrease = binding.switchCanDecrease.isChecked

        val counter = CounterUiModel(
            id = UUID.randomUUID().toString(),
            name = name,
            currentCount = 0,
            categoryId = categoryId,
            canIncrease = canIncrease,
            canDecrease = canDecrease,
            createdAt = Instant.now(),
            lastUpdatedAt = Instant.now(),
            orderAnchorAt = Instant.now()
        )

        viewModel.saveCounter(
            counter = counter,
            onCounterSaved = {
                Toast.makeText(requireContext(), "Counter saved", Toast.LENGTH_SHORT).show()
            }
        )
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            CreateCounterFragment()
    }
}