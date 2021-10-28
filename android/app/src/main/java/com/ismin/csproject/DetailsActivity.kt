package com.ismin.csproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide

class DetailsActivity : AppCompatActivity() {

    val EXTRA_POI = "extra-poi"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        val poiFromMain = intent.getSerializableExtra(EXTRA_POI) as PoI

        var txvName = findViewById<TextView>(R.id.a_details_txv_name)
        var txvPlace = findViewById<TextView>(R.id.a_details_txv_place)
        var txvLatitude = findViewById<TextView>(R.id.a_details_txv_latitude)
        var txvLongitude = findViewById<TextView>(R.id.a_details_txv_longitude)
        var txvType = findViewById<TextView>(R.id.a_details_txv_type)
        var txvCommentary = findViewById<TextView>(R.id.a_details_txv_commentary)
        var imvImage = findViewById<ImageView>(R.id.a_details_txv_image);

        txvName.text = poiFromMain.name;
        txvPlace.text = "Place: " + poiFromMain.place;
        txvLatitude.text = "Latitude: " + poiFromMain.latitude.toString();
        txvLongitude.text = "Latitude: " + poiFromMain.longitude.toString();
        txvType.text = "Type: " + poiFromMain.type + poiFromMain.level;
        txvCommentary.text = "Commentary: " + poiFromMain.commentary;

        Glide.with(this)
            .load(poiFromMain.pictureLink)
            .placeholder(R.drawable.test)
            .error(R.drawable.ic_favorite)
            .fallback(R.drawable.ic_golden_gym)
            .into(imvImage)
    }
}