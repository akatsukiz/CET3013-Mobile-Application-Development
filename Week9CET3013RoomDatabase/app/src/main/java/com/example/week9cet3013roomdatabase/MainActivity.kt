package com.example.week9cet3013roomdatabase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.week9cet3013roomdatabase.data.Word
import com.example.week9cet3013roomdatabase.data.WordViewModel
import com.example.week9cet3013roomdatabase.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var binding: ActivityMainBinding? = null
    private lateinit var mWordViewModel: WordViewModel
    private var adapter:WordListAdapter = WordListAdapter(this)
    lateinit  var startForResult: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        mWordViewModel = ViewModelProvider(this).get(WordViewModel::class.java)

        mWordViewModel.getAllWords()?.observe(this) {words->

            words.let {
                adapter.setWords(it)
            }
        }

        binding?.recyclerview?.setAdapter(adapter)
        binding?.recyclerview?.setLayoutManager(LinearLayoutManager(this))

        startForResult = registerForActivityResult<Intent, ActivityResult>(
            ActivityResultContracts.StartActivityForResult()) {result:ActivityResult ->

            if (result.resultCode == RESULT_OK) {
                val data = result.data
                val newWord = data!!.getStringExtra(NewWordActivity.EXTRA_REPLY)
                Log.d("newword", newWord!!)
                val word = Word(newWord)
                mWordViewModel.insert(word)
            } else {
                Toast.makeText(
                    applicationContext,
                    R.string.empty_not_saved,
                    Toast.LENGTH_LONG
                ).show()
            }
        }

    binding?.fabAdd?.setOnClickListener {
            val intent = Intent(this@MainActivity, NewWordActivity::class.java)

            startForResult.launch(intent)
        }


    }


}
