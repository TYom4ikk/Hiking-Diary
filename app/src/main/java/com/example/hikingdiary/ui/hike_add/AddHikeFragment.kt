package com.example.hikingdiary.ui.hike_add

import com.example.hikingdiary.R

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.hikingdiary.data.models.Hike
import com.example.hikingdiary.data.models.Photo
import com.example.hikingdiary.data.repository.HikeRepository
import com.example.hikingdiary.databinding.FragmentAddHikeBinding

class AddHikeFragment : Fragment() {

    private var _binding: FragmentAddHikeBinding? = null
    private val binding get() = _binding!!

    private lateinit var repository: HikeRepository
    private val photos = mutableListOf<Photo>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddHikeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        repository = HikeRepository(requireContext())

        // Сохранение похода
        binding.btnSave.setOnClickListener {
            val title = binding.etTitle.text.toString().trim()
            val location = binding.etLocation.text.toString().trim()
            val date = binding.etDate.text.toString().trim()
            val description = binding.etDescription.text.toString().trim()

            if (title.isEmpty() || date.isEmpty()) {
                Toast.makeText(requireContext(), "Введите название и дату", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val hike = Hike(
                id = repository.createId(),
                title = title,
                location = location,
                date = date,
                description = description,
                photos = photos.toList()
            )

            repository.addHike(hike)
            Toast.makeText(requireContext(), "Поход сохранен", Toast.LENGTH_SHORT).show()

            findNavController().popBackStack()
        }

        // Добавление фото (пока просто заглушка)
        binding.btnPickPhoto.setOnClickListener {
            Toast.makeText(requireContext(), "Добавление фото пока не реализовано", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}