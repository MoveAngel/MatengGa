package com.capstone.matengga.ui.result

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.capstone.matengga.data.local.entity.HistoryEntity
import com.capstone.matengga.data.local.room.MatengGaDatabase
import com.capstone.matengga.data.model.PredictionResponse
import com.capstone.matengga.databinding.FragmentResultBinding
import com.capstone.matengga.domain.ResultState
import kotlinx.coroutines.launch

class ResultFragment : Fragment() {
    private var _binding: FragmentResultBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ResultViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBackButton()
        handleArguments()
        observeViewModel()
    }

    private fun setupBackButton() {
        binding.backButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    private fun handleArguments() {
        val imageUri = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable("image_uri", Uri::class.java)
        } else {
            @Suppress("DEPRECATION")
            arguments?.getParcelable("image_uri")
        }

        val predictionData = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable("prediction_data", PredictionResponse::class.java)
        } else {
            @Suppress("DEPRECATION")
            arguments?.getParcelable("prediction_data")
        }

        imageUri?.let { uri ->
            binding.imageResult.setImageURI(uri)

            predictionData?.let { data ->
                showResult(data)  // Langsung tampilkan data jika sudah ada
            } ?: run {
                viewModel.predictImage(uri)  // Hit API hanya jika tidak ada data
            }
        }
    }
    private fun observeViewModel() {
        viewModel.predictionState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is ResultState.Loading -> showLoading()
                is ResultState.Success -> showResult(state.data)
                is ResultState.Error -> showError(state.error)
            }
        }
    }

    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun showResult(response: PredictionResponse) {
        binding.apply {
            progressBar.visibility = View.GONE
            fruitTypeResult.text = response.getTranslatedFruitName()
            ripenessResult.text = response.ripeness
            description.text = response.getDescription()

            // Cek apakah data berasal dari history
            val fromHistory = arguments?.getBoolean("from_history") ?: false

            // Hanya simpan ke history jika bukan berasal dari history
            if (!fromHistory) {
                val imageUri = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                    arguments?.getParcelable("image_uri", Uri::class.java)
                } else {
                    @Suppress("DEPRECATION")
                    arguments?.getParcelable("image_uri")
                }

                imageUri?.let { uri ->
                    saveToHistory(response, uri)
                }
            }
        }
    }


    private fun showError(error: String) {
        binding.progressBar.visibility = View.GONE
        Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
    }

    private fun saveToHistory(response: PredictionResponse, imageUri: Uri) {
        val historyDao = MatengGaDatabase.getInstance(requireContext()).historyDao()
        lifecycleScope.launch {
            historyDao.insertHistory(
                HistoryEntity(
                    imageUri = imageUri.toString(),
                    fruitName = response.getTranslatedFruitName(),
                    ripeness = response.ripeness,
                    ripenessPercentage = response.ripenessPercentage
                )
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}