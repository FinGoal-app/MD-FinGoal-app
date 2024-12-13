package com.bangkit.fingoal.view.budget

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.fingoal.R
import com.bangkit.fingoal.data.response.AllocationPredictRequest
import com.bangkit.fingoal.data.utils.formatRupiah
import com.bangkit.fingoal.databinding.FragmentBudgetBinding
import com.bangkit.fingoal.view.ViewModelFactory
import com.bangkit.fingoal.view.activities.budget.BudgetFormActivity
import com.bangkit.fingoal.view.adapter.BudgetAdapter
import com.bangkit.fingoal.view.customview.PredictButton
import com.bangkit.fingoal.view.main.MainViewModel
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class BudgetFragment : Fragment() {

    private val viewModel: BudgetViewModel by viewModels {
        ViewModelFactory.getInstance(requireActivity().application)
    }
    private val mainViewModel: MainViewModel by viewModels {
        ViewModelFactory.getInstance(requireActivity().application)
    }
    private var _binding: FragmentBudgetBinding? = null
    private val binding get() = _binding!!
    private lateinit var predictAllocationButton: PredictButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBudgetBinding.inflate(inflater, container, false)

        binding.fabAddAllocation.setOnClickListener {
            val intent = Intent(requireContext(), BudgetFormActivity::class.java)
            startActivity(intent)
        }

        predictAllocationButton = binding.btnPredictAllocation
        predictAllocationButton.setOnClickListener {
            predictAllocation()
        }


        mainViewModel.getToken().observe(viewLifecycleOwner) { token ->
            if (token.isNotEmpty()) {
                viewModel.getBudget()
                viewModel.getAllocationAmount()
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.isSuccess.observe(viewLifecycleOwner) { isSuccess ->
            if (isSuccess) {
                setupViewBudgets()
                setupChart()
            }
        }

        viewModel.allocation.observe(viewLifecycleOwner) { allocation ->
            binding.tvAllocationAmount.text = formatRupiah(allocation)
        }
    }
    
    private fun setupChart() {
        viewModel.budgetResult.observe(viewLifecycleOwner) { budgets ->
            if (budgets != null) {
                val budgetEntries = budgets.map { budget ->
                    PieEntry(budget.amount?.toFloat() ?: 0F, budget.kategori)
                }
                
                val dataSet = PieDataSet(budgetEntries, "")
                val colorFoods = ContextCompat.getColor(requireContext(), R.color.md_dark_theme_primaryContainer_mediumContrast)
                val colorTransport = ContextCompat.getColor(requireContext(), R.color.blue)
                val colorElectricity = ContextCompat.getColor(requireContext(), R.color.red_pastel)
                dataSet.colors = listOf(colorFoods, colorTransport, colorElectricity)
                dataSet.valueTextSize = 12f
                dataSet.valueTextColor = Color.WHITE
                dataSet.sliceSpace = 2f
                dataSet.setDrawValues(false)
                dataSet.valueFormatter = PercentFormatter()
                
                val data = PieData(dataSet)
                binding.apply { 
                    budgetPieChart.data = data
                    budgetPieChart.description.isEnabled = false
                    budgetPieChart.legend.isEnabled = true
                    budgetPieChart.centerText = getString(R.string.budget_allocation)
                    budgetPieChart.animateY(1000)
                }
            } else {
                binding.apply {
                    budgetPieChart.data = null
                    budgetPieChart.centerText = getString(R.string.empty_chart_data)
                }
            }
        }
    }

    private fun setupViewBudgets() {
        val budgetAdapter = BudgetAdapter()
        binding.rvBudget.apply {
            adapter = budgetAdapter
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
        }

        viewModel.budgetResult.observe(viewLifecycleOwner) { budgets ->
            val allHasCategory = budgets.any { it.kategori == "Foods" } == true &&
                    budgets.any { it.kategori == "Transport" } == true &&
                    budgets.any { it.kategori == "Electricity" } == true
            binding.btnPredictAllocation.visibility = if (allHasCategory) View.VISIBLE else View.GONE
            binding.tvEmptyBudget.visibility = if (budgets != null && budgets.isNotEmpty()) View.GONE else View.VISIBLE

            if (budgets != null) {
                budgetAdapter.submitList(budgets)
            }
        }
    }

    private fun predictAllocation() {
        viewModel.idUser.observe(viewLifecycleOwner) { idUser ->
            viewModel.predictAllocation(AllocationPredictRequest(idUser))

            viewModel.predictResult.observe(viewLifecycleOwner) { predictResult ->
                if (predictResult != null) {
                    viewModel.foodsPredict.value = predictResult.idealFood
                    viewModel.transportPredict.value = predictResult.idealTransportation
                    viewModel.electricityPredict.value = predictResult.idealElectricity

                    MaterialAlertDialogBuilder(requireContext())
                        .setTitle("Allocation Predict Result")
                        .setMessage(
                             getString(
                                 R.string.foods_predict, formatRupiah(predictResult.idealFood as Double)
                             ) + "\n" +
                             getString(
                                 R.string.transport_predict, formatRupiah(predictResult.idealTransportation as Double)
                             ) + "\n" +
                             getString(
                                 R.string.electricity_predict, formatRupiah(predictResult.idealElectricity as Double)
                             )
                        )
                        .setPositiveButton("OK") { dialog, _ ->
                            dialog.dismiss()
                        }.show()
                    viewModel.predictResult.removeObservers(viewLifecycleOwner)
                } else {
                    MaterialAlertDialogBuilder(requireContext())
                        .setTitle("Allocation Predict Failed")
                        .setMessage("Failed to predict allocation")
                        .setPositiveButton("OK") { dialog, _ ->
                            dialog.dismiss()
                        }.show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}