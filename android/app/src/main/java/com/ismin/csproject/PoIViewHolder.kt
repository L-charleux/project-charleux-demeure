package com.ismin.csproject

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PoIViewHolder(rootView: View): RecyclerView.ViewHolder(rootView) {

    var txvName = rootView.findViewById<TextView>(R.id.r_poi_txv_name)
    var txvPlace = rootView.findViewById<TextView>(R.id.r_poi_txv_place)
}