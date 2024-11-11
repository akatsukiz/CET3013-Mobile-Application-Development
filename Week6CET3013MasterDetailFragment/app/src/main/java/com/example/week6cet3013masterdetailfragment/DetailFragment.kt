package com.example.week6cet3013masterdetailfragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import com.example.week6cet3013masterdetailfragment.databinding.FragmentDetailBinding

class DetailFragment : Fragment() {

    lateinit var binding: FragmentDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bundle = arguments
        if (bundle != null && bundle.containsKey(KEY_COUNTRY_NAME)) {
            showSelectedCountry(bundle.getString(KEY_COUNTRY_NAME))
        }
    }

    fun showSelectedCountry(countryName: String?) {
        val countryText = binding.textViewCountryName
        countryText.text = countryName
    }

    companion object {
        public var KEY_COUNTRY_NAME = "KEY_COUNTRY_NAME"
    }


}