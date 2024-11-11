package com.example.week3cet3013

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.example.week3cet3013.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding:ActivityMainBinding
    lateinit var startForResult: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //try to link to the layout file
        binding = ActivityMainBinding.inflate(layoutInflater)
        //use the binding to initialise the app layout
        setContentView(binding.root)

        startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            if(it.resultCode == RESULT_OK)
            {
                val intent = it.data

                val message = intent?.extras?.getString("RESULT")

                //update the result textview
                binding.textResult.text = message
            }
        }


        binding.buttonStart.setOnClickListener{
            val name = binding.textName.text.toString()

            //create the intent object
            val intent = Intent(this, QuestionActivity::class.java)

            //save the name to the intent object
            intent.putExtra("NAME",name)

            //jump to the question activity
            //startActivity(intent)
            startForResult.launch(intent)
        }
    }
}