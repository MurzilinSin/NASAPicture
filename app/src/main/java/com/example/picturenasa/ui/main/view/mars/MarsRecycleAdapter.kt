package com.example.picturenasa.ui.main.view.mars

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.picturenasa.R
import com.example.picturenasa.databinding.RecyclerviewMarsItemBinding
import com.example.picturenasa.ui.main.marsRoverPhoto.RoversData
import com.squareup.picasso.Picasso

class MarsRecycleAdapter(private var onListItemClickListener: OnListItemClickListener,
                         private var roverData: MutableList<Pair<RoversData,Boolean>>
                        ) : RecyclerView.Adapter<BaseViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MarsRoverPhotoViewHolder(RecyclerviewMarsItemBinding.inflate(inflater,parent,false))
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bind(roverData[position])
    }

    override fun getItemCount(): Int {
        return roverData.size
    }

    inner class MarsRoverPhotoViewHolder(val binding: RecyclerviewMarsItemBinding ) : BaseViewHolder(binding.root){
        override fun bind(data: Pair<RoversData,Boolean>) {
            val url = data.first.img_src
            val name = data.first.rover.name
            val id = data.first.id
            val launchDate = data.first.rover.launch_date
            val landingDate = data.first.rover.landing_date
            binding.roverName.text = "Name of rover: ${name}"
            Picasso.get()
                .load(url)
                .placeholder(R.drawable.ic_no_photo_vector)
                .error(R.drawable.ic_load_error_vector)
                .into(binding.imageView);
            binding.roverId.text = "Number id: ${id.toString()}"
            binding.launchDate.text = "Launch date: ${launchDate}"
            binding.landingDate.text = "Landing date: ${landingDate}"
            binding.descriptionPhoto.visibility = if(data.second) View.VISIBLE else View.GONE
            binding.marsTextView.setOnClickListener { toggleText()}
        }
        private fun toggleText() {
            roverData[layoutPosition] = roverData[layoutPosition].let {
                it.first to !it.second
            }
            notifyItemChanged(layoutPosition)
        }
    }

    interface OnListItemClickListener {
        fun onItemClick(data: RoversData)
    }
}

abstract class BaseViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    abstract fun bind(data: Pair<RoversData,Boolean>)
}