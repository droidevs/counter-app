package io.droidevs.counterapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import io.droidevs.counterapp.R
import io.droidevs.counterapp.adapter.CategoryCountersAdapter
import io.droidevs.counterapp.data.fake.DummyData
import io.droidevs.counterapp.data.toDomain
import io.droidevs.counterapp.databinding.FragmentViewCategoryBinding
import io.droidevs.counterapp.ui.models.CategoryUiModel
import io.droidevs.counterapp.ui.models.CategoryWithCountersUiModel
import io.droidevs.counterapp.ui.models.CounterSnapshot
import io.droidevs.counterapp.ui.toSnapshot

class ViewCategoryFragment : Fragment() {

    private lateinit var binding: FragmentViewCategoryBinding
    private lateinit var adapter: CategoryCountersAdapter

    private val model : CategoryWithCountersUiModel = CategoryWithCountersUiModel(
        categoryId = "1",
        categoryName = "Category 1",
        counters = DummyData.getCounters()
            .map { counters ->
                counters.toDomain().toSnapshot()
            }
    )

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


        binding.tvCategoryName.text = model.categoryName
        binding.tvCountersCount.text = "Counters: ${model.countersCount}"
        adapter.submitList(model.counters)
    }
}
