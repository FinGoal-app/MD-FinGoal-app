package com.bangkit.fingoal.view.activities.income

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bangkit.fingoal.databinding.ActivityFormIncomeBinding
import com.bangkit.fingoal.R
import com.bangkit.fingoal.data.retrofit.AddIncomeRequest
import com.bangkit.fingoal.view.ViewModelFactory
import com.bangkit.fingoal.view.main.MainActivity

class IncomeFormActivity : AppCompatActivity() {

    private val viewModel by viewModels<IncomeFormViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityFormIncomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormIncomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.topAppbar.setNavigationOnClickListener {
            finish()
        }

        viewModel.incomeResult.observe(this) { message ->
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
        binding.edAddIncome.addTextChangedListener(object : TextWatcher {
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
                    binding.edAddIncome.error = getString(R.string.empty_name_warning)
                }
            }

            override fun afterTextChanged(s: Editable?) {}

        })

        binding.edAddIncomeAmount.addTextChangedListener(object : TextWatcher {
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

        binding.btnSubmitIncome.setOnClickListener {
            uploadIncome()
        }
    }

    private fun setButtonEnable() {
        binding.apply {
            val source = edAddIncome.text.toString()
            val amountString = edAddIncomeAmount.text.toString().replace("\\D".toRegex(), "")
            val amount = amountString.toIntOrNull() ?: 0

            val isSourceValid = source.isNotEmpty()
            val isAmountValid = amount > 0

            binding.btnSubmitIncome.isEnabled = isSourceValid && isAmountValid
        }
    }

    private fun uploadIncome() {
        binding.apply {
            val source = edAddIncome.text.toString()
            val amountString = edAddIncomeAmount.text.toString().replace("\\D".toRegex(), "")
            val amount = amountString.toIntOrNull() ?: 0

            if (amount == 0 && source.isEmpty()) {
                Toast.makeText(this@IncomeFormActivity, "Please fill in the amount and source before submitting", Toast.LENGTH_SHORT).show()
                return
            } else {
                viewModel.addIncome(AddIncomeRequest(amount, source))
            }
        }
    }
}