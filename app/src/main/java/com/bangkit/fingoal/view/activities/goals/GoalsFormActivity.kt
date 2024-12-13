package com.bangkit.fingoal.view.activities.goals

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bangkit.fingoal.R
import com.bangkit.fingoal.data.retrofit.AddGoalRequest
import com.bangkit.fingoal.data.utils.formatDateForUpload
import com.bangkit.fingoal.data.utils.formatDatePicker
import com.bangkit.fingoal.databinding.ActivityFormGoalsBinding
import com.bangkit.fingoal.view.ViewModelFactory
import com.bangkit.fingoal.view.main.MainActivity
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import java.util.Calendar
import java.util.TimeZone

class GoalsFormActivity : AppCompatActivity() {

    private val viewModel by viewModels<GoalsFormViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityFormGoalsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormGoalsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.topAppbar.setNavigationOnClickListener {
            finish()
        }

        binding.edAddDate.setOnClickListener {
            showDatePickerDialog()
        }

        viewModel.goalResult.observe(this) { message ->
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
            binding.edAddDate.setText(formattedDate)
        }
    }

    private fun setupAction() {
        binding.edAddGoal.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence,
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
                    binding.edAddGoal.error = getString(R.string.empty_name_warning)
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })

        binding.edAddAmount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence,
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

            override fun afterTextChanged(s: Editable) {}
        })

        binding.edAddDate.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence,
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
                    binding.edAddDate.error = getString(R.string.empty_name_warning)
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })

        binding.btnSubmitGoal.setOnClickListener {
            uploadGoal()
        }
    }

    private fun setButtonEnable() {
        binding.apply {
            val goal = edAddGoal.text.toString()
            val amountString = edAddAmount.text.toString().replace("\\D".toRegex(), "")
            val amount = amountString.toIntOrNull() ?: 0
            val date = edAddDate.text.toString()

            val isAmountValid = amount > 0
            val isDateValid = date.isNotEmpty()

            binding.btnSubmitGoal.isEnabled = goal.isNotEmpty() && isAmountValid && isDateValid
        }
    }

    private fun uploadGoal() {
        binding.apply {
            val goal = edAddGoal.text.toString()
            val amountString = edAddAmount.text.toString().replace("\\D".toRegex(), "")
            val amount = amountString.toIntOrNull() ?: 0
            val dateString = edAddDate.text.toString()
            val target = if (dateString.isNotEmpty()) {
                formatDateForUpload(dateString)
            } else {
                null
            }
            val description = edAddDesc.text.toString()

            if (amount == 0 && goal.isEmpty() && target == null) {
                Toast.makeText(this@GoalsFormActivity, "Please fill in the fields before submitting", Toast.LENGTH_SHORT).show()
                return
            } else {
                viewModel.goal(AddGoalRequest(goal, amount, target.toString(), description))
            }
            Log.d("Goal", "uploadGoal: $goal, $amount, $target, $description")
        }
    }
}