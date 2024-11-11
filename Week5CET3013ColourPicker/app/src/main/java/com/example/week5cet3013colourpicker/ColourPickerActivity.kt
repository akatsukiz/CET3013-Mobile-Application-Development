package com.example.week5cet3013colourpicker

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.week5cet3013colourpicker.databinding.ActivityColourPickerBinding

class ColourPickerActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var binding: ActivityColourPickerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityColourPickerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //implement 7 buttons clicking
        binding.buttonRed.setOnClickListener(this)
        binding.buttonOrange.setOnClickListener(this)
        binding.buttonYellow.setOnClickListener(this)
        binding.buttonGreen.setOnClickListener(this)
        binding.buttonIndigo.setOnClickListener(this)
        binding.buttonBlue.setOnClickListener(this)
        binding.buttonViolet.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id)
        {
            R.id.button_red -> changeColor(getString(R.string.red), R.color.red)
            R.id.button_orange -> changeColor(getString(R.string.orange), R.color.orange)
            R.id.button_yellow -> changeColor(getString(R.string.yellow), R.color.yellow)
            R.id.button_green -> changeColor(getString(R.string.green), R.color.green)
            R.id.button_blue -> changeColor(getString(R.string.blue), R.color.blue)
            R.id.button_indigo -> changeColor(getString(R.string.indigo), R.color.indigo)
            R.id.button_violet -> changeColor(getString(R.string.violet), R.color.violet)
        }
    }

    private fun changeColor(colorName: String, color: Int) {
        //Return back the user selection to the MainActivity
        val resultIntent = Intent()
        resultIntent.putExtra(MainActivity.RAINBOW_COLOR_NAME, colorName)
        resultIntent.putExtra(MainActivity.RAINBOW_COLOR, color)

        //set the intent and pass back to MainActivity
        setResult(RESULT_OK, resultIntent)

        finish() //go back to MainActivity
    }

}