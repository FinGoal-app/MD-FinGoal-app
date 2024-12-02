package com.bangkit.fingoal.view.activities.intro

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.bangkit.fingoal.R
import com.bangkit.fingoal.databinding.FragmentSecondIntroBinding

class SecondIntroFragment : Fragment() {

    private var _binding: FragmentSecondIntroBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSecondIntroBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSkip2.setOnClickListener {
            val intent = Intent(requireContext(), GetStartedActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

        binding.btnNext2.setOnClickListener {
            val viewPager = requireActivity()
                .findViewById<ViewPager2>(R.id.view_pager)
            viewPager.currentItem = 2
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}