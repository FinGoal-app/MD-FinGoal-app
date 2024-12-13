package com.bangkit.fingoal.view.adapter

import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.fingoal.R
import com.bangkit.fingoal.data.response.ResultHistoryItem
import com.bangkit.fingoal.data.utils.formatDate
import com.bangkit.fingoal.data.utils.formatRupiah
import com.bangkit.fingoal.data.utils.formatTime
import com.bangkit.fingoal.databinding.ItemTransactionBinding

class HistoryAdapter: ListAdapter<ResultHistoryItem, HistoryAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ItemTransactionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val history = getItem(position)
        holder.bind(history)
    }

    override fun getItemCount(): Int {
        return currentList.size
    }

    class ViewHolder(val binding: ItemTransactionBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(history: ResultHistoryItem) {
            binding.ivCategory.setImageResource(
                when (history.kategori) {
                    "income" -> R.drawable.ic_income
                    "expense" -> R.drawable.ic_expense
                    "savings" -> R.drawable.ic_savings
                    else -> R.drawable.ic_budget
                }
            )
            binding.tvCategory.text = history.kategori
            binding.tvTransaction.text = history.label

            val amount = history.amount?.toDouble() ?: 0.000
            val formattedAmount = formatRupiah(amount)
            val amountText = if (history.kategori == "income") {
                "+ $formattedAmount"
            } else {
                "- $formattedAmount"
            }

            val spannableString = SpannableString(amountText)
            val color = ContextCompat.getColor(
                binding.root.context,
                if (history.kategori == "income") R.color.green else R.color.red
            )

            spannableString.setSpan(
                ForegroundColorSpan(color),
                0,
                1,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            val amountStartIndex = amountText.indexOf(formattedAmount)
            val amountEndIndex = amountStartIndex + formattedAmount.length
            spannableString.setSpan(
                ForegroundColorSpan(color),
                amountStartIndex,
                amountEndIndex,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            binding.tvAmount.text = spannableString
            binding.tvDate.text = formatDate(history.createdAt.toString())
            binding.tvTime.text = formatTime(history.createdAt.toString())
        }
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<ResultHistoryItem> =
            object : DiffUtil.ItemCallback<ResultHistoryItem>() {
                override fun areItemsTheSame(
                    oldItem: ResultHistoryItem,
                    newItem: ResultHistoryItem
                ): Boolean {
                    return oldItem.idHistory == newItem.idHistory
                }
                override fun areContentsTheSame(
                    oldItem: ResultHistoryItem,
                    newItem: ResultHistoryItem
                ): Boolean {
                    return oldItem.idHistory == newItem.idHistory
                }
            }
    }
}