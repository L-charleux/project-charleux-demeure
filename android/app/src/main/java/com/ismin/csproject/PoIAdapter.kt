package com.ismin.csproject

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView


class PoIAdapter(private val pois: ArrayList<PoI>): RecyclerView.Adapter<PoIViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PoIViewHolder {
        val row = LayoutInflater.from(parent.context).inflate(R.layout.row_poi, parent, false)
        val viewHolder = PoIViewHolder(row)
        val activity = viewHolder.itemView.context as MainActivity //Reference to the Main Activity
        viewHolder.poi_item.setOnClickListener { //Detects the click on an item of the Recycler View
            activity.loadDetailedPoI(pois[viewHolder.adapterPosition].latitude, pois[viewHolder.adapterPosition].longitude) //Calls the function which loads the details of the PoI which has been clicked
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: PoIViewHolder, position: Int) {
        val (name, place, latitude, longitude, level, type, favorite) = pois[position]
        holder.txvName.text = name
        holder.txvPlace.text = place

        if (favorite) { //Display the images corresponding to the state of the parameter Favorite of the PoI
            holder.imvFavorite.setImageResource(R.drawable.ic_favorite)
        } else {
            holder.imvFavorite.setImageResource(R.drawable.ic_not_favorite)
        }

        val activity = holder.itemView.context as MainActivity //Reference to the Main Activity
        holder.imvFavorite.setOnClickListener { //Detects the click on the Image View associated to the parameter Favorite
            activity.sendFavoritePoI(pois[holder.adapterPosition].latitude, pois[holder.adapterPosition].longitude) //Calls the function which informs the API that
        }                                                                                                           //the PoIs of coordinates (latitude, longitude) has been put in favorite
    }

    override fun getItemCount(): Int {
        return pois.size
    }

    /**
     * Refreshes the recycler view with a new list of PoIs
     */
    fun refreshData(newPoIs: ArrayList<PoI>) {
        pois.clear()
        pois.addAll(newPoIs)
    }
}