package com.ismin.csproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide




class DetailsActivity : AppCompatActivity() {

    private val EXTRA_POI = "extra-poi"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        val poiFromMain = intent.getSerializableExtra(EXTRA_POI) as PoI

        val txvName = findViewById<TextView>(R.id.a_details_txv_name)
        val txvPlace = findViewById<TextView>(R.id.a_details_txv_place)
        val txvLatitude = findViewById<TextView>(R.id.a_details_txv_latitude)
        val txvLongitude = findViewById<TextView>(R.id.a_details_txv_longitude)
        val txvType = findViewById<TextView>(R.id.a_details_txv_type)
        val txvCommentary = findViewById<TextView>(R.id.a_details_txv_commentary)
        val imvImage = findViewById<ImageView>(R.id.a_details_txv_image)

        txvName.text = poiFromMain.name
        txvPlace.text = "Place: " + poiFromMain.place
        txvLatitude.text = "Latitude: " + poiFromMain.latitude.toString()
        txvLongitude.text = "Latitude: " + poiFromMain.longitude.toString()
        txvType.text = "Type: " + poiFromMain.type + poiFromMain.level
        txvCommentary.text = "Commentary: " + poiFromMain.commentary

        Glide.with(this)
            .load(poiFromMain.pictureLink)
            .placeholder(R.drawable.test)
            .error(R.drawable.ic_favorite)
            .fallback(R.drawable.ic_golden_gym)
            .into(imvImage)
    }
}