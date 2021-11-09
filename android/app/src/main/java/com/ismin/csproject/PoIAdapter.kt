package com.ismin.csproject

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.core.content.ContextCompat.startActivity

import android.content.Intent
import androidx.core.content.ContextCompat
import com.google.android.material.internal.ContextUtils.getActivity


class PoIAdapter(private val pois: ArrayList<PoI>): RecyclerView.Adapter<PoIViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PoIViewHolder {
        val row = LayoutInflater.from(parent.context).inflate(R.layout.row_poi, parent, false)
        val viewHolder = PoIViewHolder(row)
        val activity = viewHolder.itemView.context as MainActivity
        viewHolder.poi_item.setOnClickListener {
            activity.loadDetailedPoI(pois[viewHolder.adapterPosition].latitude, pois[viewHolder.adapterPosition].longitude)
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: PoIViewHolder, position: Int) {
        val (name, place, latitude, longitude, level, type, favorite) = pois[position]

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