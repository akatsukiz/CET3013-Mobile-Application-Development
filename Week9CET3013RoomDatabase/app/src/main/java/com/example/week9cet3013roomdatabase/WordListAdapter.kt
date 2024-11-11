package com.example.week9cet3013roomdatabase

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.week9cet3013roomdatabase.data.Word

class WordListAdapter(val context: Context?) : RecyclerView.Adapter<WordListAdapter.WordViewHolder>() {
    //private var mInflater: LayoutInflater? = null
    private var mWords // Cached copy of words
            : List<Word>? = null

    init {
        //mInflater = LayoutInflater.from(context)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        val mInflater = LayoutInflater.from(parent.context)

        val itemView = mInflater.inflate(R.layout.recyclerview_item, parent, false)
        return WordViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        if (mWords != null) {
            val current: Word = mWords!![position]
            holder.wordItemView.setText(current.mWord)
        } else {
            // Covers the case of data not being ready yet.
            holder.wordItemView.text = "No Word"
        }
    }

    fun setWords(words: List<Word>) {
        mWords = words
        notifyDataSetChanged()
    }

    // getItemCount() is called many times, and when it is first called,
    // mWords has not been updated (means initially, it's null, and we can't return null).
    override fun getItemCount(): Int {
        return if (mWords != null) mWords!!.size else 0
    }

    inner class WordViewHolder (itemView: View) :  RecyclerView.ViewHolder(itemView) {
        val wordItemView: TextView

        init {
            wordItemView = itemView.findViewById(R.id.textView)
        }
    }
}
