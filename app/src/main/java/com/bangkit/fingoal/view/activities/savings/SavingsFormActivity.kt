package com.bangkit.fingoal.view.activities.savings

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
import com.bangkit.fingoal.data.response.ShowGoalItem
import com.bangkit.fingoal.data.retrofit.AddSavingRequest
import com.bangkit.fingoal.databinding.ActivityFormSavingsBinding
import com.bangkit.fingoal.view.ViewModelFactory
import com.bangkit.fingoal.view.customview.Button
import com.bangkit.fingoal.view.main.MainActivity

class SavingsFormActivity : AppCompatActivity() {

    private val viewModel by viewModels<SavingsFormViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityFormSavingsBinding
    private lateinit var currentGoal: List<ShowGoalItem>
    private var selectedIdGoal: String? = null
    private lateinit var submitSavings: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormSavingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.topAppbar.setNavigationOnClickListener {
            finish()
        }

        submitSavings = binding.btnSubmitIncome

        viewModel.savingResult.observe(this) { message ->
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

        viewModel.goals.observe(this) { goals ->
            currentGoal = goals
            val goalName = goals.map { it.goal }.toTypedArray()

            binding.dropdownCategory.setOnItemClickListener { _, _, position, _ ->
                selectedIdGoal = currentGoal[position].idGoal
                binding.dropdownCategory.setText(currentGoal[position].goal, false)
            }

            val adapter = ArrayAdapter(this, R.layout.dropdown_item, goalName)
            binding.dropdownCategory.setAdapter(adapter)
        }

        viewModel.getGoals()

        setupAction()
    }

    private fun setupAction() {
        binding.edAddIncomeAmount.addTextChangedListener (object : TextWatcher {
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

        submitSavings.setOnClickListener {
            uploadSavings()
        }
    }

    private fun setButtonEnable() {
        val goal = binding.dropdownCategory.text.toString()
        val amountString = binding.edAddIncomeAmount.text.toString().replace("\\D".toRegex(), "")
        val amount = amountString.toIntOrNull() ?: 0

        val isGoalValid = goal.isNotEmpty()
        val isAmountValid = amount > 0

        submitSavings.isEnabled = isGoalValid && isAmountValid
    }

    private fun uploadSavings() {
        val idGoal = selectedIdGoal ?: ""
        val amountString = binding.edAddIncomeAmount.text.toString().replace("\\D".toRegex(), "")
        val amount = amountString.toIntOrNull() ?: 0

        if (amount == 0 && idGoal == "") {
            Toast.makeText(this, "Please fill in the fields before submitting", Toast.LENGTH_SHORT).show()
            return
        } else {
            viewModel.addSaving(AddSavingRequest(idGoal, amount))
        }
    }
}