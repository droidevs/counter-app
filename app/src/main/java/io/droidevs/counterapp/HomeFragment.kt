package io.droidevs.counterapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.droidevs.counterapp.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {


    var binding : FragmentHomeBinding? = null
    var totalCountersText : View? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater)

        totalCountersText = binding?.txtTotalCounters
        var btnViewAll = binding?.btnViewAll
        var btnCreate = binding?.btnNewCounter

        setUpButtons()

        return binding?.root
    }


    private fun setUpButtons() {

    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment()
    }
}