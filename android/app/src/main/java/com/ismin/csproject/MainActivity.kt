package com.ismin.csproject

import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
//import android.view.Menu
//import android.view.MenuItem
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




class MainActivity : AppCompatActivity(), OnMapReadyCallback { //PoICreator

    private val poiStorage = PoIStorage()

    private lateinit var tabs: TabLayout
    private lateinit var viewPager: ViewPager
    private lateinit var viewPagerAdapter: ViewPagerAdapter
    //private lateinit var btnCreateBook: FloatingActionButton

    val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl("https://app-f6e9d866-668a-4c24-9828-5053e791145d.cleverapps.io/")
        .build()
    val poiService = retrofit.create(PoIService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadAllPoIs()

        tabs = findViewById(R.id.a_main_lyt_tab)
        viewPager = findViewById(R.id.a_main_vwp_fragment_container)
        viewPagerAdapter = ViewPagerAdapter(supportFragmentManager)


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

    override fun onMapReady(gmap: GoogleMap?) {
        if (gmap != null) {
            var icToPick: BitmapDescriptor;
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
            gmap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(46.1390412, 2.4349004)))
            gmap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(46.1390412, 2.4349004), 6F))
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

    private fun launchDetails() {

    }

    /*
    private fun displayPoIList() {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        val fragment = BookListFragment.newInstance(bookshelf.getAllBooks())
        fragmentTransaction.replace(R.id.a_main_lyt_fragment_container, fragment)
        fragmentTransaction.commit()
    }
     */

    /*
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.main_menu_delete -> {
                bookshelf.clear()
                displayBookList()
                true
            }
            // If we got here, the user's action was not recognized.
            else -> super.onOptionsItemSelected(item)
        }
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