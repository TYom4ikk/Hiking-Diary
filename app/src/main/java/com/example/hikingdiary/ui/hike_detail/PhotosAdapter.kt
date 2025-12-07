package com.example.hikingdiary.ui.hike_detail

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.hikingdiary.R
import com.example.hikingdiary.data.models.Photo

class PhotosAdapter(
    private val items: List<Photo>
) : RecyclerView.Adapter<PhotosAdapter.PhotoViewHolder>() {

    class PhotoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgPhoto: ImageView = itemView.findViewById(R.id.imgPhoto)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_photo, parent, false)
        return PhotoViewHolder(view)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val photo = items[position]
        Glide.with(holder.imgPhoto.context)
            .load(Uri.parse(photo.uri))
            .into(holder.imgPhoto)
    }

    override fun getItemCount(): Int = items.size
}
