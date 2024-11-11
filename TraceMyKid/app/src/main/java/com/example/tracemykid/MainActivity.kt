package com.example.tracemykid

import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.tracemykid.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var isChecked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Add the Action Bar at here
        setSupportActionBar(binding.toolbar)
        // Hide the default title as wanted to show customized title
        supportActionBar?.setDisplayShowTitleEnabled(false)

        // Spinner
        val items= resources.getStringArray(R.array.category)
        val spinnerAdapter= object : ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, items) {

            override fun isEnabled(position: Int): Boolean {
                // Disable the first item from Spinner
                // First item will be used for hint
                return position != 0
            }

            override fun getDropDownView(
                position: Int,
                convertView: View?,
                parent: ViewGroup
            ): View {
                val view: TextView = super.getDropDownView(position, convertView, parent) as TextView
                //set the color of first item in the drop down list to gray
                if(position == 0) {
                    view.setTextColor(Color.GRAY)
                } else {
                    //here it is possible to define color for other items by
                    //view.setTextColor(Color.RED)
                }
                return view
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        //create the menu bar
        menuInflater.inflate(com.example.tracemykid.R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    // Toolbar item
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            com.example.tracemykid.R.id.menu_about -> {
                showDialog()
            }

            else -> {
            }
        }
        return super.onOptionsItemSelected(item)
    }

    // Show App Information
    private fun showDialog() {
        val dialog = AboutDialog()
        dialog.show(supportFragmentManager, "123")
    }
}
