package com.ismin.csproject

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.tabs.TabLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.graphics.drawable.VectorDrawable
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import java.util.*

class MainActivity : AppCompatActivity(), OnMapReadyCallback { //PoICreator

    private val poiStorage = PoIStorage()

    private lateinit var tabs: TabLayout
    private lateinit var viewPager: ViewPager
    private lateinit var viewPagerAdapter: ViewPagerAdapter

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl("https://project-lcx-tde.cleverapps.io/")
        .build()
    private val poiService = retrofit.create(PoIService::class.java)

    val EXTRA_POI = "extra-poi"
    val EXTRA_STRING = "extra-string"

    /**
     * Used to launch the Details Activity to receive the string changeFavorite from it
     * This string contains the coordinates of the PoI which has been displayed if the user
     * toggled its favorite parameter
     * If this parameter has been toggled, the function which informs the API is called
     */
    private val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val changeFavorite = result.data?.getSerializableExtra(EXTRA_STRING) as String
            if (changeFavorite != "notChanged") {
                val latitude = changeFavorite.split(",")[0].toDouble()
                val longitude = changeFavorite.split(",")[1].toDouble()
                sendFavoritePoI(latitude, longitude)
            }
        }
    }

    /**
     * Called when the Main Activity is created
     * Initializes the Tab Layout, the View Pager and its adapter
     * Calls the function which loads all the PoIs from the API
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tabs = findViewById(R.id.a_main_lyt_tab)
        viewPager = findViewById(R.id.a_main_vwp_fragment_container)
        viewPagerAdapter = ViewPagerAdapter(supportFragmentManager)

        loadAllPoIs()
    }

    /**
     * Loads the dataset of PoIs from the API and call the function which initializes the display
     */
    private fun loadAllPoIs() {
        poiService.getAllPoIs().enqueue(object : Callback<List<PoI>> {
            override fun onResponse(
                call: Call<List<PoI>>,
                response: Response<List<PoI>>
            ) {
                val allPoIs: List<PoI>? = response.body()

                allPoIs?.forEach {
                    poiStorage.addPoI(it)
                }
                initializeDisplay()
            }

            override fun onFailure(call: Call<List<PoI>>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Error when trying to fetch PoIs" + t.localizedMessage, Toast.LENGTH_LONG).show()
            }
        }
        )
    }

    /**
     * Reloads the dataset of PoIs from the API and call the function which refreshes the display
     */
    private fun reloadAllPoIs() {
        poiStorage.clear()
        poiService.getAllPoIs().enqueue(object : Callback<List<PoI>> {
            override fun onResponse(
                call: Call<List<PoI>>,
                response: Response<List<PoI>>
            ) {
                val allPoIs: List<PoI>? = response.body()

                allPoIs?.forEach {
                    poiStorage.addPoI(it)
                }
                refreshDisplay()
            }

            override fun onFailure(call: Call<List<PoI>>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Error when trying to fetch PoIs" + t.localizedMessage, Toast.LENGTH_LONG).show()
            }
        }
        )
    }

    /**
     * Loads the details about the PoI of coordinates (latitude,longitude) from the API and starts the Details Activity
     */
    fun loadDetailedPoI(latitude: Double, longitude: Double) {
        poiService.getDetailedPoI(latitude.toString(), longitude.toString()).enqueue(object : Callback<DetailedPoI> {
            override fun onResponse(
                call: Call<DetailedPoI>,
                response: Response<DetailedPoI>
            ) {
                val detailedPoI: DetailedPoI? = response.body()
                val intent = Intent(this@MainActivity, DetailsActivity::class.java)
                intent.putExtra(EXTRA_POI, detailedPoI)
                startForResult.launch(intent)
            }

            override fun onFailure(call: Call<DetailedPoI>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Error when trying to fetch detailed PoI" + t.localizedMessage, Toast.LENGTH_LONG).show()
            }
        }
        )
    }

    /**
     * Sends a http request to inform the API a PoI has been put in favorite and reloads the dataset of PoIs
     */
    fun sendFavoritePoI(latitude: Double, longitude: Double) {
        poiService.putFavoritePoI(latitude.toString(), longitude.toString())
            .enqueue(object : Callback<PoI> {
                override fun onResponse(
                    call: Call<PoI>,
                    response: Response<PoI>
                ) {
                    val poi: PoI? = response.body()

                    poi?.let {
                        reloadAllPoIs()
                    }
                }

                override fun onFailure(call: Call<PoI>, t: Throwable) {
                    Toast.makeText(this@MainActivity, "Error when trying to commute favorite state of a poi" + t.localizedMessage, Toast.LENGTH_LONG).show()
                }
            }
            )
    }

    /**
     * Initializes the Display of the Tab Layout and the View Pager containing the fragments of the list, the map and the general information
     */
    private fun initializeDisplay() {
        viewPagerAdapter.addFragment(PoIListFragment.newInstance(poiStorage.getAllPoIs()), "") //Creates a new fragment for the PoI List and add it to the View Pager Adapter
        val supportMapFragment = SupportMapFragment.newInstance() //Creates a new fragment for the map
        supportMapFragment.getMapAsync(this)
        viewPagerAdapter.addFragment(supportMapFragment, "") //Add the map fragment to the View Pager Adapter
        viewPagerAdapter.addFragment(InfoFragment(), "") //Creates a new fragment for the general information and add it to the View Pager Adapter

        viewPager.adapter = viewPagerAdapter
        tabs.setupWithViewPager(viewPager)

        tabs.getTabAt(0)!!.setIcon(R.drawable.ic_list)
        tabs.getTabAt(1)!!.setIcon(R.drawable.ic_maps)
        tabs.getTabAt(2)!!.setIcon(R.drawable.ic_info)
    }

    /**
     * Refreshes the display of the fragments of the list and the map
     */
    private fun refreshDisplay() {
        val poiListFragment = viewPagerAdapter.getItem(0) as PoIListFragment
        poiListFragment.refreshList(poiStorage.getAllPoIs())
        val supportMapFragment = viewPagerAdapter.getItem(1) as SupportMapFragment
        supportMapFragment.getMapAsync(this)
    }

    /**
     * Triggered when the function "getMapAsync" is called
     * Removes all the markers and creates new ones for all the PoIs of the poiStorage with the good icons
     * Centers the map on the city of "Gardanne"
     */
    override fun onMapReady(gmap: GoogleMap?) {
        if (gmap != null) {
            var icToPick: BitmapDescriptor
            gmap.clear()
            poiStorage.getAllPoIs().forEach {

                icToPick = when {
                    it.type == "Pokestop" -> {
                        BitmapDescriptorFactory.fromBitmap(getBitmap(getDrawable(R.drawable.ic_pokestop) as VectorDrawable))
                    }
                    it.level == "Gold" -> {
                        BitmapDescriptorFactory.fromBitmap(getBitmap(getDrawable(R.drawable.ic_golden_gym) as VectorDrawable))
                    }
                    else -> {
                        BitmapDescriptorFactory.fromBitmap(getBitmap(getDrawable(R.drawable.ic_gym) as VectorDrawable))
                    }
                }

                gmap.addMarker(
                    MarkerOptions()
                        .position(LatLng(it.latitude, it.longitude))
                        .title(it.name)
                        .icon(icToPick)
                )
            }
            gmap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(43.444984, 5.479419)))
            gmap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(43.444984, 5.479419), 13F))
        }
    }

    /**
    Converts a VectorDrawable into a Bitmap
    This function is used for the marker icon on the map
     */
    private fun getBitmap(vectorDrawable: VectorDrawable): Bitmap? {
        val bitmap = Bitmap.createBitmap(
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)
        vectorDrawable.draw(canvas)
        return bitmap
    }

    /**
    Creates a menu in the Main Activity
    */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    /**
    Detects the click on an item of the menu
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.main_menu_refresh -> { //Triggered when the user clicks on the refresh button
                reloadAllPoIs()
                true
            }
            // If we got here, the user's action was not recognized.
            else -> super.onOptionsItemSelected(item)
        }
    }
}