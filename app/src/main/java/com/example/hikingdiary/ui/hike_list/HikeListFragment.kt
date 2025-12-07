package com.example.hikingdiary.ui.hike_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.core.os.bundleOf

import com.example.hikingdiary.data.repository.HikeRepository
import com.example.hikingdiary.databinding.FragmentHikeListBinding
import com.example.hikingdiary.R

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
                val args = bundleOf("hikeId" to hike.id)
                findNavController().navigate(R.id.action_to_hikeDetail, args)
            }
        )

        binding.rvHikes.layoutManager = LinearLayoutManager(requireContext())
        binding.rvHikes.adapter = adapter

        binding.btnAdd.setOnClickListener {
            findNavController().navigate(R.id.action_to_addHike)
        }

        // Верхнее меню и exit
        binding.ivMenu.setOnClickListener {
            Toast.makeText(requireContext(), "Меню пока не реализовано", Toast.LENGTH_SHORT).show()
        }

        binding.tvExit.setOnClickListener {
            // Завершение приложения
            activity?.finishAffinity()
        }

        // Нижняя навигация
        binding.navFire.setOnClickListener {
            findNavController().navigate(R.id.action_to_welcome)
        }

        binding.navList.setOnClickListener {
            // Уже находимся в списке походов
        }

        binding.navProfile.setOnClickListener {
            findNavController().navigate(R.id.action_to_profile)
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