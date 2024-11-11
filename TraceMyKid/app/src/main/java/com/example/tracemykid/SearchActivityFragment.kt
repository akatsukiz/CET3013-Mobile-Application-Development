package com.example.tracemykid

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tracemykid.data.KidActivityViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class SearchActivityFragment :  Fragment(), ActivityListener {
    private lateinit var viewModel: KidActivityViewModel
    private var adapter:CardListAdapter = CardListAdapter(context,this)


    var mOnSearchSelectedListener:OnSearchSelectedListener?=null

    public interface OnSearchSelectedListener{
        fun onItemSelected(id: Int,
                           activityName: String,
                           activityCategory: String,
                           activityDate: String,
                           photo: String,
                           notes: String,
                           reporterName: String,
                           gotImage: Boolean,
                           location: String)
    }

    fun setOnSearchSelectedListener(listener: OnSearchSelectedListener?){
        mOnSearchSelectedListener=listener
    }
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_search_activity, container, false)


        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        val searchView = view.findViewById<SearchView>(R.id.search_activity)
        // Retrieve all the activities from the database
        viewModel = ViewModelProvider(this).get(KidActivityViewModel::class.java)
        viewModel.getAllActivities()?.observe(viewLifecycleOwner){activities ->


            activities.let{
                adapter.setActivities(it)
            }

        }
        recyclerView.adapter=adapter
        recyclerView.layoutManager=LinearLayoutManager(requireContext())

        searchView.setOnQueryTextListener(object:SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String): Boolean {
                viewModel.searchActivity(query)?.observe(viewLifecycleOwner){activities->
                    activities.let{
                        adapter.setActivities(it)
                    }
                }
                recyclerView.adapter=adapter
                recyclerView.layoutManager=LinearLayoutManager(requireContext())
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                viewModel.searchActivity(newText)?.observe(viewLifecycleOwner){activities->
                    activities.let{
                        adapter.setActivities(it)
                    }
                }
                recyclerView.adapter=adapter
                recyclerView.layoutManager=LinearLayoutManager(requireContext())
                return false
            }
        })

        // For navigation bar
        val navigationBar = view.findViewById<BottomNavigationView>(R.id.navigation_below2)
        navigationBar.selectedItemId = R.id.menu_search
        navigationBar.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.menu_add -> {
                    view.findNavController().navigate(R.id.action_searchActivityFragment_to_addActivityFragment)
                    true
                }

                R.id.menu_search -> {
                    true
                }

                R.id.menu_setting -> {
                    view.findNavController().navigate(R.id.action_searchActivityFragment_to_settingFragment)
                    true
                }

                else -> {
                    false
                }
            }
        }

        return view
    }


    override fun onActivityClick(
        id : Int,
        activityName: String,
        activityCategory: String,
        activityDate: String,
        photo: String,
        notes: String,
        reporterName: String,
        gotImage: Boolean,
        location: String
    ) {
        Log.e("onActivityClick", "Checked")


        val direction = SearchActivityFragmentDirections.actionSearchActivityFragmentToFragmentActivity(activityName,activityCategory,activityDate,
            photo.toString(),notes.toString(),reporterName,gotImage, location, id)
        findNavController().navigate(direction)

    }
}

