package com.bangkit.fingoal.view.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.fingoal.R
import com.bangkit.fingoal.data.response.ShowAllocationItem
import com.bangkit.fingoal.data.utils.formatRupiah
import com.bangkit.fingoal.databinding.ItemBudgetBinding
import com.bangkit.fingoal.view.ViewModelFactory
import com.bangkit.fingoal.view.activities.budget.EditBudgetActivity
import com.bangkit.fingoal.view.activities.budget.EditBudgetActivity.Companion.EXTRA_AMOUNT
import com.bangkit.fingoal.view.activities.budget.EditBudgetActivity.Companion.EXTRA_CATEGORY
import com.bangkit.fingoal.view.activities.budget.EditBudgetActivity.Companion.EXTRA_ID
import com.bangkit.fingoal.view.budget.BudgetViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class BudgetAdapter: ListAdapter<ShowAllocationItem, BudgetAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ItemBudgetBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val budget = getItem(position)
        holder.bind(budget)
    }

    override fun getItemCount(): Int {
        return currentList.size
    }

    class ViewHolder(val binding: ItemBudgetBinding) : RecyclerView.ViewHolder(binding.root) {
        private val viewModel = ViewModelProvider(
            binding.root.context as ViewModelStoreOwner,
            ViewModelFactory.getInstance(binding.root.context)
        )[BudgetViewModel::class.java]

        fun bind(budget: ShowAllocationItem) {
            binding.apply {
                ivBudget.setImageResource(
                    when (budget.kategori) {
                        "Foods" -> R.drawable.ic_food
                        "Transport" -> R.drawable.ic_car
                        "Electricity" -> R.drawable.ic_bulb
                        else -> R.drawable.ic_budget
                    }
                )
                tvBudgetAllocation.text = budget.kategori
                tvBudgetAmount.text = formatRupiah(budget.amount?.toDouble() ?: 0.0)

                fabEdit.setOnClickListener {
                    val intent = Intent(itemView.context, EditBudgetActivity::class.java)
                    intent.putExtra(EXTRA_ID, budget.idAllocation)
                    intent.putExtra(EXTRA_CATEGORY, budget.kategori)
                    intent.putExtra(EXTRA_AMOUNT, formatRupiah(budget.amount?.toDouble() ?: 0.0))
                    itemView.context.startActivity(intent)
                }

                fabDelete.setOnClickListener {
                    MaterialAlertDialogBuilder(it.context)
                        .setTitle("Delete Budget")
                        .setMessage("Are you sure want to delete this budget?")
                        .setNegativeButton("Cancel") { dialog, _ ->
                            dialog.dismiss()
                        }.setPositiveButton("Delete") { _, _ ->
                            viewModel.deleteBudget(budget.idAllocation.toString())
                            viewModel.deleteResult.observe(binding.root.context as LifecycleOwner) { deleteResult ->
                                if (deleteResult != null) {
                                    Toast.makeText(binding.root.context, deleteResult, Toast.LENGTH_SHORT).show()
                                    viewModel.deleteResult.removeObservers(binding.root.context as LifecycleOwner)
                                    viewModel.getBudget()
                                }
                            }
                        }.show()
                }
            }
        }
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<ShowAllocationItem> =
            object : DiffUtil.ItemCallback<ShowAllocationItem>() {
                override fun areItemsTheSame(
                    oldItem: ShowAllocationItem,
                    newItem: ShowAllocationItem
                ): Boolean {
                    return oldItem.idAllocation == newItem.idAllocation
                }
                override fun areContentsTheSame(
                    oldItem: ShowAllocationItem,
                    newItem: ShowAllocationItem
                ): Boolean {
                    return oldItem.idAllocation == newItem.idAllocation
                }
            }
    }
}