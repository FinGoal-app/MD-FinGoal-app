package com.bangkit.fingoal.view.adapter

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.fingoal.R
import com.bangkit.fingoal.data.response.ShowGoalItem
import com.bangkit.fingoal.data.retrofit.PredictRequest
import com.bangkit.fingoal.data.utils.calculateMonthDifference
import com.bangkit.fingoal.data.utils.formatDate
import com.bangkit.fingoal.data.utils.formatRupiah
import com.bangkit.fingoal.databinding.ItemGoalsBinding
import com.bangkit.fingoal.view.ViewModelFactory
import com.bangkit.fingoal.view.activities.goals.EditGoalActivity
import com.bangkit.fingoal.view.activities.goals.EditGoalActivity.Companion.EXTRA_AMOUNT
import com.bangkit.fingoal.view.activities.goals.EditGoalActivity.Companion.EXTRA_GOAL
import com.bangkit.fingoal.view.activities.goals.EditGoalActivity.Companion.EXTRA_ID
import com.bangkit.fingoal.view.activities.goals.EditGoalActivity.Companion.EXTRA_TARGET
import com.bangkit.fingoal.view.goals.GoalsViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class GoalAdapter: ListAdapter<ShowGoalItem, GoalAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ItemGoalsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val goal = getItem(position)
        holder.bind(goal)
        holder.binding.apply {
            if (tvProgress.text == "Finished") {
                btnPredict.visibility = View.GONE
                fabEdit.visibility = View.GONE
            } else {
                btnPredict.visibility = View.VISIBLE
                fabDelete.visibility = View.VISIBLE
                fabEdit.visibility = View.VISIBLE
            }
        }
    }

    override fun getItemCount(): Int {
        return currentList.size
    }

    class ViewHolder(val binding: ItemGoalsBinding) : RecyclerView.ViewHolder(binding.root) {
        private val viewModel = ViewModelProvider(
            binding.root.context as ViewModelStoreOwner,
            ViewModelFactory.getInstance(binding.root.context)
        )[GoalsViewModel::class.java]

        fun bind(goal: ShowGoalItem) {
            binding.apply {
                tvGoal.text = goal.goal
                tvMonthTarget.text = calculateMonthDifference(goal.updatedAt.toString(), goal.target.toString())
                tvDateTarget.text = formatDate(goal.target.toString())
                tvAmountProgress.text = formatRupiah(goal.amount?.toDouble() ?: 0.0)
                goal.savingGoal?.let { savingGoal ->
                    tvProgress.text = if (savingGoal != 0 ) {
                        if (savingGoal == goal.amount) {
                            "Finished"
                        } else {
                            "On Progress"
                        }
                    } else {
                        "On Progress"
                    }
                }
                tvAmountLeft.text = formatRupiah(goal.savingGoal?.toDouble() ?: 0.0)

                val totalAmount = goal.amount?.toDouble() ?: 0.0
                val amountSaved = goal.savingGoal?.toDouble() ?: 0.0
                val progressPercentage = if (goal.savingGoal != 0) {
                    if (goal.savingGoal == goal.amount) {
                        100.0 // Progress 100% (Finished)
                    } else {
                        (amountSaved / totalAmount) * 100
                    }
                } else {
                    0.0
                }
                goalProgress.progress = progressPercentage.toInt()

                btnPredict.setOnClickListener {
                    btnPredict.isEnabled = false
                    val goalId = goal.idGoal.toString()
                    viewModel.predictGoal(PredictRequest(goalId))
                    progressIndicator.visibility = View.VISIBLE
                    btnPredict.backgroundTintList = ColorStateList.valueOf(Color.GRAY)

                    viewModel.getPredict()
                    btnPredict.backgroundTintList = ColorStateList.valueOf(
                        ContextCompat.getColor(itemView.context, R.color.green))
                    btnPredict.isEnabled = true

                    viewModel.predictResult.observe(root.context as LifecycleOwner) { predict ->
                        progressIndicator.visibility = View.VISIBLE
                        if (predict != null) {
                            progressIndicator.visibility = View.GONE
                            MaterialAlertDialogBuilder(root.context)
                                .setTitle("Predict Result for ${goal.goal}")
                                .setMessage("${goal.predictGoal}")
                                .setPositiveButton("OK") { dialog, _ ->
                                    dialog.dismiss()
                                }.show()
                        }
                        viewModel.predictResult.removeObservers(root.context as LifecycleOwner)
                    }
                }

                fabDelete.setOnClickListener {
                    MaterialAlertDialogBuilder(it.context)
                        .setTitle("Delete Goal")
                        .setMessage("Are you sure want to delete this goal?")
                        .setNegativeButton("Cancel") { dialog, _ ->
                            dialog.dismiss()
                        }.setPositiveButton("Delete") { _, _ ->
                            viewModel.deleteGoal(goal.idGoal.toString())
                            viewModel.deleteResult.observe(binding.root.context as LifecycleOwner) { deleteResult ->
                                if (deleteResult != null) {
                                    Toast.makeText(binding.root.context, deleteResult, Toast.LENGTH_SHORT).show()
                                    viewModel.deleteResult.removeObservers(binding.root.context as LifecycleOwner)
                                    viewModel.getGoals()
                                }
                            }
                        }.show()
                }

                fabEdit.setOnClickListener {
                    val intent = Intent(this.root.context, EditGoalActivity::class.java)
                    intent.apply {
                        putExtra(EXTRA_ID, goal.idGoal)
                        putExtra(EXTRA_GOAL, goal.goal)
                        putExtra(EXTRA_AMOUNT, formatRupiah(goal.amount?.toDouble() ?: 0.0))
                        putExtra(EXTRA_TARGET, formatDate(goal.target.toString()))
                    }
                    this.root.context.startActivity(intent)
                }
            }
        }
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<ShowGoalItem> =
            object : DiffUtil.ItemCallback<ShowGoalItem>() {
                override fun areItemsTheSame(
                    oldItem: ShowGoalItem,
                    newItem: ShowGoalItem
                ): Boolean {
                    return oldItem.idGoal == newItem.idGoal
                }
                override fun areContentsTheSame(
                    oldItem: ShowGoalItem,
                    newItem: ShowGoalItem
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }
}