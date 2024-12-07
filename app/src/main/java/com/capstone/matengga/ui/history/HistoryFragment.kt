package com.capstone.matengga.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.matengga.data.local.entity.HistoryEntity
import com.capstone.matengga.data.local.room.MatengGaDatabase
import com.capstone.matengga.databinding.FragmentHistoryBinding
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
        setupRecyclerView()
        observeHistory()
    }

    private fun setupRecyclerView() {
        binding.rvHistory.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = historyAdapter
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}