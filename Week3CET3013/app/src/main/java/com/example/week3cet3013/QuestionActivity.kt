package com.example.week3cet3013

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.week3cet3013.databinding.ActivityQuestionBinding

class QuestionActivity : AppCompatActivity() {
    lateinit var binding:ActivityQuestionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityQuestionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //retrieve back the name
        val bundle = intent.extras
        val name = bundle?.getString("NAME")

        //update the text header
        binding.textHeader.text=name+", "+getString(R.string.header)

        //apply the event for the true/false button
        binding.buttonTrue.setOnClickListener(){
            val result = getString(R.string.correct)
            val intent = Intent()

            //save the result
            intent.putExtra("RESULT",result)
            setResult(RESULT_OK,intent)
            super.finish()
        }

        binding.buttonFalse.setOnClickListener(){
            val result = getString(R.string.incorrect)
            val intent = Intent()

            //save the result
            intent.putExtra("RESULT",result)
            setResult(RESULT_OK,intent)
            super.finish()
        }
    }
}