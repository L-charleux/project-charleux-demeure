package com.ismin.csproject

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView

class PoIViewHolder(rootView: View): RecyclerView.ViewHolder(rootView) {

    var poi_item = rootView.findViewById<ConstraintLayout>(R.id.row_poi_lyt_item)
    var txvName = rootView.findViewById<TextView>(R.id.r_poi_txv_name)
    var txvPlace = rootView.findViewById<TextView>(R.id.r_poi_txv_place)
    var imvFavorite = rootView.findViewById<ImageView>(R.id.r_poi_imv_favorite)
}