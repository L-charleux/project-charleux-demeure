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

    /**
     * Called when the Details Activity is created
     * Initializes the Texts Views and the Images Views used to display the details of the PoI received from the Main Activity
     * Detects if the user toggles the parameter Favorite of the PoI by clicking on the corresponding Image View
     * If the parameter is toggled, sends the coordinates of the PoI to Main Activity to inform it when the Details Activity is closed
     */
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

        Glide.with(this) //Displays the image of the PoI
            .load(poiFromMain.pictureLink)
            .placeholder(R.drawable.ic_poi_logo)
            .error(R.drawable.ic_broken_image)
            .fallback(R.drawable.ic_broken_image)
            .into(imvImage)

        imvFavorite.setOnClickListener { //Detects the click on the Image View associated to the parameter Favorite
            changeFavorite = !changeFavorite //and displays the images corresponding to the actual state of this parameter
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

    /**
     * Called when this activity is closed and calls the function which sends the string to the Main Activity
     */
    override fun onBackPressed() {
        stopActivityAndReturnResult()
        super.onBackPressed()
    }

    /**
     * Returns the string of the Main Activity
     */
    private fun stopActivityAndReturnResult() {
        val returnIntent = Intent()
        var coordBecameFavorite = "notChanged"
        if (changeFavorite)
            coordBecameFavorite = poiFromMain.latitude.toString() + "," + poiFromMain.longitude.toString()
        returnIntent.putExtra(EXTRA_STRING, coordBecameFavorite)
        setResult(Activity.RESULT_OK, returnIntent)
        finish()
    }

}