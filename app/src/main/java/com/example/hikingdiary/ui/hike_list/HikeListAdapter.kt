package com.example.hikingdiary.ui.hike_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hikingdiary.R

class HikeListAdapter : RecyclerView.Adapter<HikeListAdapter.HikeViewHolder>() {

    class HikeViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.item_hike, parent, false))

    override fun getItemCount(): Int = 0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        HikeViewHolder(LayoutInflater.from(parent.context), parent)

    override fun onBindViewHolder(holder: HikeViewHolder, position: Int) {}
}
