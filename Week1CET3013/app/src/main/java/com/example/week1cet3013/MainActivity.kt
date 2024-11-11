package com.example.week1cet3013

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val name = "Chan Seow Fen"
        val TAG = "Week1"

        //Display a simple message
        Toast.makeText(this,"Welcome "+name, Toast.LENGTH_LONG).show()

        //Display the Logcat message (for debugging purpose)
        for (i in 1..10)
        {
            Log.d(TAG, "Hello "+i.toString())
        }
    }
}