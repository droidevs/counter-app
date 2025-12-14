package io.droidevs.counterapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.droidevs.counterapp.databinding.FragmentHomeBinding
import io.droidevs.counterapp.model.CounterSnapshot
import java.time.Instant


class HomeFragment : Fragment() {


    var binding : FragmentHomeBinding? = null
    var recycler : RecyclerView? = null
    var totalCountersText : View? = null


    val dummyCounters = listOf(
        CounterSnapshot(
            id = "1",
            name = "Water Intake",
            currentCount = 3,
            maxCount = 8,
            createdAt = Instant.now().minusSeconds(3600),
            lastUpdatedAt = Instant.now().minusSeconds(1800)
        ),
        CounterSnapshot(
            id = "2",
            name = "Push-ups",
            currentCount = 15,
            maxCount = 30,
            createdAt = Instant.now().minusSeconds(7200),
            lastUpdatedAt = Instant.now().minusSeconds(600)
        ),
        CounterSnapshot(
            id = "3",
            name = "Meditation",
            currentCount = 1,
            maxCount = 1,
            createdAt = Instant.now().minusSeconds(10800),
            lastUpdatedAt = Instant.now().minusSeconds(100)
        ),
        CounterSnapshot(
            id = "4",
            name = "Steps",
            currentCount = 5000,
            maxCount = 10000,
            createdAt = Instant.now().minusSeconds(14400),
            lastUpdatedAt = Instant.now().minusSeconds(300)
        ),
        CounterSnapshot(
            id = "5",
            name = "Reading",
            currentCount = 20,
            maxCount = null, // unlimited
            createdAt = Instant.now().minusSeconds(20000),
            lastUpdatedAt = Instant.now().minusSeconds(200)
        ),
        CounterSnapshot(
            id = "6",
            name = "Coffee Cups",
            currentCount = 2,
            maxCount = 5,
            createdAt = Instant.now().minusSeconds(25000),
            lastUpdatedAt = Instant.now().minusSeconds(150)
        )
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater)

        recycler = binding?.recyclerLastCounters
        totalCountersText = binding?.txtTotalCounters
        var btnViewAll = binding?.btnViewAll
        var btnCreate = binding?.btnNewCounter

        setUpRecyclerView()
        setUpButtons()

        return binding?.root
    }

    private fun setUpRecyclerView() {
        recycler?.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)


    }

    private fun setUpButtons() {

    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment()
    }
}