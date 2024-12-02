package com.bangkit.fingoal.view.activities.goals

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bangkit.fingoal.databinding.ActivityGoalsBinding

class AddGoalsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGoalsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGoalsBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}