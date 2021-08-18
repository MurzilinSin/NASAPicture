package com.example.picturenasa.ui.main.view

import android.os.Bundle
import android.transition.ChangeBounds
import android.transition.TransitionManager
import android.transition.Visibility
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnticipateOvershootInterpolator
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import coil.api.load
import com.example.picturenasa.R
import com.example.picturenasa.databinding.FragmentApodStartBinding
import com.example.picturenasa.ui.main.picture.PictureOfTheDayData
import com.example.picturenasa.ui.main.viemodel.PictureOfTheDayViewModel
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*

class APODFragment(private val day:String) : Fragment() {
    private var _binding : FragmentApodStartBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PictureOfTheDayViewModel by lazy {
        ViewModelProvider(this).get(PictureOfTheDayViewModel::class.java)
    }
    private var show = false
    private var textIsVisible = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentApodStartBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getData(setDate(day)).observe(viewLifecycleOwner, Observer<PictureOfTheDayData> {renderData(it)})
        binding.imageView.setOnClickListener { if(show) hideComponents() else showComponents()}
    }

    private fun showComponents() {
        show = true

        val constraintSet = ConstraintSet()
        constraintSet.clone(context,R.layout.fragment_apod_end)
        val transition = ChangeBounds()
        transition.interpolator = AnticipateOvershootInterpolator(1.0f)
        transition.duration = 200

        TransitionManager.beginDelayedTransition(binding.secondContainer, transition)
        constraintSet.applyTo(binding.secondContainer)
        binding.description.visibility = View.VISIBLE
    }

    private fun hideComponents() {
        show = false

        val constraintSet = ConstraintSet()
        constraintSet.clone(context,R.layout.fragment_apod_start)
        val transition = ChangeBounds()
        transition.interpolator = AnticipateOvershootInterpolator(1.0f)
        transition.duration = 200

        TransitionManager.beginDelayedTransition(binding.secondContainer, transition)
        constraintSet.applyTo(binding.secondContainer)
        binding.description.visibility = View.GONE
    }

    private fun setDate(time: String?): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
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

    private fun renderData(data: PictureOfTheDayData) {
        when(data) {
            is PictureOfTheDayData.Success -> {
                val serverResponseData = data.serverResponseData
                val url = serverResponseData.url
                println(url)
                val explanation = serverResponseData.explanation
                val title = serverResponseData.title
                val date = serverResponseData.date
                if (url.isNullOrEmpty()) {
                    //showError()
                } else {
//                    showSuccess()
                    println(serverResponseData.media_type)
                    binding.date.text = date
                    binding.description.text = explanation
                    binding.title.text = title
                    when(serverResponseData.media_type) {
                        "video" -> {
                            println("МЫ ПРИШЛИ СЮДА")

                        }
                        "image" -> {
                            binding.imageView.load(url) {
                                lifecycle(this@APODFragment)
                                error(R.drawable.ic_load_error_vector)
                                placeholder(R.drawable.ic_no_photo_vector)
                            }
                        }
                    }


                }

            }
            is PictureOfTheDayData.Loading -> {
            }

            is PictureOfTheDayData.Error -> {
                Picasso
                        .get()
                        .load("https://artismedia.by/blog/wp-content/uploads/2018/05/in-blog2-1.png")
                        .into(binding.imageView)

            }
        }
    }
}