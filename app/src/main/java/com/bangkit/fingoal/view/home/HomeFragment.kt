package com.bangkit.fingoal.view.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.fingoal.R
import com.bangkit.fingoal.data.utils.formatRupiah
import com.bangkit.fingoal.databinding.FragmentHomeBinding
import com.bangkit.fingoal.view.ViewModelFactory
import com.bangkit.fingoal.view.activities.expense.ExpenseFormActivity
import com.bangkit.fingoal.view.activities.income.IncomeFormActivity
import com.bangkit.fingoal.view.activities.savings.SavingsFormActivity
import com.bangkit.fingoal.view.adapter.HistoryAdapter
import com.bangkit.fingoal.view.main.MainActivity
import com.bangkit.fingoal.view.main.MainViewModel

class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModels {
        ViewModelFactory.getInstance(requireActivity().application)
    }
    private val mainViewModel: MainViewModel by viewModels {
        ViewModelFactory.getInstance(requireActivity().application)
    }
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        mainViewModel.getUsername().observe(viewLifecycleOwner) { username ->
            val firstName = username.split(" ").firstOrNull() ?: ""
            (activity as MainActivity).supportActionBar?.title = getString(R.string.title_welcome_user, firstName)
        }

        binding.ivIncome.setOnClickListener {
            startActivity(Intent(requireContext(), IncomeFormActivity::class.java))
        }

        binding.ivExpense.setOnClickListener {
            startActivity(Intent(requireContext(), ExpenseFormActivity::class.java))
        }

        binding.ivSavings.setOnClickListener {
            startActivity(Intent(requireContext(), SavingsFormActivity::class.java))
        }

        mainViewModel.getToken().observe(viewLifecycleOwner) { token ->
            if (token.isNotEmpty()) {
                viewModel.getHome()
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        setupAmount()
        setupHistory()
    }

    private fun setupAmount() {
        viewModel.balance.observe(viewLifecycleOwner) { balance ->
            binding.tvBalanceAmount.text = formatRupiah(balance)
        }

        viewModel.savings.observe(viewLifecycleOwner) { savings ->
            binding.tvSavingsAmount.text = formatRupiah(savings)
        }
    }

    private fun setupHistory() {
        val dividerItemDecoration = DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL)
        val historyAdapter = HistoryAdapter()
        binding.rvTransaction.apply {
            adapter = historyAdapter
            addItemDecoration(dividerItemDecoration)
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
        }

        viewModel.homeResult.observe(viewLifecycleOwner) { history ->
            if (history != null && history.isNotEmpty()) {
                historyAdapter.submitList(history)
                binding.tvEmptyHistory.visibility = View.GONE
            } else {
                binding.tvEmptyHistory.visibility = View.VISIBLE
                historyAdapter.submitList(emptyList())
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}