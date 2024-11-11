package com.example.tracemykid

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.tracemykid.data.KidActivity


class CardListAdapter(val context: Context?, listener: ActivityListener) : RecyclerView.Adapter<CardListAdapter.CardViewHolder>() {
    //private var mInflater: LayoutInflater? = null
    private var mKidActivity
            : List<KidActivity>? = null
    private val mListener: ActivityListener=listener
    private var inflater: LayoutInflater? = null

    init {
        //mInflater = LayoutInflater.from(context)
    }

    //private ArrayList<Note> noteArrayList = new ArrayList<>();


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val mInflater = LayoutInflater.from(parent.context)

        val itemView = mInflater.inflate(R.layout.card_layout, parent, false)
        return CardViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        if (mKidActivity != null) {
            val current: KidActivity = mKidActivity!![position]
            if(current.activityPhoto!="")
            holder.imageItemView.setImageBitmap(convertBitmap(current.activityPhoto))

            holder.titleItemView.setText(current.activityName)
            holder.itemItemView.setText(current.reporterName)

            holder.cardView.setOnClickListener {
                Log.e("CardView clicked", "Checked")
                mListener.onActivityClick(current.id, current.activityName,current.activityCategory,current.activityDate,
                    current.activityPhoto.toString(),current.activityNotes.toString(),current.reporterName,current.gotImage, current.activityLocation.toString())
                notifyDataSetChanged()
            }


        } else {
            // Covers the case of data not being ready yet.
            holder.itemItemView.text = "No Word"
        }
    }

    fun setActivities(kidActivity:List<KidActivity>) {
        mKidActivity= kidActivity
        notifyDataSetChanged()
    }

    fun convertBitmap(encodedString: String?): Bitmap? {
        return try {
            val encodedByte = Base64.decode(encodedString, Base64.DEFAULT)
            val bitmap = BitmapFactory.decodeByteArray(encodedByte, 0, encodedByte.size)
            Log.e("Image error", "Got image")
            bitmap
        } catch (e: Exception) {
            e.message
            Log.e("Image error", "No image")
            null
        }
    }
    // getItemCount() is called many times, and when it is first called,
    // mWords has not been updated (means initially, it's null, and we can't return null).
    override fun getItemCount(): Int {
        return if (mKidActivity != null) mKidActivity!!.size else 0
    }

    inner class CardViewHolder (itemView: View) :  RecyclerView.ViewHolder(itemView) {
        val imageItemView: ImageView
        val titleItemView: TextView
        val itemItemView: TextView
        val cardView: CardView

        init {
            imageItemView = itemView.findViewById(R.id.itemImage)
            titleItemView = itemView.findViewById(R.id.itemTitle)
            itemItemView = itemView.findViewById(R.id.itemDetail)
            cardView = itemView.findViewById(R.id.card_view)

        }
    }
}
