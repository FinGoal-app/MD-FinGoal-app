package com.bangkit.fingoal.view.activities.intro

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bangkit.fingoal.databinding.ActivityIntroBinding

class IntroActivity : AppCompatActivity() {

    private lateinit var binding: ActivityIntroBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewPager = binding.viewPager
        val adapter = IntroPagerAdapter(this)
        viewPager.adapter = adapter
        viewPager.isUserInputEnabled = false

        val dotsIndicator = binding.dotsIndicator
        dotsIndicator.attachTo(viewPager)
    }
}