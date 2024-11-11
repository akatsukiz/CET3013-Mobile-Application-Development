package com.example.week5cet3013colourpicker

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.week5cet3013colourpicker.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private lateinit var startForColorResult: ActivityResultLauncher<Intent>
    private lateinit var pref: SharedPreferences
    private lateinit var colorName:String
    private lateinit var message:String
    var color = 0
    val FILE = "mypref"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //show the toolbar
        setSupportActionBar(binding.toolbar)

        //create sharedprerences object
        pref = getSharedPreferences(FILE, MODE_PRIVATE)

        reloadSetting()
        startForColorResult = registerForActivityResult<Intent, ActivityResult>(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult ->


            if (result.resultCode == RESULT_OK) {
                val intent = result.data

                //Recover the returned data
                colorName = intent!!.getStringExtra(RAINBOW_COLOR_NAME)!!
                color = intent.getIntExtra(RAINBOW_COLOR, R.color.white)

                //form the favorite color message
                message = getString(R.string.color_chosen_message, colorName)

                //display the message to the
                binding.textResult.text = message
                binding.textResult.setBackgroundColor(
                    ContextCompat.getColor(this, color)
                )
            }

        }

        //invoke the button clicking event
        binding.buttonColor.setOnClickListener{

            val intent = Intent(this, ColourPickerActivity::class.java)
            startForColorResult.launch(intent)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu) //create the menu bar
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //save the color setting
        if (item.itemId == R.id.menu_item_save) {
            val editor = pref.edit()
            editor.putString(RAINBOW_COLOR_NAME, colorName)
            editor.putInt(RAINBOW_COLOR, color)

            //save the setting to the preferences file
            editor.commit()
            editor.apply()
            Toast.makeText(this, "Colour settings saved.", Toast.LENGTH_SHORT).show()
        }
        //display the alert dialog
        if(item.itemId == R.id.menu_item_about) {
            showDialog()
        }

        return super.onOptionsItemSelected(item)
    }

    private fun reloadSetting() {
        //recover the color
        colorName = pref.getString(RAINBOW_COLOR_NAME, "white").toString()
        color = pref.getInt(RAINBOW_COLOR, R.color.white)
        message = getString(R.string.color_chosen_message, colorName)

        binding.textResult.text = message
        binding.textResult.setBackgroundColor(ContextCompat.getColor(this, color))


    }

    private fun showDialog() {
        val dialog = AboutDialog()
        dialog.show(supportFragmentManager, "123")
    }

    companion object {
        const val RAINBOW_COLOR_NAME = "COLOR_NAME"
        const val RAINBOW_COLOR = "COLOR"
    }

}