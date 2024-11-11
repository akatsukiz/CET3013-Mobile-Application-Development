package com.example.week4cet3013discount

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.week4cet3013discount.databinding.ActivityMainBinding
import java.util.Locale
import java.util.UUID

class MainActivity : AppCompatActivity() {
    lateinit var binding:ActivityMainBinding
    private lateinit var viewModel:Discount

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView((binding.root))

        //add the action bar at here
        setSupportActionBar(binding.toolbar)

        viewModel = ViewModelProvider(this).get(Discount::class.java)

        //recover the data
        //if the bundle has something then it won't be a null object
        /*if (bundle != null) { //java way
            val message = bundle.getString(DISCOUNT_MESSAGE)
            val discount = bundle.getString(DISCOUNT_CODE)

            //update the discount message and discount code
            binding.textMessage.text = message
            binding.textDiscount.text = discount
        }*/
        /*
        bundle?.apply { //kotlin way, if bundle null wont go in here, vice versa
            val message = getString(DISCOUNT_MESSAGE)
            val discount = getString(DISCOUNT_CODE)

            //update the discount message and discount code
            binding.textMessage.text = message
            binding.textDiscount.text = discount
        }
    */
        binding.buttonDiscount.setOnClickListener {
            //Task 1: Get the user name, email and region
            val userName: String = binding.textUsername.text.toString()
            val email: String = binding.textEmail.text.toString()
            val region: String = binding.spinnerRegion.selectedItem.toString()

            val errorMessage = getString(R.string.error_message)
            if (userName.isEmpty()) {
                binding.textUsername.error = errorMessage
                return@setOnClickListener
            }

            if (email.isEmpty()) {
                binding.textEmail.error = errorMessage
                return@setOnClickListener
            }

            if (region == "Others") {
                //Show a Toast message, Unsupported Region
                Toast.makeText(this, "Unsupported Region", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            //get the discount message
            val discount_message = getString(R.string.discount_code, userName)
            binding.textMessage.text = discount_message

            //get a random code
            var discountText = UUID.randomUUID().toString()

            //extract the first 8 characters in capital form
            discountText = discountText.substring(0,8).uppercase(Locale.getDefault())

            //update the text again
            binding.textDiscount.text = discountText
        }

    }//oncreate

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_clear -> {
                clearAll()
            }

            R.id.menu_about -> {
                showDialog()
            }

            else -> {

            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        const val DISCOUNT_MESSAGE = "discount_message"
        const val DISCOUNT_CODE = "discount_code"
    }
    private fun clearAll() {
        binding.textUsername.setText("")
        binding.textEmail.setText("")
        binding.textMessage.text=""
        binding.textDiscount.text=""
    }

    override fun onPause() {
        super.onPause()

        viewModel.message = binding.textMessage.text.toString()
        viewModel.discountCode = binding.textDiscount.text.toString()
    }

    override fun onResume() {
        super.onResume()
        //recover the data for any configuration changes
        binding.textMessage.text = viewModel.message
        binding.textDiscount.text = viewModel.discountCode
    }

    private fun showDialog() {
        val dialog = AboutDialog()
        dialog.show(supportFragmentManager, "123")
    }
    /*
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        //try to get the text_message and text_discount
        val message = binding.textMessage.text.toString()
        val discount = binding.textDiscount.text.toString()

        //save the data to the bundle
        outState.putString(DISCOUNT_MESSAGE, message)
        outState.putString(DISCOUNT_CODE, discount)

    }
     */
}//MainActivity