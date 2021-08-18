package com.example.picturenasa.ui.main.view.mars

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.picturenasa.databinding.FragmentMarsPhotosBinding
import com.example.picturenasa.ui.main.marsRoverPhoto.MarsRoverPhotoData
import com.example.picturenasa.ui.main.marsRoverPhoto.RoversData
import com.example.picturenasa.ui.main.viemodel.MarsCuriosityPhotoViewModel
import com.example.picturenasa.ui.main.view.main.BEFORE_YESTERDAY
import com.google.android.material.tabs.TabLayout
import java.text.SimpleDateFormat
import java.util.*

class MarsRoverPhotoFragment: Fragment() {
    private var _binding: FragmentMarsPhotosBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MarsCuriosityPhotoViewModel by lazy {
       ViewModelProvider(this).get(MarsCuriosityPhotoViewModel::class.java)
   }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMarsPhotosBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createTabLayout()
        viewModel.getData("curiosity", setDate(BEFORE_YESTERDAY)).observe(viewLifecycleOwner,
            { renderData(it) })
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab.text) {
                    "Curiosity" -> {
                        viewModel.getData("curiosity", setDate(BEFORE_YESTERDAY)).observe(viewLifecycleOwner,
                            { renderData(it) })
                    }
                    "Opportunity" -> {
                        viewModel.getData("opportunity", "2018-02-11").observe(viewLifecycleOwner,
                            { renderData(it) })
                    }
                    "Spirit" -> {
                        viewModel.getData("spirit", "2010-02-21").observe(viewLifecycleOwner,
                            { renderData(it) })
                    }
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {
                println(binding.tabLayout.selectedTabPosition)
            }
            override fun onTabReselected(tab: TabLayout.Tab) {
            }
        })
        binding.tabLayout.getTabAt(0)?.isSelected
        println(binding.tabLayout.getTabAt(0)?.isSelected)
        binding.tabLayout.selectTab(binding.tabLayout.getTabAt(0))
        binding.tabLayout.getTabAt(0)?.select()
    }

    private fun renderData(data: MarsRoverPhotoData) {
        when(data) {
            is MarsRoverPhotoData.Success -> {
                val serverResponseData = data.serverResponseData
                val pair = mutableListOf<Pair<RoversData,Boolean>>()
                for (photo in serverResponseData.photos!!) {
                    pair.add(Pair(photo,false))
                }
                binding.recycleView.adapter = MarsRecycleAdapter(object: MarsRecycleAdapter.OnListItemClickListener {
                    override fun onItemClick(data: RoversData) {
                    }
                },pair)
                binding.recycleView.animate()
            }
            is MarsRoverPhotoData.Loading -> {
            }
            is MarsRoverPhotoData.Error -> {
              println("MarsRoverPhotoData.Error")
            }
        }
    }

    private fun setDate(time: String?): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        val currentDate = Date()
        val calendar = Calendar.getInstance()
        calendar.time = currentDate
        return when(time) {
            "Yesterday" -> {
                calendar.add(Calendar.DATE,-1)
                sdf.format(calendar.time)
            }
            "Before" -> {
                calendar.add(Calendar.DATE,-2)
                sdf.format(calendar.time)
            }
            else -> {
                calendar.add(Calendar.DATE,0)
                sdf.format(calendar.time)
            }
        }
    }

    private fun createTabLayout() {
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Curiosity"))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Opportunity"))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Spirit"))
        binding.tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#FF0000"))
        binding.tabLayout.setTabTextColors(Color.parseColor("#851d05"), Color.parseColor("#ffffff"))
    }
}