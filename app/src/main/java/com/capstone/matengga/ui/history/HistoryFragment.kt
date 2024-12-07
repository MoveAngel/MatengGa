package com.capstone.matengga.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.matengga.R
import com.capstone.matengga.data.local.entity.HistoryEntity
import com.capstone.matengga.data.local.room.MatengGaDatabase
import com.capstone.matengga.data.model.PredictionResponse
import com.capstone.matengga.databinding.FragmentHistoryBinding
import com.capstone.matengga.ui.result.ResultFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch

class HistoryFragment : Fragment() {
    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    private val historyAdapter = HistoryAdapter()
    private val viewModel: HistoryViewModel by viewModels {
        HistoryViewModel.Factory(
            MatengGaDatabase.getInstance(requireContext()).historyDao()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        setupMenu()
        setupRecyclerView()
        observeHistory()
    }

    private fun setupToolbar() {
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)
    }

    private fun setupMenu() {
        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.history_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_delete_all -> {
                        showDeleteConfirmation()
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun setupRecyclerView() {
        binding.rvHistory.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = historyAdapter
        }

        historyAdapter.setOnItemClickCallback { history ->
            // Navigasi ke ResultFragment dengan menandai bahwa data berasal dari history
            // Ini penting agar ResultFragment tidak menyimpan ulang ke database
            val resultFragment = ResultFragment()
            val bundle = Bundle().apply {
                putParcelable("image_uri", Uri.parse(history.imageUri))
                putParcelable("prediction_data", PredictionResponse(
                    fruitName = history.fruitName,
                    ripeness = history.ripeness,
                    ripenessPercentage = history.ripenessPercentage
                ))
                putBoolean("from_history", true)  // Tambahkan flag ini untuk mencegah penyimpanan ulang
            }
            resultFragment.arguments = bundle

            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, resultFragment)
                .addToBackStack(null)
                .commit()
        }
    }

    private fun observeHistory() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.historyList.collect { histories ->
                    if (histories.isEmpty()) {
                        showEmptyState()
                    } else {
                        showHistories(histories)
                    }
                }
            }
        }
    }

    private fun showEmptyState() {
        binding.apply {
            rvHistory.visibility = View.GONE
            emptyState.visibility = View.VISIBLE
        }
    }

    private fun showHistories(histories: List<HistoryEntity>) {
        binding.apply {
            rvHistory.visibility = View.VISIBLE
            emptyState.visibility = View.GONE
            historyAdapter.submitList(histories)
        }
    }

    private fun showDeleteConfirmation() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Hapus Semua Riwayat")
            .setMessage("Apakah Anda yakin ingin menghapus semua riwayat?")
            .setPositiveButton("Hapus") { _, _ ->
                viewModel.clearHistory()
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}