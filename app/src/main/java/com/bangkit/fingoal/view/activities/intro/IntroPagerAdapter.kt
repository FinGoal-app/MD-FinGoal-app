package com.bangkit.fingoal.view.activities.intro

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class IntroPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FirstIntroFragment()
            1 -> SecondIntroFragment()
            2 -> ThirdIntroFragment()
            else -> throw IllegalStateException("Invalid position: $position")
        }
    }
}