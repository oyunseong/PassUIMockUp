package com.passuimockup

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.passuimockup.databinding.ItemBannerBinding

class BannerAdapter(
    private val items: List<Int>
) : RecyclerView.Adapter<BannerAdapter.ViewHolder>() {

    inner class ViewHolder(
        private val binding: ItemBannerBinding,
//        private val onClick: (Int) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Int, position: Int) {
//            binding.number.text = "$item"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemBannerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position%items.size], position)
    }

    override fun getItemCount(): Int = Int.MAX_VALUE

}

