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

    val EXTRA_POI = "extra-poi"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PoIViewHolder {
        val row = LayoutInflater.from(parent.context).inflate(R.layout.row_poi, parent, false)
        val viewHolder = PoIViewHolder(row)
        val activity = viewHolder.itemView.context as Activity
        viewHolder.poi_item.setOnClickListener {
            val intent = Intent(activity, DetailsActivity::class.java)
            intent.putExtra(EXTRA_POI, pois[viewHolder.adapterPosition])
            activity.startActivity(intent)
        }
        return viewHolder
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