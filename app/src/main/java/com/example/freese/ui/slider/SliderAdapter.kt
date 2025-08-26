package com.example.freese.ui.slider

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.freese.databinding.SliderLayoutBinding // Perlu membuat layout item_slider_page.xml

class SliderAdapter(private val items: List<SliderItem>) : RecyclerView.Adapter<SliderAdapter.SliderViewHolder>() {

   // Anda perlu membuat file layout baru: res/layout/item_slider_page.xml
   // Isinya bisa berupa ImageView dan dua TextView di dalam LinearLayout/ConstraintLayout.
   override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewHolder {
      val binding = SliderLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
      return SliderViewHolder(binding)
   }

   override fun onBindViewHolder(holder: SliderViewHolder, position: Int) {
      holder.bind(items[position])
   }

   override fun getItemCount(): Int = items.size

   inner class SliderViewHolder(private val binding: SliderLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
      fun bind(sliderItem: SliderItem) {
         binding.idTVtitle .text = sliderItem.title
         binding.idTVheading .text = sliderItem.description
         Glide.with(itemView.context)
            .load(sliderItem.imageUrl)
            .into(binding.idIV)
      }
   }
}