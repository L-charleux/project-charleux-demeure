package com.ismin.csproject

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide




class DetailsActivity : AppCompatActivity() {

    private val EXTRA_POI = "extra-poi"
    private val EXTRA_STRING = "extra-string"
    private var changeFavorite = false
    private lateinit var poiFromMain : DetailedPoI

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        poiFromMain = intent.getSerializableExtra(EXTRA_POI) as DetailedPoI

        val txvName = findViewById<TextView>(R.id.a_details_txv_name)
        val txvPlace = findViewById<TextView>(R.id.a_details_txv_place)
        val txvLatitude = findViewById<TextView>(R.id.a_details_txv_latitude)
        val txvLongitude = findViewById<TextView>(R.id.a_details_txv_longitude)
        val txvType = findViewById<TextView>(R.id.a_details_txv_type)
        val txvCommentary = findViewById<TextView>(R.id.a_details_txv_commentary)
        val imvFavorite = findViewById<ImageView>(R.id.a_details_imv_favorite)
        val imvImage = findViewById<ImageView>(R.id.a_details_txv_image)

        txvName.text = poiFromMain.name
        txvPlace.text = "Place: " + poiFromMain.place
        txvLatitude.text = "Latitude: " + poiFromMain.latitude.toString()
        txvLongitude.text = "Latitude: " + poiFromMain.longitude.toString()
        txvType.text = "Type: " + poiFromMain.type + poiFromMain.level
        txvCommentary.text = "Commentary: " + poiFromMain.commentary

        if (poiFromMain.favorite) {
            imvFavorite.setImageResource(R.drawable.ic_favorite)
        } else {
            imvFavorite.setImageResource(R.drawable.ic_not_favorite)
        }

        Glide.with(this)
            .load(poiFromMain.pictureLink)
            .placeholder(R.drawable.ic_poi_logo)
            .error(R.drawable.ic_broken_image)
            .fallback(R.drawable.ic_broken_image)
            .into(imvImage)

        imvFavorite.setOnClickListener {
            changeFavorite = !changeFavorite;
            if (poiFromMain.favorite) {
                if (changeFavorite)
                    imvFavorite.setImageResource(R.drawable.ic_not_favorite)
                else
                    imvFavorite.setImageResource(R.drawable.ic_favorite)
            } else {
                if (changeFavorite)
                    imvFavorite.setImageResource(R.drawable.ic_favorite)
                else
                    imvFavorite.setImageResource(R.drawable.ic_not_favorite)
            }
        }
    }

    override fun onBackPressed() {
        stopActivityAndReturnResult()
        super.onBackPressed()
    }

    private fun stopActivityAndReturnResult() {
        val returnIntent = Intent()
        var coordBecameFavorite : String = "notChanged"
        if (changeFavorite)
            coordBecameFavorite = poiFromMain.latitude.toString() + "," + poiFromMain.longitude.toString()
        returnIntent.putExtra(EXTRA_STRING, coordBecameFavorite)
        setResult(Activity.RESULT_OK, returnIntent)
        finish()
    }

}