package com.ismin.csproject

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class PoIAdapter(private val pois: ArrayList<PoI>): RecyclerView.Adapter<PoIViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PoIViewHolder {
        val row = LayoutInflater.from(parent.context).inflate(R.layout.row_poi, parent, false)
        return PoIViewHolder(row)
    }

    override fun onBindViewHolder(holder: PoIViewHolder, position: Int) {
        val (name, place, latitude, longitude, badge, commentary, level, type, pictureLink) = pois[position]

        holder.txvName.text = name
        holder.txvPlace.text = place
    }

    override fun getItemCount(): Int {
        return pois.size
    }

    fun refreshData(newPoIs: ArrayList<PoI>) {
        pois.clear()
        pois.addAll(newPoIs)
    }
}