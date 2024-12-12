package com.exam.matengga.view.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.exam.matengga.R
import com.exam.matengga.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Contoh penggunaan binding untuk TextView dan ImageView
        binding.IniNama.text = getString(R.string.app_name)
        binding.imageView.setImageResource(R.drawable.logo) // Ganti dengan resource gambar Anda
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
