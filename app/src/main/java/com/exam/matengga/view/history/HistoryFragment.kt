package com.exam.matengga.view.history

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.exam.matengga.databinding.FragmentHistoryBinding

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HistoryViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = HistoryAdapter { history ->
            viewModel.deleteHistory(history)
        }

        binding.rvHistory.adapter = adapter
        binding.rvHistory.layoutManager = LinearLayoutManager(requireContext())

        viewModel.allHistory.observe(viewLifecycleOwner) { historyList ->
            if (historyList.isEmpty()) {
                binding.emptyState.visibility = View.VISIBLE
                binding.rvHistory.visibility = View.GONE
            } else {
                binding.emptyState.visibility = View.GONE
                binding.rvHistory.visibility = View.VISIBLE
                adapter.submitList(historyList)
            }
        }

        binding.btnDeleteAll.setOnClickListener {
            viewModel.deleteAllHistory()
            Toast.makeText(requireContext(), "All history deleted", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
