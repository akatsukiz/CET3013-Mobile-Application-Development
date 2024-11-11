package com.example.week9cet3013roomdatabase

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.week9cet3013roomdatabase.databinding.ActivityNewWordBinding

class NewWordActivity : AppCompatActivity() {

    lateinit var binding: ActivityNewWordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNewWordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonSave.setOnClickListener {

            val replyIntent = Intent()
            if (binding.editWord.text.toString().isEmpty()) {
                setResult(RESULT_CANCELED, replyIntent)
            } else {
                val word = binding.editWord.text.toString()
                replyIntent.putExtra(NewWordActivity.EXTRA_REPLY, word)
                setResult(RESULT_OK, replyIntent)
            }
            finish()
        }
    }
    companion object {
        val EXTRA_REPLY = "com.example.android.roomwordssample.REPLY"
    }
}
