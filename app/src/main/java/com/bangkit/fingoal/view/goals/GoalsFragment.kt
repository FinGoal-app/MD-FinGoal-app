package com.bangkit.fingoal.view.goals

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.fingoal.data.utils.formatRupiah
import com.bangkit.fingoal.databinding.FragmentGoalsBinding
import com.bangkit.fingoal.view.ViewModelFactory
import com.bangkit.fingoal.view.activities.goals.GoalsFormActivity
import com.bangkit.fingoal.view.adapter.GoalAdapter
import com.bangkit.fingoal.view.main.MainViewModel

class GoalsFragment : Fragment() {
    private val viewModel: GoalsViewModel by viewModels {
        ViewModelFactory.getInstance(requireActivity().application)
    }
    private val mainViewModel: MainViewModel by viewModels {
        ViewModelFactory.getInstance(requireActivity().application)
    }
    private var _binding: FragmentGoalsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGoalsBinding.inflate(inflater, container, false)

        binding.fabAddGoal.setOnClickListener {
            val intent = Intent(requireContext(), GoalsFormActivity::class.java)
            startActivity(intent)
        }

        mainViewModel.getToken().observe(viewLifecycleOwner) { token ->
            if (token.isNotEmpty()) {
                viewModel.getGoals()
                viewModel.getSavings()
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.savings.observe(viewLifecycleOwner) { savings ->
            binding.tvSavingsAmount.text = formatRupiah(savings)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.isSuccess.observe(viewLifecycleOwner) { isSuccess ->
            if (isSuccess) {
                setupViewGoals()
            }
        }
    }

    private fun setupViewGoals() {
        val goalAdapter = GoalAdapter()
        binding.rvGoals.apply {
            adapter = goalAdapter
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
        }

        viewModel.goals.observe(viewLifecycleOwner) { goals ->
            if (goals != null && goals.isNotEmpty()) {
                val sortedGoals = goals.sortedByDescending { it.savingGoal != it.amount }
                goalAdapter.submitList(sortedGoals)
                binding.tvEmptyGoals.visibility = View.GONE
            } else {
                binding.tvEmptyGoals.visibility = View.VISIBLE
            }
        }

        viewModel.deleteResult.observe(viewLifecycleOwner) { deleteResult ->
            if (deleteResult != null) {
                Toast.makeText(requireContext(), deleteResult, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}