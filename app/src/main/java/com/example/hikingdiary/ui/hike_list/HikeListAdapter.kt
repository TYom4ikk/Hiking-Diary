package com.example.hikingdiary.ui.hike_list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.hikingdiary.R
import com.example.hikingdiary.data.models.Hike

class HikeListAdapter(
    private var items: List<Hike>,
    private val onClick: (Hike) -> Unit
) : RecyclerView.Adapter<HikeListAdapter.HikeViewHolder>() {

    class HikeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgPreview: ImageView = itemView.findViewById(R.id.imgPreview)
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        val tvDate: TextView = itemView.findViewById(R.id.tvDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HikeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_hike, parent, false)
        return HikeViewHolder(view)
    }

    override fun onBindViewHolder(holder: HikeViewHolder, position: Int) {
        val hike = items[position]

        holder.tvTitle.text = hike.title
        holder.tvDate.text = hike.date

        if (hike.photos.isNotEmpty()) {
            Glide.with(holder.imgPreview.context)
                .load(hike.photos.first().uri)
                .into(holder.imgPreview)
        } else {
            holder.imgPreview.setImageResource(R.drawable.placeholder_photo)
        }

        holder.itemView.setOnClickListener {
            onClick(hike)
        }
    }

    override fun getItemCount(): Int = items.size

    fun update(newItems: List<Hike>) {
        items = newItems
        notifyDataSetChanged()
    }
}