package com.bangkit.fingoal.view.activities.profile

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bangkit.fingoal.data.utils.formatRupiah
import com.bangkit.fingoal.databinding.ActivityProfileBinding
import com.bangkit.fingoal.view.ViewModelFactory
import com.bangkit.fingoal.view.activities.intro.GetStartedActivity
import com.bangkit.fingoal.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class ProfileActivity : AppCompatActivity() {

    private val viewModel: ProfileViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogout.setOnClickListener {
            MaterialAlertDialogBuilder(this)
                .setTitle("Logout")
                .setMessage("Are you sure want to logout?")
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }.setPositiveButton("Yes") { _, _ ->
                    viewModel.logout()
                    Toast.makeText(this, getString(R.string.logout_success), Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, GetStartedActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    finish()
                }.show()
        }

        setupProfile()
    }

    private fun setupProfile() {
        viewModel.getUserProfile()

        viewModel.name.observe(this) { name ->
            binding.tvUserName.text = name
        }

        viewModel.balance.observe(this) { balance ->
            binding.tvBalanceAmount.text = formatRupiah(balance)
        }

        viewModel.savings.observe(this) { savings ->
            binding.tvSavingsAmount.text = formatRupiah(savings)
        }

        viewModel.allocation.observe(this) { allocation ->
            binding.tvAllocationAmount.text = formatRupiah(allocation)
        }
    }
}