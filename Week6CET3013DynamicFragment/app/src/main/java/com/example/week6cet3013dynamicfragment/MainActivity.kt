package com.example.week6cet3013dynamicfragment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.week6cet3013dynamicfragment.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //setup the radio group listener
        binding.groupFood.setOnCheckedChangeListener { group, checkedId ->

            //setup the fragment transaction
            val transaction = supportFragmentManager.beginTransaction()

            if(checkedId == R.id.radio_food1) {
                //create the fragment food A
                val fragment = FoodAFragment()

                //Add the fragment to the frameLayout (container)
                transaction.replace(R.id.container, fragment)
            }
            else {
                //create the fragment food B
                val fragment = FoodBFragment()

                //Add the fragment to the frameLayout (container)
                transaction.replace(R.id.container, fragment)
            }
            transaction.commit() //save the transaction
        }
    }
}