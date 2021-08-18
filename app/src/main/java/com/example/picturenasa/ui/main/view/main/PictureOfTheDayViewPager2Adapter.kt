package com.example.picturenasa.ui.main.view.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.picturenasa.ui.main.view.apod.APODFragment

import java.lang.IllegalStateException

class PictureOfTheDayViewPager2Adapter (private val fragmentManager: FragmentManager,
                                        private val lifecycle: Lifecycle)
                : FragmentStateAdapter(fragmentManager,lifecycle) {

    override fun getItemCount(): Int {
        return fragments.size
    }

    private val fragments = arrayListOf<Fragment>(
            APODFragment(TODAY),
            APODFragment(YESTERDAY),
            APODFragment(BEFORE_YESTERDAY)
    )

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> APODFragment(TODAY)
            1 -> APODFragment(YESTERDAY)
            2 -> APODFragment(BEFORE_YESTERDAY)
            else -> throw IllegalStateException("This is your arrays of fragment count = ${fragments.size}")
        }
    }
}