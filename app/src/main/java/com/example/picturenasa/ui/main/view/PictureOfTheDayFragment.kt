package com.example.picturenasa.ui.main.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.MediaController
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import coil.api.load
import com.example.picturenasa.R
import com.example.picturenasa.databinding.FragmentPictureDayBinding
import com.example.picturenasa.ui.main.MainActivity
import com.example.picturenasa.ui.main.settings.SettingsFragment
import com.example.picturenasa.ui.main.picture.PictureOfTheDayData
import com.example.picturenasa.ui.main.viemodel.PictureOfTheDayViewModel
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*

const val YESTERDAY = "Yesterday"
const val BEFORE_YESTERDAY = "Before Yesterday"
const val TODAY = "Today"

class PictureOfTheDayFragment : Fragment() {
    private var _binding : FragmentPictureDayBinding? = null
    private val binding get() = _binding!!
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private val viewModel: PictureOfTheDayViewModel by lazy {
        ViewModelProvider(this).get(PictureOfTheDayViewModel::class.java)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentPictureDayBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.videoOfTheDay.visibility = View.GONE
        viewModel.getData(setDate(TODAY)).observe(viewLifecycleOwner,Observer<PictureOfTheDayData> {renderData(it)})
        setBottomBehaviour(binding.includedBottomSheet.bottomSheetContainer)
        setBottomAppBar()
        binding.inputLayout.setEndIconOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("https://en.wikipedia.org/wiki/${binding.inputEditText.text.toString()}")
                binding.inputEditText.setText("")
            })
        }
        binding.groupChipDays.setOnCheckedChangeListener { _, checkedId ->
                when(checkedId) {
                    R.id.yesterday -> {
                        viewModel.getData(setDate(YESTERDAY)).observe(this@PictureOfTheDayFragment as LifecycleOwner,Observer<PictureOfTheDayData> {renderData(it)})
                    }
                    R.id.before_yesterday -> {
                        viewModel.getData(setDate(BEFORE_YESTERDAY)).observe(this@PictureOfTheDayFragment as LifecycleOwner,Observer<PictureOfTheDayData> {renderData(it)})
                    }
                    R.id.today -> {
                        viewModel.getData(setDate(TODAY)).observe(this@PictureOfTheDayFragment as LifecycleOwner,Observer<PictureOfTheDayData> {renderData(it)})
                    }
                }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_bottom_bar,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.app_bar_fav -> Toast.makeText(context,"Избранное", Toast.LENGTH_SHORT)
                    .show()
            R.id.app_bar_settings -> {
                requireActivity()
                        .supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.container, SettingsFragment())
                        .addToBackStack("")
                        .commitAllowingStateLoss()
            }
            android.R.id.home -> {
                activity?.let {
                    BottomNavigationDrawerFragment.newInstance().show(parentFragmentManager, "tag")
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun renderData(data: PictureOfTheDayData) {
        when(data) {
            is PictureOfTheDayData.Success -> {
                val serverResponseData = data.serverResponseData
                val url = serverResponseData.url
                println(url)
                val explanation = serverResponseData.explanation
                val title = serverResponseData.title
                if (url.isNullOrEmpty()) {
                    //showError()
                } else {
//                    showSuccess()
                    println(serverResponseData.media_type)
                    binding.includedBottomSheet.bottomSheetDescription.text = explanation
                    binding.includedBottomSheet.bottomSheetDescriptionHeader.text = title
                    when(serverResponseData.media_type) {
                        "video" -> {
                            println("МЫ ПРИШЛИ СЮДА")
                            binding.videoOfTheDay.visibility = View.VISIBLE
                            binding.imageView.visibility = View.GONE
                            binding.videoOfTheDay.setVideoPath(url)
                            binding.videoOfTheDay.setMediaController(MediaController(context))
                            binding.videoOfTheDay.requestFocus(0)
                            binding.videoOfTheDay.start()
                        }
                        "image" -> {
                            binding.imageView.visibility = View.VISIBLE
                            binding.videoOfTheDay.visibility = View.GONE
                            binding.imageView.load(url) {
                                lifecycle(this@PictureOfTheDayFragment)
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

    private fun setBottomBehaviour(bottomSheet : ConstraintLayout) {
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    private fun setBottomAppBar() {
        val context = activity as MainActivity
        context.setSupportActionBar(binding.bottomAppBar)
        setHasOptionsMenu(true)
        binding.fab.setOnClickListener {
            if (isMain) {
                isMain = false
                binding.bottomAppBar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_END
                binding.fab.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_back_fab))
                binding.bottomAppBar.replaceMenu(R.menu.menu_bottom_bar_other_screen)
            } else {
                isMain = true
                binding.bottomAppBar.navigationIcon = ContextCompat.getDrawable(context,R.drawable.ic_hamburger_menu_bottom_bar)
                binding.bottomAppBar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_CENTER
                binding.fab.setImageDrawable(ContextCompat.getDrawable(context,
                        R.drawable.ic_plus_fab))
                binding.bottomAppBar.replaceMenu(R.menu.menu_bottom_bar)
            }
        }
    }

    private fun setDate(time: String?): String {
        var sdf = SimpleDateFormat("yyyy-MM-dd")
        var currentDate = Date()
        var calendar = Calendar.getInstance()
        calendar.time = currentDate
        /*calendar.add(Calendar.DATE,step)*/
        return when(time) {
            "Yesterday" -> {
                calendar.add(Calendar.DATE,-1)
                sdf.format(calendar.time)
            }
            "Before Yesterday" -> {
                calendar.add(Calendar.DATE,-2)
                sdf.format(calendar.time)
            }
            else -> {
                calendar.add(Calendar.DATE,0)
                sdf.format(calendar.time)
            }
        }
    }

    companion object {
        var isMain = true
        fun newInstance() = PictureOfTheDayFragment()
    }
}