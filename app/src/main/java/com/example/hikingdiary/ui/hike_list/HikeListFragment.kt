package com.example.hikingdiary.ui.hike_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hikingdiary.data.repository.HikeRepository
import com.example.hikingdiary.databinding.FragmentHikeListBinding

class HikeListFragment : Fragment() {

    private var _binding: FragmentHikeListBinding? = null
    private val binding get() = _binding!!

    private lateinit var repository: HikeRepository
    private lateinit var adapter: HikeListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHikeListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        repository = HikeRepository(requireContext())

        adapter = HikeListAdapter(
            items = repository.getAllHikes(),
            onClick = { hike ->
                val action =
                    HikeListFragmentDirections.actionToHikeDetail(hike.id)
                findNavController().navigate(action)
            }
        )

        binding.rvHikes.layoutManager = LinearLayoutManager(requireContext())
        binding.rvHikes.adapter = adapter

        binding.btnAdd.setOnClickListener {
            findNavController().navigate(R.id.action_to_addHike)
        }
    }

    override fun onResume() {
        super.onResume()
        adapter.update(repository.getAllHikes())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}