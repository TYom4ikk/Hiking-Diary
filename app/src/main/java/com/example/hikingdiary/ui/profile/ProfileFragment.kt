package com.example.hikingdiary.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.hikingdiary.R
import com.example.hikingdiary.data.repository.HikeRepository
import com.example.hikingdiary.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var repository: HikeRepository

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        repository = HikeRepository(requireContext())

        // Автоматическое количество походов
        val count = repository.getAllHikes().size
        binding.tvHikeCount.text = count.toString()

        // Восстановление профиля из SharedPreferences
        val prefs = requireContext().getSharedPreferences("profile_prefs", 0)
        binding.etNickname.setText(prefs.getString("nickname", ""))
        binding.etAbout.setText(prefs.getString("about", ""))

        // Нижняя навигация
        binding.navFire.setOnClickListener {
            findNavController().navigate(R.id.welcomeFragment)
        }

        binding.navList.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.navProfile.setOnClickListener {
            // Уже находимся в профиле
        }
    }

    override fun onPause() {
        super.onPause()
        val prefs = requireContext().getSharedPreferences("profile_prefs", 0)
        prefs.edit()
            .putString("nickname", binding.etNickname.text.toString())
            .putString("about", binding.etAbout.text.toString())
            .apply()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
