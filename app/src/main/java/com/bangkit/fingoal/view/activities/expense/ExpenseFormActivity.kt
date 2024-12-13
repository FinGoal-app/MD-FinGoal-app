package com.bangkit.fingoal.view.activities.expense

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bangkit.fingoal.R
import com.bangkit.fingoal.data.retrofit.AddExpenseRequest
import com.bangkit.fingoal.databinding.ActivityFormExpenseBinding
import com.bangkit.fingoal.view.ViewModelFactory
import com.bangkit.fingoal.view.customview.Button
import com.bangkit.fingoal.view.main.MainActivity

class ExpenseFormActivity : AppCompatActivity() {
    private val viewModel by viewModels<ExpenseFormViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityFormExpenseBinding
    private lateinit var submitExpense: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormExpenseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        submitExpense = binding.btnSubmitExpense

        binding.topAppbar.setNavigationOnClickListener {
            finish()
        }

        viewModel.expenseResult.observe(this) { message ->
            if (message != null) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.isSuccess.observe(this) { isSuccess ->
            if (isSuccess) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }

        setupAction()
    }

    private fun setupAction() {
        binding.edAddExpense.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {}

            override fun onTextChanged(
                s: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                if (s.toString().isNotEmpty()) {
                    setButtonEnable()
                } else {
                    binding.edAddExpense.error = getString(R.string.empty_name_warning)
                }
            }

            override fun afterTextChanged(s: Editable?) {}

        })

        binding.edAddExpenseAmount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {}

            override fun onTextChanged(
                s: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                setButtonEnable()
            }

            override fun afterTextChanged(s: Editable?) {}

        })

        submitExpense.setOnClickListener {
            uploadExpense()
        }
    }

    private fun setButtonEnable() {
        binding.apply {
            val amountString = edAddExpenseAmount.text.toString().replace("\\D".toRegex(), "")
            val amount = amountString.toIntOrNull() ?: 0
            val purpose = edAddExpense.text.toString()

            val isAmountValid = amount > 0
            val isPurposeValid = purpose.isNotEmpty()

            submitExpense.isEnabled = isAmountValid && isPurposeValid
        }
    }

    private fun uploadExpense() {
        binding.apply {
            val amountString = edAddExpenseAmount.text.toString().replace("\\D".toRegex(), "")
            val amount = amountString.toIntOrNull() ?: 0
            val purpose = edAddExpense.text.toString()

            if (amount == 0 && purpose.isEmpty()) {
                Toast.makeText(this@ExpenseFormActivity, "Please fill in the amount and purpose before submitting", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.addExpense(AddExpenseRequest(amount, purpose))
            }
        }
    }
}