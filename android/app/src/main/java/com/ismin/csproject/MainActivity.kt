package com.ismin.csproject

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
//import android.view.View
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
//import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.graphics.drawable.VectorDrawable
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.FragmentTransaction
import java.sql.Timestamp
import java.time.Instant
import java.util.*


class MainActivity : AppCompatActivity(), OnMapReadyCallback { //PoICreator

    private val poiStorage = PoIStorage()

    private lateinit var tabs: TabLayout
    private lateinit var viewPager: ViewPager
    private lateinit var viewPagerAdapter: ViewPagerAdapter
    //private lateinit var btnCreateBook: FloatingActionButton

    val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl("https://project-lcx-tde.cleverapps.io/")
        .build()
    val poiService = retrofit.create(PoIService::class.java)

    val EXTRA_POI = "extra-poi"
    val EXTRA_STRING = "extra-string"

    private val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val changeFavorite = result.data?.getSerializableExtra(EXTRA_STRING) as String
            if (changeFavorite != "notChanged") {
                var latitude = changeFavorite.split(",")[0].toDouble()
                var longitude = changeFavorite.split(",")[1].toDouble()
                sendFavoritePoI(latitude, longitude)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tabs = findViewById(R.id.a_main_lyt_tab)
        viewPager = findViewById(R.id.a_main_vwp_fragment_container)
        viewPagerAdapter = ViewPagerAdapter(supportFragmentManager)

        loadAllPoIs()

        /*
        btnCreateBook = findViewById(R.id.a_main_btn_create_book)
        btnCreateBook.setOnClickListener {
            displayCreateBook()
        }
         */
    }

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
                //startActivity(intent)
            }

            override fun onFailure(call: Call<DetailedPoI>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Error when trying to fetch detailed PoI" + t.localizedMessage, Toast.LENGTH_LONG).show()
            }
        }
        )
    }

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

    private fun initializeDisplay() {
        viewPagerAdapter.addFragment(PoIListFragment.newInstance(poiStorage.getAllPoIs()), "")
        var supportMapFragment = SupportMapFragment.newInstance()
        supportMapFragment.getMapAsync(this)
        viewPagerAdapter.addFragment(supportMapFragment, "")
        viewPagerAdapter.addFragment(InfoFragment(), "")

        viewPager.adapter = viewPagerAdapter
        tabs.setupWithViewPager(viewPager)

        tabs.getTabAt(0)!!.setIcon(R.drawable.ic_list)
        tabs.getTabAt(1)!!.setIcon(R.drawable.ic_maps)
        tabs.getTabAt(2)!!.setIcon(R.drawable.ic_info)
    }

    private fun refreshDisplay() {
        val poiListFragment = viewPagerAdapter.getItem(0) as PoIListFragment
        poiListFragment.refreshList(poiStorage.getAllPoIs())
        val supportMapFragment = viewPagerAdapter.getItem(1) as SupportMapFragment
        supportMapFragment.getMapAsync(this)
    }

    override fun onMapReady(gmap: GoogleMap?) {
        if (gmap != null) {
            var icToPick: BitmapDescriptor;
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
                        .title(it.name.toString())
                        .icon(icToPick)
                )
            }
            gmap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(43.444984, 5.479419)))
            gmap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(43.444984, 5.479419), 13F))
        }
    }

    private fun getBitmap(vectorDrawable: VectorDrawable): Bitmap? {
        val bitmap = Bitmap.createBitmap(
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight())
        vectorDrawable.draw(canvas)
        return bitmap
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.main_menu_refresh -> {
                reloadAllPoIs()
                true
            }
            // If we got here, the user's action was not recognized.
            else -> super.onOptionsItemSelected(item)
        }
    }

    /*
    private fun displayPoIList() {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        val fragment = BookListFragment.newInstance(bookshelf.getAllBooks())
        fragmentTransaction.replace(R.id.a_main_lyt_fragment_container, fragment)
        fragmentTransaction.commit()
    }

    private fun displayCreateBook() {
        btnCreateBook.visibility = View.GONE
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        val fragment = CreateBookFragment.newInstance()
        fragmentTransaction.replace(R.id.a_main_lyt_fragment_container, fragment)
        fragmentTransaction.commit()
    }

    override fun onBookCreated(book: Book) {
        bookService.createBook(book)
            .enqueue(object : Callback<Book> {
                override fun onResponse(
                    call: Call<Book>,
                    response: Response<Book>
                ) {
                    val createdBook: Book? = response.body()

                    createdBook?.let {
                        bookshelf.addBook(book);
                    }

                    displayBookList();
                }

                override fun onFailure(call: Call<Book>, t: Throwable) {
                    Toast.makeText(this@MainActivity, "Error when trying to create a book" + t.localizedMessage, Toast.LENGTH_LONG).show()
                }
            }
            )
    }
     */
}