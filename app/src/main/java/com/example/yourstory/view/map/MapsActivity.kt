package com.example.yourstory.view.map

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.yourstory.R
import com.example.yourstory.databinding.ActivityMapsBinding
import com.example.yourstory.model.repository.Repository
import com.example.yourstory.model.utils.SessionManager
import com.example.yourstory.viewmodel.map.MapsViewModel
import com.example.yourstory.viewmodel.map.MapsViewModelFactorys
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var viewModel: MapsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val repository = Repository()
        val sessionManager = SessionManager(this)
        val viewModelFactory = MapsViewModelFactorys(repository, sessionManager)
        viewModel = ViewModelProvider(this, viewModelFactory)[MapsViewModel::class.java]

        val token = viewModel.getToken() ?: ""
        viewModel.fetchAllStories("Bearer " + token , location = 1)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-6.3270288, 107.31042)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))

//        mMap.setOnMapLongClickListener { latLng ->
//            mMap.addMarker(MarkerOptions().position(latLng))
//        }

        viewModel.stories.observe(this) { stories ->

            for (story in stories) {
                val latLng = LatLng(story.lat, story.lon)
                mMap.addMarker(MarkerOptions().position(latLng).title(story.name))
            }

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(-6.3270288, 107.31042), 5f))
        }

    }
}