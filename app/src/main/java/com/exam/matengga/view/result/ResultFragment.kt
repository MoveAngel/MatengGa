package com.exam.matengga.view.result

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.exam.matengga.data.local.entity.HistoryEntity
import com.exam.matengga.data.local.room.MatengGaDatabase
import com.exam.matengga.data.repository.PredictionRepository
import com.exam.matengga.databinding.FragmentResultBinding
import com.exam.matengga.view.history.HistoryAdapter
import com.exam.matengga.view.history.HistoryViewModel
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileNotFoundException
import java.util.Locale

class ResultFragment : Fragment() {

    private var _binding: FragmentResultBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ResultViewModel
    private val repository = PredictionRepository()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResultBinding.inflate(inflater, container, false)
        val factory = ResultViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[ResultViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val imageUri = arguments?.getParcelable<Uri>("image_uri")
        if (imageUri != null) {
            Log.d(TAG, "Received Image URI: $imageUri")
            try {
                val localFile = copyUriToFile(imageUri)
                binding.imageResult.setImageURI(Uri.fromFile(localFile))
                showLoading(true)
                classifyAndObservePrediction(localFile, imageUri)
            } catch (e: FileNotFoundException) {
                Log.e(TAG, "File not found for URI: $imageUri", e)
                Toast.makeText(requireContext(), "File not found for the selected image", Toast.LENGTH_SHORT).show()
                showLoading(false)
            }
        } else {
            Log.e(TAG, "Image URI is null")
            Toast.makeText(requireContext(), "Image not found", Toast.LENGTH_SHORT).show()
            showLoading(false)
        }
    }

    private fun classifyAndObservePrediction(file: File, imageUri: Uri) {
        viewModel.classifyFruit(file)
        viewModel.prediction.observe(viewLifecycleOwner) { result ->
            when {
                result.isSuccess -> {
                    val data = result.getOrNull()
                    binding.fruitTypeResult.text = data?.predictedFruit?.let { translateFruitName(it) } ?: "Tidak diketahui"
                    binding.ripenessResult.text = data?.ripeness?.let { translateRipeness(it) } ?: "-"

                    data?.let {
                        saveToHistory(
                            it.predictedFruit ?: "Tidak diketahui",
                            it.ripeness ?: "-",
                            imageUri
                        )
                    }
                }
                result.isFailure -> {
                    Toast.makeText(requireContext(), "Error: ${result.exceptionOrNull()?.message}", Toast.LENGTH_SHORT).show()
                }
            }
            showLoading(false)
        }
    }

    private fun copyUriToFile(uri: Uri): File {
        val inputStream = requireContext().contentResolver.openInputStream(uri)
            ?: throw FileNotFoundException("Unable to open URI: $uri")
        val file = File(requireContext().cacheDir, "temp_image.jpg")
        file.outputStream().use { outputStream ->
            inputStream.copyTo(outputStream)
        }
        return file
    }

    private fun translateRipeness(ripeness: String): String {
        return when (ripeness.lowercase(Locale.ROOT)) {
            "unripe" -> "Belum matang"
            "ripe" -> "Matang"
            else -> "Tidak diketahui"
        }
    }

    private fun translateFruitName(fruitName: String): String {
        return when (fruitName.lowercase(Locale.ROOT)) {
            "durian" -> "Durian"
            "strawberry" -> "Stroberi"
            "grape" -> "Anggur"
            "dragon fruit" -> "Buah Naga"
            else -> fruitName
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.loadingOverlay.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.kartuResult.visibility = if (isLoading) View.GONE else View.VISIBLE
        binding.imageResult.visibility = if (isLoading) View.GONE else View.VISIBLE
        binding.fruitNamee.visibility = if (isLoading) View.GONE else View.VISIBLE
        binding.fruitTypeResult.visibility = if (isLoading) View.GONE else View.VISIBLE
        binding.resultofripeness.visibility = if (isLoading) View.GONE else View.VISIBLE
        binding.ripenessResult.visibility = if (isLoading) View.GONE else View.VISIBLE
    }

    private fun saveToHistory(predictedFruit: String, ripeness: String, imageUri: Uri) {
        val history = HistoryEntity(
            fruitName = predictedFruit,
            ripeness = ripeness,
            imageUri = imageUri.toString(),
            timestamp = System.currentTimeMillis()
        )

        lifecycleScope.launch {
            val dao = MatengGaDatabase.getDatabase(requireContext()).historyDao()
            dao.insertHistory(history)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "ResultFragment"
    }
}


