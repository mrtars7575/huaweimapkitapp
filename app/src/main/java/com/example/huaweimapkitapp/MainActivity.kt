package com.example.huaweimapkitapp

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.huawei.hms.maps.*
import com.huawei.hms.maps.model.*


class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var huaweiMap: HuaweiMap
    private lateinit var marker: Marker
    private lateinit var cameraUpdate: CameraUpdate
    private lateinit var cameraPosition: CameraPosition
    private lateinit var mapView :MapView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapsInitializer.setApiKey("DAEDADBfDgUVHydgn/rZGmpU7Sw4nGraSNn1rA4rfcYD59JG1XxAztkZCvdV4AnplUsEKyu8UslA9+MYc6zxQAl9t29QSYs97hfhgQ==")
        MapsInitializer.initialize(this)


        setContentView(R.layout.activity_main)

        mapView = findViewById(R.id.huaweiMapView)


        var mapViewBundle: Bundle? = null
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_BUNDLE_KEY)
        }

        mapView.onCreate(mapViewBundle)
        mapView.getMapAsync(this)

    }
    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }


    override fun onPause() {
        mapView.onPause()
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }



    override fun onMapReady(p0: HuaweiMap?) {
        println("onMapReady : ")
        //mapping
        huaweiMap = p0!!
        huaweiMap.mapType = HuaweiMap.MAP_TYPE_NORMAL

        //marker add
        marker = huaweiMap.addMarker(
            MarkerOptions()
                .icon(BitmapDescriptorFactory.defaultMarker()) //default marker
                .title("Huawei Turkey") // maker title
                .position(LatLng(41.031261, 29.117277)) //marker position

        )
        //camera position settings
        cameraPosition = CameraPosition.builder()
            .target(LatLng(41.031261, 29.117277))
            .zoom(10f)
            .bearing(2.0f)
            .tilt(2.5f).build()
        cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition)
        huaweiMap.moveCamera(cameraUpdate)
    }
    companion object {
        private const val MAP_BUNDLE_KEY = "MapBundleKey"
    }

}