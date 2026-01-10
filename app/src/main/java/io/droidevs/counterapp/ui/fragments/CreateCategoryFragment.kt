package io.droidevs.counterapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import io.droidevs.counterapp.CounterApp
import io.droidevs.counterapp.R
import io.droidevs.counterapp.ui.adapter.CategoryColorAdapter
import io.droidevs.counterapp.data.repository.CategoryColorProvider
import io.droidevs.counterapp.databinding.FragmentCreateCategoryBinding
import io.droidevs.counterapp.domain.model.CategoryColor
import io.droidevs.counterapp.ui.decoration.GridSpacingItemDecoration
import io.droidevs.counterapp.ui.models.CategoryUiModel
import io.droidevs.counterapp.ui.vm.CreateCategoryViewModel
import io.droidevs.counterapp.ui.vm.factories.CreateCategoryViewModelFactory
import java.util.UUID


class CreateCategoryFragment : Fragment() {

    private lateinit var binding: FragmentCreateCategoryBinding
    private val viewModel: CreateCategoryViewModel by viewModels {
        CreateCategoryViewModelFactory(
            categoryUseCases = (requireActivity().application as CounterApp).useCases.categoryUseCases
        )
    }

    private lateinit var adapter: CategoryColorAdapter
    private lateinit var colors: MutableList<CategoryColor>
    private var selectedColor: Int = 0


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

        val previewCard = binding.cardPreview
        val previewName = binding.tvPreviewName
        val editName = binding.etCategoryName
        val recycler = binding.recyclerColors
        val createBtn = binding.btnCreateCategory


        colors = CategoryColorProvider.generatePalette(
            context = requireContext(),
            numColors = 8
        ) as MutableList<CategoryColor>


        val suggested = CategoryColorProvider.generateColorForCategory(
            context = requireContext(),
            categoryName = binding.etCategoryName.text.toString().trim()
        )

        selectedColor = suggested.colorInt

        previewCard.setCardBackgroundColor(selectedColor)

        adapter = CategoryColorAdapter(colors) { color ->
            selectedColor = color
            previewCard.setCardBackgroundColor(color)
        }

        recycler.layoutManager = GridLayoutManager(requireContext(), 5)
        recycler.adapter = adapter
        recycler.addItemDecoration(
            GridSpacingItemDecoration(resources.getDimensionPixelSize(R.dimen.color_spacing))
        )


        editName.doAfterTextChanged {
            previewName.text = it?.toString().orEmpty()
            val suggested = CategoryColorProvider.generateColorForCategory(
                context = requireContext(),
                categoryName = it.toString().trim()
            )

            selectedColor = suggested.colorInt
            previewCard.setCardBackgroundColor(selectedColor)
        }

        createBtn.setOnClickListener {
            val name = editName.text?.toString()?.trim()

            if (name.isNullOrEmpty()) {
                editName.error = "Category name required"
                return@setOnClickListener
            }

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
            color = CategoryColor(colorInt = selectedColor),
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
