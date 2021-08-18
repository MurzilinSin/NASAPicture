package com.example.picturenasa.ui.main.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.picturenasa.databinding.FragmentMainStartBinding
import com.google.android.material.tabs.TabLayoutMediator

const val TODAY = "Today"
const val YESTERDAY = "Yesterday"
const val BEFORE_YESTERDAY = "Before"

class MainFragment : Fragment() {
    private var _binding : FragmentMainStartBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentMainStartBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
         binding.inputLayout.setEndIconOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("https://en.wikipedia.org/wiki/${binding.inputEditText.text.toString()}")
                binding.inputEditText.setText("")
            })
        }
        binding.viewPager.adapter = PictureOfTheDayViewPager2Adapter(childFragmentManager,lifecycle)
        TabLayoutMediator(binding.tabLayout,binding.viewPager) { _,_ ->
        }.attach()

        binding.indicator.setViewPager(binding.viewPager)
        setCustomTabs()
        binding.viewPager.setPageTransformer(ZoomOutPageTransformer())
    }
    private fun setCustomTabs() {
        binding.tabLayout.getTabAt(0)?.text = "Today"
        binding.tabLayout.getTabAt(1)?.text = "Yesterday"
        binding.tabLayout.getTabAt(2)?.text = "Before"
    }


    companion object {
        fun newInstance() = MainFragment()
    }
}