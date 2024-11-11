package com.example.week11cet3013sensorsurvey

import android.hardware.Sensor
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    private var mSensorManager: SensorManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //get the sensor manager
        mSensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        val sensorList = mSensorManager!!.getSensorList(Sensor.TYPE_ALL)
    }
}