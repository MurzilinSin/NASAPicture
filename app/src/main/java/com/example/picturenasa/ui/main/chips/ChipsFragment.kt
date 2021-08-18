package com.example.picturenasa.ui.main.chips

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.picturenasa.databinding.ChipsFragmentBinding
import com.google.android.material.chip.Chip

class ChipsFragment: Fragment() {

    private var _binding: ChipsFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = ChipsFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.chipGroup.setOnCheckedChangeListener {chipGroup, position ->
            chipGroup.findViewById<Chip>(position)?.let {
                Toast.makeText(context, "Выбран ${it.text}", Toast.LENGTH_SHORT).show()}
        }
        binding.chipClose.setOnCloseIconClickListener {
            Toast.makeText(context,"Закрыть через Chip", Toast.LENGTH_SHORT).show()
        }
    }
}