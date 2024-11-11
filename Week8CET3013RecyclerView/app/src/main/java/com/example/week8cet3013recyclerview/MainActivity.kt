package com.example.week8cet3013recyclerview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.week8cet3013recyclerview.databinding.ActivityMainBinding
import java.util.LinkedList

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var mWordList = LinkedList<String>()
    private lateinit var mAdapter: WordListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        for (i in 0..19) {
            mWordList.addLast("Word $i")
        }

        mAdapter = WordListAdapter(this, mWordList)

        //set the layout for the recyclerview
        //If this step was skipped, compilation error will appear
        binding.recyclerview.setLayoutManager(
            LinearLayoutManager(this)
        )
        binding.recyclerview.itemAnimator = DefaultItemAnimator()

        // Add a neat dividing line between items in the list
        binding.recyclerview.addItemDecoration(
            DividerItemDecoration(this,
                LinearLayoutManager.VERTICAL)
        )

        // Set the adapter for the recyclerview
        binding.recyclerview.setAdapter(mAdapter)

        binding.fabList.setOnClickListener {
            val size = mWordList.size // Find current size of linked list

            mWordList.addLast("+Word $size")
            binding.recyclerview.adapter?.notifyDataSetChanged()
            binding.recyclerview.smoothScrollToPosition(size) // Move to the last position
        }
    }
}