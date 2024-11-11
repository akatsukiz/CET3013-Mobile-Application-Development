package com.example.week6cet3013masterdetailfragment

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.ListFragment
import android.R
import android.widget.AdapterView

class MasterFragment: ListFragment() {
    var countries = arrayOf("China", "France","Germany",
        "India", "Russia", "United Kingdom", "United States")

    var mOnMasterSelectedListener: OnMasterSelectedListener? = null

    public interface OnMasterSelectedListener {
        fun onItemSelected(countryName: String?)
    }

    fun setOnMasterSelectedListener(listener: OnMasterSelectedListener?) {
        mOnMasterSelectedListener = listener
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val countryAdapter: ListAdapter = ArrayAdapter(
            requireActivity(),R.layout.simple_list_item_1,
            countries)

        listAdapter = countryAdapter

        listView.choiceMode = ListView.CHOICE_MODE_SINGLE

        listView.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                if (mOnMasterSelectedListener != null) {

                    mOnMasterSelectedListener!!.onItemSelected((view as TextView).text.toString())
                }
            }
    }


}