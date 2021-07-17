package com.example.picturenasa.ui.main.view

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import coil.api.load
import com.example.picturenasa.R
import com.example.picturenasa.databinding.FragmentMarsPhotosBinding
import com.example.picturenasa.ui.main.marsWeather.MarsRoverPhotoData
import com.example.picturenasa.ui.main.viemodel.MarsCuriosityPhotoViewModel
import com.google.android.material.tabs.TabLayout
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*

class MarsRoverPhotoFragment: Fragment() {
    private var _binding: FragmentMarsPhotosBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MarsCuriosityPhotoViewModel by lazy {
       ViewModelProvider(this).get(MarsCuriosityPhotoViewModel::class.java)
   }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentMarsPhotosBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createTabLayout()
        viewModel.getData("curiosity", setDate(BEFORE_YESTERDAY)).observe(viewLifecycleOwner, Observer<MarsRoverPhotoData> { renderData(it) })
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab.text) {
                    "Curiosity" -> {
                        viewModel.getData("curiosity", setDate(BEFORE_YESTERDAY)).observe(viewLifecycleOwner, Observer<MarsRoverPhotoData> { renderData(it) })
                    }
                    "Opportunity" -> {
                        viewModel.getData("opportunity", "2018-02-11").observe(viewLifecycleOwner, Observer<MarsRoverPhotoData> { renderData(it) })
                    }
                    "Spirit" -> {
                        viewModel.getData("spirit", "2010-02-21").observe(viewLifecycleOwner, Observer<MarsRoverPhotoData> { renderData(it) })
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                //binding.viewPager.currentItem = tab.position
                println(binding.tabLayout.selectedTabPosition)
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
                //binding.viewPager.currentItem = tab.position
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
                val url = serverResponseData.photos?.get(0)?.img_src
                val url1 = serverResponseData.photos?.get(serverResponseData.photos.size - 1)?.img_src
                val name = serverResponseData.photos?.get(0)?.rover?.name
                val id = serverResponseData.photos?.get(0)?.id
                val launchDate = serverResponseData.photos?.get(0)?.rover?.launch_date
                val landingDate = serverResponseData.photos?.get(0)?.rover?.landing_date
                binding.includedPhotoFragment.imageView.load(url) {
                    lifecycle(this@MarsRoverPhotoFragment)
                    error(R.drawable.ic_load_error_vector)
                    placeholder(R.drawable.ic_no_photo_vector)
                }
                binding.includedPhotoFragment.imageView1.load(url1) {
                    lifecycle(this@MarsRoverPhotoFragment)
                    error(R.drawable.ic_load_error_vector)
                    placeholder(R.drawable.ic_no_photo_vector)
                }
                binding.roverName.text = "Name of rover: ${name}"
                binding.landingDate.text = "Landing date: ${landingDate}"
                binding.launchDate.text = "Launch date: ${launchDate}"
                binding.roverId.text = "Number id: ${id.toString()}"

            }
            is MarsRoverPhotoData.Loading -> {
            }

            is MarsRoverPhotoData.Error -> {
              println("MarsRoverPhotoData.Error")
            }
        }
    }

    private fun setDate(time: String?): String {
        var sdf = SimpleDateFormat("yyyy-MM-dd")
        var currentDate = Date()
        var calendar = Calendar.getInstance()
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
        binding.tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#FF0000"));
        binding.tabLayout.setTabTextColors(Color.parseColor("#851d05"), Color.parseColor("#ffffff"));
    }
}