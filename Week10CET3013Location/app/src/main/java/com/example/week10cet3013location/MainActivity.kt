package com.example.week10cet3013location

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.Debug.getLocation
import androidx.core.app.ActivityCompat
import com.example.week10cet3013location.databinding.ActivityMainBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    var mLastLocation: Location? = null
    var mFusedLocationClient: FusedLocationProviderClient? = null
    var result = "" //result returned from the thread
    val coroutineScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // Initialize the FusedLocationClient.
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(
            this
        )

        binding.buttonLocation.setOnClickListener {
            getLocation()
        }
    }

    fun getLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf<String>(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                MainActivity.REQUEST_LOCATION_PERMISSION
            )
        } else {
            //Log.d("Location", "Location Permission Granted")
            mFusedLocationClient!!.lastLocation.addOnSuccessListener { location ->
                if (location != null) {

                   mLastLocation = location
                    val locationText =
                        getString(R.string.location_text,
                            mLastLocation?.getLatitude(),
                            mLastLocation?.getLongitude(),
                            mLastLocation?.getTime())

                   // binding.textviewLocation.text = locationText
                    coroutineScope.launch(Dispatchers.Main) {
                        val addressObject = FetchAddressTask(this@MainActivity, location)

                        addressObject.fetchAddress()

                        result = addressObject.resultMessage
                        binding.textviewLocation.text = locationText + "\n\n" + result
                    }


                }
            else {
                binding.textviewLocation.setText(R.string.no_location)
            }
        }


    }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            REQUEST_LOCATION_PERMISSION ->             // If the permission is granted, get the location,
                // otherwise, show a Toast
                if (grantResults.size > 0
                    && grantResults[0] === PackageManager.PERMISSION_GRANTED
                ) {
                    getLocation()
                } else {
                    Toast.makeText(
                        this,
                        R.string.location_permission_denied,
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }

    }

    companion object {
        public val REQUEST_LOCATION_PERMISSION = 1
    }

}