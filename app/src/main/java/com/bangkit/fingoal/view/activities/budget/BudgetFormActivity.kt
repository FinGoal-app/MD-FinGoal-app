package com.bangkit.fingoal.view.activities.budget

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bangkit.fingoal.R
import com.bangkit.fingoal.data.retrofit.AddAllocationRequest
import com.bangkit.fingoal.databinding.ActivityFormBudgetBinding
import com.bangkit.fingoal.view.ViewModelFactory
import com.bangkit.fingoal.view.customview.Button
import com.bangkit.fingoal.view.main.MainActivity

class BudgetFormActivity : AppCompatActivity() {

    private val viewModel by viewModels<BudgetFormViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityFormBudgetBinding
    private lateinit var addBudgetButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormBudgetBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.topAppbar.setNavigationOnClickListener {
            finish()
        }

        addBudgetButton = binding.btnSubmitBudget

        val categories = resources.getStringArray(R.array.budget_category)
        val adapter = ArrayAdapter(this, R.layout.dropdown_item, categories)
        binding.dropdownCategory.setAdapter(adapter)

        viewModel.allocationResult.observe(this) { message ->
            if (message != null) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.isSuccess.observe(this) { isSuccess ->
            if (isSuccess) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }

        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        setupAction()
    }

    private fun setupAction() {
        binding.edAddBudgetAmount.addTextChangedListener(object : TextWatcher {
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

        addBudgetButton.setOnClickListener {
            uploadBudget()
        }
    }

    private fun setButtonEnable() {
        binding.apply {
            val category = dropdownCategory.text.toString()
            val amountString = edAddBudgetAmount.text.toString().replace("\\D".toRegex(), "")
            val amount = amountString.toIntOrNull() ?: 0

            val isCategoryValid = category.isNotEmpty()
            val isAmountValid = amount > 0

            addBudgetButton.isEnabled = isCategoryValid && isAmountValid
        }
    }

    private fun uploadBudget() {
        binding.apply {
            val category = dropdownCategory.text.toString()
            val amountString = edAddBudgetAmount.text.toString().replace("\\D".toRegex(), "")
            val amount = amountString.toIntOrNull() ?: 0

            if (amount == 0 && category == "Select category") {
                Toast.makeText(this@BudgetFormActivity, "Please fill in the fields before submitting", Toast.LENGTH_SHORT).show()
                return
            } else {
                viewModel.addAllocation(AddAllocationRequest(category, amount))
            }
        }
    }
}