package com.example.picturenasa.ui.main.view.apod

import android.graphics.Color
import android.graphics.Typeface.BOLD
import android.graphics.Typeface.ITALIC
import android.os.Build
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import coil.api.load
import com.example.picturenasa.R
import com.example.picturenasa.databinding.FragmentApodBinding
import com.example.picturenasa.ui.main.picture.PictureOfTheDayData
import com.example.picturenasa.ui.main.viemodel.PictureOfTheDayViewModel
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*

class APODFragment(private val day:String) : Fragment() {
    private var _binding : FragmentApodBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PictureOfTheDayViewModel by lazy {
        ViewModelProvider(this).get(PictureOfTheDayViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentApodBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getData(setDate(day)).observe(viewLifecycleOwner, Observer<PictureOfTheDayData> {renderData(it)})
        activity?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                binding.explanation.typeface = context?.let { it -> ResourcesCompat.getFont(it, R.font.dekko) }
            }
        }
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
                val explanation = serverResponseData.explanation
                if (url.isNullOrEmpty()) {
                    //showError()
                } else {
                    //showSuccess()
                    println(serverResponseData.media_type)
                    val spannableString = SpannableString(explanation)
                    val index = explanation?.indexOf(' ')
                    spannableString.let {
                        it.setSpan(UnderlineSpan(),0,index!!,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                        it.setSpan(StyleSpan(BOLD),0,index,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                    }
                    binding.explanation.text = spannableString
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