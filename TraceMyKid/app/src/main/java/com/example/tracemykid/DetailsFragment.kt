package com.example.tracemykid

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.example.tracemykid.data.KidActivity
import com.example.tracemykid.data.KidActivityViewModel
import com.example.tracemykid.databinding.FragmentDetailsBinding

class DetailsFragment : Fragment() {

    var kidViewModel: KidActivityViewModel? = null

    lateinit var binding : FragmentDetailsBinding

    private val args: DetailsFragmentArgs by navArgs()

    private var activity_name = ""
    private var activity_category = ""
    private var date_and_time = ""
    private var location = ""
    private var photo = ""
    private var notes = ""
    private var reporter_name = ""
    private var gotImage=false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        Log.e("Details Fragment", "Checked")
        // Inflate the layout for this fragment
        binding = FragmentDetailsBinding.inflate(inflater, container, false)

        binding.buttonCancel.setOnClickListener {
            // Navigate to let user confirm details before inserting the data to database
            val direction = DetailsFragmentDirections.actionDetailsFragmentToAddActivityFragment()
            binding.root.findNavController()
                .navigate(direction)
        }

        activity_name = args.activityName
        activity_category = args.activityCategory
        date_and_time = args.dateAndTime
        location = args.location
        photo = args.photo
        notes = args.notes
        reporter_name = args.reporterName
        gotImage=args.gotImage


        binding.apply {
            textActivityNameDetails.text = activity_name
            textCategoryDetails.text = activity_category
            textDateAndTimeDetails.text = date_and_time
            textReporterNameDetails.text = reporter_name
            textLocationDetails.text = location
            if (notes.isNotEmpty()) {
                textNotesDetails.text = notes
            }
            else {
                textNotesDetails.text = "No Data"
            }
            if (photo.isNotEmpty()) {
                val bitmap = convertBitmap(photo)
                imagePhoto2.setImageBitmap(bitmap)
            }
        }

        kidViewModel = ViewModelProvider(this).get(KidActivityViewModel::class.java)
        binding.buttonConfirm.setOnClickListener {
            val kidActivity = KidActivity(
                0,
                activity_name,
                activity_category,
                date_and_time,
                location,
                photo,
                notes,
                reporter_name,
                gotImage
            )
            kidViewModel!!.insert(kidActivity)
            // Navigate to let user confirm details before inserting the data to database
            val direction = DetailsFragmentDirections.actionDetailsFragmentToAddActivityFragment()
            binding.root.findNavController()
                .navigate(direction)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity_name = args.activityName
        activity_category = args.activityCategory
        date_and_time = args.dateAndTime
        location = args.location
        photo = args.photo
        notes = args.notes
        reporter_name = args.reporterName


        binding.apply {
            textActivityNameDetails.text = activity_name
            textCategoryDetails.text = activity_category
            textDateAndTimeDetails.text = date_and_time
            textReporterNameDetails.text = reporter_name
            textLocationDetails.text = location
            if (notes.isNotEmpty()) {
                textNotesDetails.text = notes
            }
            else {
                textNotesDetails.text = "No Data"
            }
            if (photo.isNotEmpty()) {
                val bitmap = convertBitmap(photo)
                imagePhoto2.setImageBitmap(bitmap)
            }
        }
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
}