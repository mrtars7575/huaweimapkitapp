package com.example.huaweimapkitapp.view

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.huaweimapkitapp.ChargeModel
import com.example.huaweimapkitapp.Connection
import com.example.huaweimapkitapp.databinding.FragmentMapBinding
import com.example.huaweimapkitapp.service.ChargeAPI
import com.huawei.hms.maps.*
import com.huawei.hms.maps.model.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MapFragment : Fragment() , OnMapReadyCallback  {

    private lateinit var binding : FragmentMapBinding

    private lateinit var huaweiMap: HuaweiMap
    private lateinit var cameraUpdate: CameraUpdate
    private lateinit var cameraPosition: CameraPosition
    private lateinit var mapView : MapView
    // private var call:Call<List<ChargeModel>> ?=null

    var latitude : Double? = null
    var longitude : Double? = null
    var distance : Int? = null
    var countrycode : String = ""

    //retrofit
    private val BASE_URL = "https://api.openchargemap.io/v3/";
    private var chargeModels : ArrayList<ChargeModel> ?= null
    private var connectionsList : ArrayList<Connection>?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().setTitle("Charging Stations")
        takeSearchInfo()

        MapsInitializer.setApiKey("DAEDADBfDgUVHydgn/rZGmpU7Sw4nGraSNn1rA4rfcYD59JG1XxAztkZCvdV4AnplUsEKyu8UslA9+MYc6zxQAl9t29QSYs97hfhgQ==")
        MapsInitializer.initialize(context)

        loadData()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMapBinding.inflate(inflater,container,false)

        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        mapView = binding.huaweiMapView


        var mapViewBundle: Bundle? = null
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle("Map Fragment")
        }

        mapView.onCreate(mapViewBundle)
        mapView.getMapAsync(this)

    }
    private fun takeSearchInfo(){
        val bundle = arguments
        if(bundle!=null){
            latitude = bundle.getDouble("latitude")
            longitude = bundle.getDouble("longitude")
            distance = bundle.getInt("distance")
            countrycode = bundle.getString("countrycode").toString()

        }
    }
    //retrofit function
     private fun loadData(){
         val retrofit = Retrofit.Builder()
             .baseUrl(BASE_URL)
             .addConverterFactory(GsonConverterFactory.create())
             .build()

         val service: ChargeAPI = retrofit.create(ChargeAPI::class.java)

        val call :Call<List<ChargeModel>> = when {
            latitude !=null && latitude != 0.0 && longitude != null && longitude != 0.0 -> {
                if(countrycode!=null && countrycode!=""){
                    service.getData(latitude = latitude, longitude = longitude, distance = distance!!, countryCode = countrycode, key = "f9eca3e4-ebd1-4fd8-ad45-7713db5b0bd2", )
                }else{
                    service.getDataToCountrCode(distance = distance!!, countryCode = countrycode, key = "f9eca3e4-ebd1-4fd8-ad45-7713db5b0bd2" )
                }
            }
            countrycode != null && countrycode!="" -> {
                service.getDataToCountrCode(countryCode = countrycode, distance = distance!!,key = "f9eca3e4-ebd1-4fd8-ad45-7713db5b0bd2" )
            }
            else -> {
                Toast.makeText(requireContext(), "Invalid input", Toast.LENGTH_SHORT).show()
                return
            }

        }


        call.enqueue(object : Callback<List<ChargeModel>> {
            override fun onResponse(
                call: Call<List<ChargeModel>>,
                response: Response<List<ChargeModel>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        chargeModels = ArrayList(it)
                        Log.e(ContentValues.TAG, "on response raw: ${response.raw()}")
                    }
                }
            }

            override fun onFailure(call: Call<List<ChargeModel>>, t: Throwable) {
                // Handle the network failure
                Log.e(ContentValues.TAG, "onFailure: ${t.message}")
            }
        })
     }

    companion object {
        private const val TAG = "MapFragment"
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
        chargeModels!!.forEach { chargeModel ->
             huaweiMap?.addMarker(
                MarkerOptions()
                    .icon(BitmapDescriptorFactory.defaultMarker()) //default marker
                    .title(chargeModel.addressInfo!!.title.toString()) // maker title
                    .position(LatLng(chargeModel.addressInfo!!.latitude!!, chargeModel.addressInfo!!.longitude!!))

            )
        }

        //camera position settings
        cameraPosition = CameraPosition.builder()
            .target(LatLng(chargeModels!![0].addressInfo!!.latitude!!,chargeModels!![0].addressInfo!!.longitude!!))
            .zoom(10f)
            .bearing(2.0f)
            .tilt(2.5f).build()
        cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition)
        huaweiMap.moveCamera(cameraUpdate)



        huaweiMap.setOnInfoWindowClickListener {marker ->
            val selectedMarkerTitle =marker.title
            val selectedMarkerLatitude = marker.position.latitude
            val selectedMarkerLongitude = marker.position.longitude

            chargeModels!!.forEach{ chargeModel ->
                if(chargeModel!!.addressInfo!!.title == selectedMarkerTitle
                    && chargeModel!!.addressInfo!!.latitude == selectedMarkerLatitude
                    && chargeModel!!.addressInfo!!.longitude == selectedMarkerLongitude
                ) {
                    connectionsList = chargeModel.connections as ArrayList<Connection>?

                    val action = MapFragmentDirections.actionMapFragmentToDetailFragment(chargeModel)
                    findNavController().navigate(action)
                 }

            }

        }


    }

}