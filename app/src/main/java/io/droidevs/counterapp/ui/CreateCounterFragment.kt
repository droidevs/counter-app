package io.droidevs.counterapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import io.droidevs.counterapp.R
import io.droidevs.counterapp.databinding.FragmentCreateCounterBinding
import java.time.Instant
import java.util.UUID


class CreateCounterFragment : Fragment() {

    private lateinit var binding: FragmentCreateCounterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
            canIncrease = canIncrease,
            canDecrease = canDecrease,
            createdAt = Instant.now(),
            lastUpdatedAt = Instant.now()
        )

        // todo: save counter to database

        Toast.makeText(requireContext(), "Counter saved", Toast.LENGTH_SHORT).show()

    }

    companion object {

        @JvmStatic
        fun newInstance() =
            CreateCounterFragment()
    }
}