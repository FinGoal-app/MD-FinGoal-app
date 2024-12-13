package com.bangkit.fingoal.view.activities.budget

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bangkit.fingoal.R
import com.bangkit.fingoal.data.retrofit.UpdateAllocationRequest
import com.bangkit.fingoal.databinding.ActivityEditBudgetBinding
import com.bangkit.fingoal.view.ViewModelFactory
import com.bangkit.fingoal.view.customview.AmountEditText
import com.bangkit.fingoal.view.customview.Button

class EditBudgetActivity : AppCompatActivity() {

    private val viewModel: EditBudgetViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityEditBudgetBinding
    private lateinit var editTextAmount: AmountEditText
    private lateinit var updateBudgetButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBudgetBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val budgetName = intent.getStringExtra(EXTRA_CATEGORY)
        setSupportActionBar(binding.topAppbar)
        supportActionBar?.title = getString(R.string.edit_budget, budgetName)


        editTextAmount = binding.edEditBudgetAmount
        updateBudgetButton = binding.btnSubmitBudget

        binding.topAppbar.setNavigationOnClickListener {
            finish()
        }

        val categories = resources.getStringArray(R.array.budget_category)
        val adapter = ArrayAdapter(this, R.layout.dropdown_item, categories)
        binding.dropdownEditCategory.setAdapter(adapter)

        viewModel.updateBudgetResult.observe(this) { result ->
            if (result != null) {
                Toast.makeText(this, result, Toast.LENGTH_SHORT).show()
                finish()
            }
        }

        setupAction()
    }

    private fun setupAction() {
        val currentCategory = intent.getStringExtra(EXTRA_CATEGORY)
        val categories = resources.getStringArray(R.array.budget_category)
        val position = categories.indexOf(currentCategory)

        if (position != -1) {
            binding.dropdownEditCategory.setText(currentCategory, false)
        }

        binding.dropdownEditCategory.setOnItemClickListener { _, _, position, _ ->
            val selectedCategory = categories[position]
            binding.dropdownEditCategory.setText(selectedCategory, false)
        }

        editTextAmount.addTextChangedListener(object : TextWatcher {
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

        updateBudgetButton.setOnClickListener {
            updateBudget()
        }
    }

    private fun setButtonEnable() {
        binding.apply {
            val category = dropdownEditCategory.text.toString()
            val amountString = edEditBudgetAmount.text.toString().replace("\\D".toRegex(), "")
            val amount = amountString.toIntOrNull() ?: 0

            val isCategoryValid = category.isNotEmpty()
            val isAmountValid = amount > 0

            updateBudgetButton.isEnabled = isCategoryValid && isAmountValid
        }
    }

    private fun updateBudget() {
        binding.apply {
            val budgetId = intent.getStringExtra(EXTRA_ID)
            val newCategory = dropdownEditCategory.text.toString()
            val newAmountString = edEditBudgetAmount.text.toString().replace("\\D".toRegex(), "")
            val newAmount = newAmountString.toIntOrNull() ?: 0

            if (newAmount == 0) {
                Toast.makeText(this@EditBudgetActivity, "Please fill in the fields before submitting", Toast.LENGTH_SHORT).show()
                return
            } else {
                viewModel.updateBudget(budgetId.toString(), UpdateAllocationRequest(newCategory, newAmount))
            }
        }
    }

    companion object {
        const val EXTRA_ID = "extra_id"
        const val EXTRA_CATEGORY = "extra_category"
        const val EXTRA_AMOUNT = "extra_amount"
    }
}