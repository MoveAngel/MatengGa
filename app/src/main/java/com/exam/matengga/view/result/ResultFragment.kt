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
import com.exam.matengga.data.repository.PredictionRepository
import com.exam.matengga.databinding.FragmentResultBinding
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
                observePrediction(localFile)
            } catch (e: FileNotFoundException) {
                Log.e(TAG, "File not found for URI: $imageUri", e)
                Toast.makeText(requireContext(), "File not found for the selected image", Toast.LENGTH_SHORT).show()
            }
        } else {
            Log.e(TAG, "Image URI is null")
            Toast.makeText(requireContext(), "Image not found", Toast.LENGTH_SHORT).show()
        }
    }

    private fun observePrediction(file: File) {
        viewModel.classifyFruit(file)
        viewModel.prediction.observe(viewLifecycleOwner) { result ->
            when {
                result.isSuccess -> {
                    val data = result.getOrNull()
                    binding.fruitTypeResult.text = data?.predictedFruit?.let { translateFruitName(it) } ?: "Tidak diketahui"
                    binding.ripenessResult.text = data?.ripeness?.let { translateRipeness(it) } ?: "-"
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
            "apple" -> "Apel"
            "dragonfruit" -> "Buah Naga"
            else -> fruitName
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.imageResult.visibility = if (isLoading) View.GONE else View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "ResultFragment"
    }
}