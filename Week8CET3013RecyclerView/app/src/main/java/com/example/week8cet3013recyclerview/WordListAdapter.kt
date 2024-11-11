package com.example.week8cet3013recyclerview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.LinkedList

 class WordListAdapter(val context: Context, val wordList: LinkedList<String>):
    RecyclerView.Adapter<WordListAdapter.WordViewHolder>() {

     private lateinit var mWordList: LinkedList<String>
     private lateinit var mInflater: LayoutInflater

     init {
         mInflater = LayoutInflater.from(context)
         mWordList = wordList //from the MainActivity (20 words)
     }

     override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
         val mItemView = mInflater!!.inflate(R.layout.wordlist_item,
             parent, false
         )

         //try to create each row in the recyclerview layout
         return WordViewHolder(mItemView, this)
     }

     override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
         val mCurrent = mWordList!![position]
         holder.wordItemView.text = mCurrent
     }

     override fun getItemCount(): Int {
         return mWordList!!.size
     }

     inner class WordViewHolder(itemView: View, adapter: WordListAdapter)
         : RecyclerView.ViewHolder(itemView), View.OnClickListener {

         val wordItemView = itemView.findViewById<TextView?>(R.id.word)
         val mAdapter = adapter

         init {
             itemView.setOnClickListener(this) // Register the click event for the viewholder
         }

         override fun onClick(v: View?) {
             val position = layoutPosition // Which row is selected

             val element = mWordList.get(position) // Get item from position

             mWordList.set(position, "Clicked $element") // Set something based on the selected item

             mAdapter.notifyDataSetChanged() // Refresh the recyclerview

         }

     }

 }
