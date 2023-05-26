package com.example.huaweimapkitapp

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.huaweimapkitapp.databinding.FragmentSearchBinding
import com.google.android.material.snackbar.Snackbar
import com.huawei.hmf.tasks.OnFailureListener
import com.huawei.hmf.tasks.OnSuccessListener
import com.huawei.hmf.tasks.Task
import com.huawei.hms.common.ApiException
import com.huawei.hms.location.*
import com.huawei.hms.support.account.AccountAuthManager
import com.huawei.hms.support.account.request.AccountAuthParams
import com.huawei.hms.support.account.request.AccountAuthParamsHelper
import com.huawei.hms.support.account.service.AccountAuthService


class SearchFragment : Fragment() {

    private lateinit var binding : FragmentSearchBinding
    private lateinit var countryCode : String
    var latitude : Double? = null
    var longitude : Double? = null
    private var distance : Int? = null
    val requestMultiplePermissions = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ){
            permission -> permission.entries.forEach{
                Log.d(TAG,"${it.key} = ${it.key} ")
            }
    }
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    private lateinit var settingsClient :SettingsClient
    private lateinit var mLocationRequest:LocationRequest
    private lateinit var fusedLocationProviderClient : FusedLocationProviderClient




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        requestLocationPermission()
        registerLauncher()
        settingsClient = LocationServices.getSettingsClient(requireActivity())
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        checkLocationSettings()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //take user info
        takeUserInfo()

        //spinner
        val countryCodeList = listOf<String>("TR","US")
        val spinnerAdapter = ArrayAdapter(requireContext(),android.R.layout.simple_spinner_item,countryCodeList)
        binding.spinner.adapter = spinnerAdapter
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                countryCode = p0?.getItemAtPosition(p2) as String
                //Toast.makeText(context,selectedItem,Toast.LENGTH_SHORT).show()

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }

        //set distance

        var dist = binding.distanceEditText.text.toString()
        distance = dist.toIntOrNull()

        //clear location textview
        binding.clearTv.setOnClickListener {
            clearLocation()
        }

        //location button
        binding.locationBtn.setOnClickListener {
            if (hasLocationPermission()){
                if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    //request permission
                    if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.ACCESS_BACKGROUND_LOCATION)) {
                        Snackbar.make(binding.root, "Please replace the permission process for this app with --> {Allow all the time}", Snackbar.LENGTH_INDEFINITE).setAction("Give Permission") {
                            permissionLauncher.launch(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                        }.show()
                    } else {
                        permissionLauncher.launch(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                    }
                } else {
                    val mLocationRequest = LocationRequest()
                    // Set the location type.
                    mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                    // Set the number of location updates to 1.
                    mLocationRequest.numUpdates = 1

                    getLastKnownLocation()
                }
            }

        }


        //search button
        binding.searchBtn.setOnClickListener {

            navigateToMapFragment()

        }

    }

    private fun clearLocation(){
        longitude = null
        latitude = null

        binding.latTv.text =longitude
        binding.longTv.text = latitude
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.logout_menu,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.log_out_item){
            signOut()
        }
        return super.onOptionsItemSelected(item)

    }

    private fun registerLauncher(){
        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { result ->
            if (result) {
                //permission granted
                if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    getLastKnownLocation()
                }
            } else {
                //permission denied
                Toast.makeText(requireActivity(), "Permisson needed!", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun getLastKnownLocation(){
        val lastLocation = fusedLocationProviderClient.lastLocation
        lastLocation.addOnSuccessListener(OnSuccessListener { location ->
            if (location == null) {
                Toast.makeText(requireContext(),"",Toast.LENGTH_LONG).show()
                return@OnSuccessListener
            }

            latitude = location.latitude
            longitude = location.longitude

            binding.latTv.text = latitude.toString()
            binding.longTv.text = longitude.toString()

            // Log.e(TAG," lat : ${latitude}  long : ${longitude}")
            //Toast.makeText(requireContext(),"lat : ${lat} long : ${long}",Toast.LENGTH_LONG).show()

            // TODO: Define logic for processing the Location object upon success.
            return@OnSuccessListener
        })
            // Define callback for failure in obtaining the last known location.
            .addOnFailureListener {
                // TODO: Define callback for API call failure.
                Toast.makeText(requireContext(),it.message,Toast.LENGTH_LONG).show()
            }
    }

    private fun checkLocationSettings(){
        val builder = LocationSettingsRequest.Builder()
        mLocationRequest = LocationRequest()
        builder.addLocationRequest(mLocationRequest)
        val locationSettingsRequest = builder.build()
        // Check the device location settings.
        settingsClient.checkLocationSettings(locationSettingsRequest)
            // Define the listener for success in calling the API for checking device location settings.
            .addOnSuccessListener(OnSuccessListener { locationSettingsResponse ->
                val locationSettingsStates = locationSettingsResponse.locationSettingsStates
                val stringBuilder = StringBuilder()
                // Check whether the location function is enabled.
                stringBuilder.append("isLocationUsable=")
                    .append(locationSettingsStates.isLocationUsable)
                // Check whether HMS Core (APK) is available.
                stringBuilder.append(",\nisHMSLocationUsable=")
                    .append(locationSettingsStates.isHMSLocationUsable)
                Log.i(TAG, "checkLocationSetting onComplete:$stringBuilder")
            })
            // Define callback for failure in checking the device location settings.
            .addOnFailureListener(OnFailureListener { e ->
                Log.i(TAG, "checkLocationSetting onFailure:" + e.message)
            })
    }

    private fun requestLocationPermission(){
        if(!hasLocationPermission()){
            requestMultiplePermissions.launch(arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,

            ))
        }

    }

    fun hasLocationPermission() : Boolean {
        return ActivityCompat.checkSelfPermission(requireContext(),Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED
                ||  ActivityCompat.checkSelfPermission(requireContext(),Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED

    }

    private fun signOut(){
        val authParams: AccountAuthParams =
            AccountAuthParamsHelper(AccountAuthParams.DEFAULT_AUTH_REQUEST_PARAM).setAuthorizationCode()
                .createParams()
        val service: AccountAuthService =
            AccountAuthManager.getService(requireActivity(), authParams)

        service.cancelAuthorization().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Processing after a successful authorization cancellation.
                Log.i(TAG, "onSuccess: ")
            } else {
                // Handle the exception.
                val exception = task.exception
                if (exception is ApiException) {
                    val statusCode = exception.statusCode
                    Log.i(TAG, "onFailure: $statusCode")
                }
            }
        }
    }

    private fun navigateToMapFragment(){
        val action = SearchFragmentDirections.actionSearchFragmentToMapFragment()
        Navigation.findNavController(binding.searchBtn).navigate(action)
    }

    private fun takeUserInfo(){
        arguments?.let {
            val username = SearchFragmentArgs.fromBundle(it).username
            binding.userInfoTv.text = username
        }
    }

    private fun selectSpinnerItem(){

    }

    companion object {
        private const val TAG = "SearchFragment"
    }
}