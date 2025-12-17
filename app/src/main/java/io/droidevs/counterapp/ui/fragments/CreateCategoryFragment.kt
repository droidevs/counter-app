package io.droidevs.counterapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import io.droidevs.counterapp.CounterApp
import io.droidevs.counterapp.R
import io.droidevs.counterapp.databinding.FragmentCreateCategoryBinding
import io.droidevs.counterapp.domain.model.Category
import io.droidevs.counterapp.ui.models.CategoryUiModel
import io.droidevs.counterapp.ui.vm.CreateCategoryViewModel
import io.droidevs.counterapp.ui.vm.factories.CreateCategoryViewModelFactory
import io.droidevs.counterapp.ui.vm.factories.CreateCounterViewModelFactory
import java.time.Instant
import java.util.UUID


class CreateCategoryFragment : Fragment() {

    private lateinit var binding: FragmentCreateCategoryBinding
    private val viewModel: CreateCategoryViewModel by viewModels {
        CreateCategoryViewModelFactory(
            repository = (requireActivity().application as CounterApp).categoryRepository
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSaveCategory.setOnClickListener {
            saveCategory()
        }
    }

    private fun saveCategory() {
        val name = binding.etCategoryName.text.toString().trim()

        if (name.isEmpty()) {
            binding.etCategoryName.error = "Category name is required"
            return
        }

        // Dummy category creation
        val category = CategoryUiModel(
            id = UUID.randomUUID().toString(),
            name = name,
            countersCount = 0
        )

        viewModel.saveCategory(
            category = category,
            onSuccess = {
                Toast.makeText(
                    requireContext(),
                    "Category \"${category.name}\" created",
                    Toast.LENGTH_SHORT
                ).show()
            }
        )

    }

    companion object {
        fun newInstance() = CreateCategoryFragment()
    }
}
