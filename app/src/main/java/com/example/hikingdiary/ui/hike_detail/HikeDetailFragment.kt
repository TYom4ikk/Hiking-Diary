package com.example.hikingdiary.ui.hike_detail

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hikingdiary.R
import com.example.hikingdiary.data.repository.HikeRepository
import com.example.hikingdiary.databinding.FragmentHikeDetailBinding

class HikeDetailFragment : Fragment(R.layout.fragment_hike_detail) {

    private var _binding: FragmentHikeDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var repository: HikeRepository

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentHikeDetailBinding.bind(view)
        repository = HikeRepository(requireContext())

        val hikeId = arguments?.getString("hikeId")
        if (hikeId == null) {
            Toast.makeText(requireContext(), "Не найден идентификатор похода", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
            return
        }

        val hike = repository.getById(hikeId)
        if (hike == null) {
            Toast.makeText(requireContext(), "Поход не найден", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
            return
        }

        binding.tvTitle.text = hike.title
        binding.tvDate.text = hike.date
        binding.tvLocation.text = hike.location
        binding.tvDescription.text = hike.description

        binding.rvPhotos.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvPhotos.adapter = PhotosAdapter(hike.photos)

        binding.btnDelete.setOnClickListener {
            repository.deleteHike(hike.id)
            Toast.makeText(requireContext(), "Поход удален", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
        }

        binding.btnClose.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}