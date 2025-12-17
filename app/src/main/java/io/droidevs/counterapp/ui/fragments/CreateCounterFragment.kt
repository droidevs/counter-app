package io.droidevs.counterapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import io.droidevs.counterapp.CounterApp
import io.droidevs.counterapp.databinding.FragmentCreateCounterBinding
import io.droidevs.counterapp.ui.fragments.ViewCategoryFragment.Companion.ARG_CATEGORY_ID
import io.droidevs.counterapp.ui.models.CounterSnapshot
import io.droidevs.counterapp.ui.vm.CreateCounterViewModel
import io.droidevs.counterapp.ui.vm.factories.CreateCounterViewModelFactory
import java.time.Instant
import java.util.UUID

class CreateCounterFragment : Fragment() {

    private lateinit var binding: FragmentCreateCounterBinding

    private val viewModel : CreateCounterViewModel by viewModels {
        CreateCounterViewModelFactory(
            repository = (requireActivity().application as CounterApp).counterRepository
        )
    }

    // todo : categoryId variable from the arguments
    private lateinit var categoryId : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        categoryId = arguments?.getString(ARG_CATEGORY_ID) ?: ""
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
        binding.btnSave.setOnClickListener { v->
            saveCounter()
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

        val counter = CounterSnapshot(
            id = UUID.randomUUID().toString(),
            name = name,
            currentCount = 0,
            categoryId = categoryId,
            canIncrease = canIncrease,
            canDecrease = canDecrease,
            createdAt = Instant.now(),
            lastUpdatedAt = Instant.now()
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