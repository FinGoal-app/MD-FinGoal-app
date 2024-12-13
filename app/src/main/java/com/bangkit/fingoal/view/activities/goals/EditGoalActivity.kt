package com.bangkit.fingoal.view.activities.goals

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bangkit.fingoal.R
import com.bangkit.fingoal.data.retrofit.UpdateGoalRequest
import com.bangkit.fingoal.data.utils.formatDateForUpload
import com.bangkit.fingoal.data.utils.formatDatePicker
import com.bangkit.fingoal.databinding.ActivityEditGoalBinding
import com.bangkit.fingoal.view.ViewModelFactory
import com.bangkit.fingoal.view.customview.AmountEditText
import com.bangkit.fingoal.view.customview.Button
import com.bangkit.fingoal.view.main.MainActivity
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import java.util.Calendar
import java.util.TimeZone

class EditGoalActivity : AppCompatActivity() {

    private val viewModel: EditGoalViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityEditGoalBinding
    private lateinit var amountEditText: AmountEditText
    private lateinit var updateGoalButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditGoalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val goalName = intent.getStringExtra(EXTRA_GOAL)
        setSupportActionBar(binding.topAppbar)
        supportActionBar?.title = getString(R.string.edit_goal, goalName)

        amountEditText = binding.edEditGoalAmount
        updateGoalButton = binding.btnSubmitGoal

        binding.topAppbar.setNavigationOnClickListener {
            finish()
        }

        binding.edEditTargetDate.setOnClickListener {
            showDatePickerDialog()
        }

        viewModel.updateGoalResult.observe(this) { result ->
            if (result != null) {
                Toast.makeText(this, result, Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }

        setupAction()
    }

    private fun showDatePickerDialog() {
        val today = MaterialDatePicker.todayInUtcMilliseconds()
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        calendar.timeInMillis = today

        val constraintsBuilder = CalendarConstraints.Builder()
        constraintsBuilder.setStart(today)
        constraintsBuilder.setValidator(DateValidatorPointForward.from(today))

        val constraints = constraintsBuilder.build()

        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select Date")
            .setSelection(today)
            .setCalendarConstraints(constraints)
            .build()

        datePicker.show(this.supportFragmentManager, "datePicker")

        datePicker.addOnPositiveButtonClickListener {
            val selectedDate = datePicker.selection
            val formattedDate = formatDatePicker(selectedDate.toString())
            binding.edEditTargetDate.setText(formattedDate)
        }
    }

    private fun setupAction() {
        binding.edEditGoalName.addTextChangedListener(object : TextWatcher {
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
                    binding.edEditGoalName.error = getString(R.string.empty_name_warning)
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        amountEditText.addTextChangedListener(object : TextWatcher {
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

        updateGoalButton.setOnClickListener {
            updateGoal()
        }
    }

    private fun setButtonEnable() {
        binding.apply {
            val goal = edEditGoalName.text.toString()
            val amountString = edEditGoalAmount.text.toString().replace("\\D".toRegex(), "")
            val amount = amountString.toIntOrNull() ?: 0
            val target = edEditTargetDate.text.toString()

            val isGoalValid = goal.isNotEmpty()
            val isAmountValid = amount > 0
            val isTargetValid = target.isNotEmpty()

            updateGoalButton.isEnabled = isGoalValid && isAmountValid && isTargetValid
        }
    }

    private fun updateGoal() {
        binding.apply {
            val goalId = intent.getStringExtra(EXTRA_ID)
            val newGoal = edEditGoalName.text.toString()
            val newAmountString = edEditGoalAmount.text.toString().replace("\\D".toRegex(), "")
            val newAmount = newAmountString.toIntOrNull() ?: 0
            val newTarget = formatDateForUpload(edEditTargetDate.text.toString())
            val newDesc = edEditDesc.text.toString()

            if (newAmount == 0 && newGoal.isEmpty() && newTarget.isEmpty()) {
                Toast.makeText(this@EditGoalActivity, "Please fill in the fields before submitting", Toast.LENGTH_SHORT).show()
                return
            } else {
                viewModel.updateGoal(goalId.toString(), UpdateGoalRequest(newGoal, newAmount, newTarget, newDesc))
            }
        }
    }

    companion object {
        const val EXTRA_ID = "extra_id"
        const val EXTRA_GOAL = "extra_goal"
        const val EXTRA_AMOUNT = "extra_amount"
        const val EXTRA_TARGET = "extra_target"
    }
}