package com.example.tracemykid

import android.Manifest
import android.R
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.Debug
import androidx.core.app.ActivityCompat
import androidx.core.view.drawToBitmap
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.example.tracemykid.data.KidActivity
import com.example.tracemykid.data.KidActivityRoomDatabase.Companion.coroutineScope
import com.example.tracemykid.data.KidActivityViewModel
import com.example.tracemykid.databinding.FragmentActivityBinding
import com.example.tracemykid.databinding.FragmentDetailsBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class FragmentActivity : Fragment() {

    lateinit var binding:FragmentActivityBinding

    private val args: FragmentActivityArgs by navArgs()

    var kidViewModel:KidActivityViewModel?=null

    // For Camera
    var startForResult: ActivityResultLauncher<Intent>? = null
    private val TAG = "CameraXMain"
    lateinit var bitmap:Bitmap
    var isImage = false

    // For Location
    var mLastLocation: Location? = null
    var mFusedLocationClient: FusedLocationProviderClient? = null
    var result = "" //result returned from the thread

    private lateinit var selectedDate: String
    private lateinit var selectedTime: String

    private var activity_name = ""
    private var activity_category = ""
    private var date_and_time = ""
    private var location = ""
    private var photoString = ""
    private var notes = ""
    private var reporter_name = ""
    private var gotImage=false

    companion object {
        private const val REQUEST_LOCATION_PERMISSION = 1
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreate(savedInstanceState)

        binding = FragmentActivityBinding.inflate(inflater, container, false)
        binding.spinnerCategory2.isEnabled=false
        activity_name = args.activityName
        activity_category = args.activityCategory
        date_and_time = args.dateAndTime
        location = args.location
        photoString = args.photo
        notes = args.notes
        reporter_name = args.reporterName
        gotImage=args.gotImage
        kidViewModel = ViewModelProvider(this)[KidActivityViewModel::class.java]

        binding.imagePhoto2.isEnabled = false

        var arrayCategory= arrayOf<String>("Activity Category","Indoor","Outdoor","Physical","Overnight","Educational","Other")
        val adapter: ArrayAdapter<CharSequence> =
            ArrayAdapter(requireContext(), R.layout.simple_spinner_item, arrayCategory)
        binding.spinnerCategory2.setSelection(adapter.getPosition(activity_category))
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(
            requireContext()
        )
        binding.apply {
            textActivityNameDetails.setText(activity_name)
            val date = args.dateAndTime.split(" ")[0]
            val time = args.dateAndTime.split(" ")[1]
            textDateDetailsInfo.text = date
            textTimeDetailsInfo.text = time
            textReporterNameDetails.setText(reporter_name)
            textLocationDetails.text = location
            if (notes.isNotEmpty()) {
                textNotesDetails.setText(notes)
            }
            else {
                textNotesDetails.setText("No Data")
            }
            if (photoString.isNotEmpty()) {
                val bitmap = convertBitmap(photoString)
                imagePhoto2.setImageBitmap(bitmap)
            }
        }

        // Camera
        val photo: ImageView = binding.imagePhoto2
        startForResult = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult ->
            Log.e("photofunction","Checked")
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                val intent = result.data
                val file_uri = intent!!.getStringExtra("URL")
                Log.d(TAG, file_uri!!)
                try {
                    val imageUri = Uri.parse(file_uri)

                    val source = ImageDecoder.createSource(requireContext().contentResolver, imageUri)
                    bitmap = ImageDecoder.decodeBitmap(source)
                    gotImage=true
                    Log.e("Photo get","Checked")
                    photoString=convertString(bitmap).toString()
                    photo.setImageBitmap(bitmap)


                    Log.d(TAG, imageUri.toString())
                } catch (e: Exception) {
                    Log.d(
                        TAG,"Uri Error:" + e.localizedMessage
                    )
                }
            }
        }
        // If user choose to include photo
        binding.imagePhoto2.setOnClickListener {
            val intent = Intent(requireContext(), CameraActivity::class.java)
            Log.e("photoonclick","Checked")
            startForResult!!.launch(intent)
        }

        binding.buttonBack.setOnClickListener {
            val direction = FragmentActivityDirections.actionFragmentActivityToSearchActivityFragment()
            binding.root.findNavController()
                .navigate(direction)

        }

        binding.buttonEdit.setOnClickListener {
            binding.buttonBack.isVisible = false
            binding.buttonEdit.isVisible = false
            binding.buttonOk.isVisible = true
            binding.buttonDelete.isVisible = false
            binding.buttonCancel.isVisible = true

            binding.textActivityNameDetails.isEnabled = true
            binding.spinnerCategory2.isEnabled = true
            binding.textNotesDetails.isEnabled = true
            binding.textReporterNameDetails.isEnabled = true
            binding.textDateDetailsInfo.isEnabled = true
            binding.textTimeDetailsInfo.isEnabled = true
            binding.switchLocation.isVisible = true

            binding.textLocationDetails.isEnabled=true
            binding.imagePhoto2.isEnabled = true

            binding.textDateDetailsInfo.setOnClickListener {
                showDatePicker(binding.textDateDetailsInfo)
            }
            binding.textTimeDetailsInfo.setOnClickListener {
                showTimePicker(binding.textTimeDetailsInfo)
            }
            binding.switchLocation.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    getLocation(binding.textLocationDetails)
                }
                else {
                    binding.textLocationDetails.setText(com.example.tracemykid.R.string.no_location)
                }
            }
        }

        binding.buttonDelete.setOnClickListener {
            val kidActivity = KidActivity(
                args.id,
                activity_name,
                activity_category,
                date_and_time,
                location,
                photoString,
                notes,
                reporter_name,
                gotImage
            )
            kidViewModel!!.delete(kidActivity)

            val direction = FragmentActivityDirections.actionFragmentActivityToSearchActivityFragment()
            binding.root.findNavController()
                .navigate(direction)
        }

        binding.buttonCancel.setOnClickListener {
            binding.buttonBack.isVisible = true
            binding.buttonEdit.isVisible = true
            binding.buttonOk.isVisible = false
            binding.buttonDelete.isVisible = true
            binding.buttonCancel.isVisible = false

            binding.textActivityNameDetails.isEnabled = false
            binding.spinnerCategory2.isEnabled = false
            binding.textNotesDetails.isEnabled = false
            binding.textReporterNameDetails.isEnabled = false
            binding.textDateDetailsInfo.isEnabled = false
            binding.textTimeDetailsInfo.isEnabled = false
            binding.textLocationDetails.isEnabled = false
            binding.imagePhoto2.isEnabled = false
            binding.switchLocation.isVisible = false
        }


        binding.buttonOk.setOnClickListener(){

            binding.buttonBack.isVisible = true
            binding.buttonEdit.isVisible = true
            binding.buttonOk.isVisible = false
            binding.buttonDelete.isVisible = true
            binding.buttonCancel.isVisible = false

            binding.textActivityNameDetails.isEnabled = false
            binding.spinnerCategory2.isEnabled = false
            binding.textNotesDetails.isEnabled = false
            binding.textReporterNameDetails.isEnabled = false
            binding.textDateDetailsInfo.isEnabled = false
            binding.textTimeDetailsInfo.isEnabled = false
            binding.textLocationDetails.isEnabled = false
            binding.imagePhoto2.isEnabled = false
            binding.switchLocation.isVisible = false


            activity_name = binding.textActivityNameDetails.text.toString()
            activity_category = binding.spinnerCategory2.selectedItem.toString()
            date_and_time = binding.textDateDetailsInfo.text.toString() + " " + binding.textTimeDetailsInfo.text.toString()
            location = binding.textLocationDetails.text.toString()

            notes = binding.textNotesDetails.text.toString()
            reporter_name = binding.textReporterNameDetails.text.toString()

            Log.e("Location value:",location)
            val kidActivity = KidActivity(
                args.id,
                activity_name,
                activity_category,
                date_and_time,
                location,
                photoString,
                notes,
                reporter_name,
                gotImage
            )
            kidViewModel!!.update(kidActivity)


        }
        return binding.root
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

    fun convertString(bitmap: Bitmap): String? {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val b = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
    }

    // For user to pick date
    private fun showDatePicker(txtDate: TextView) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, monthOfYear, dayOfMonth ->
                val selectedDateCalendar = Calendar.getInstance()
                selectedDateCalendar.set(year, monthOfYear, dayOfMonth)
                val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                selectedDate = dateFormat.format(selectedDateCalendar.time)
                txtDate.setText(selectedDate)
            },
            year, month, day
        )
        datePickerDialog.show()
    }

    // For user to pick time
    private fun showTimePicker(txtTime: TextView) {
        val calendar = Calendar.getInstance()
        val hourOfDay = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            requireContext(),
            { _, hourOfDay, minute ->
                val selectedTimeCalendar = Calendar.getInstance()
                selectedTimeCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                selectedTimeCalendar.set(Calendar.MINUTE, minute)
                val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
                selectedTime = timeFormat.format(selectedTimeCalendar.time)
                txtTime.setText(selectedTime)
            },
            hourOfDay, minute, false
        )
        timePickerDialog.show()
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        requireActivity().onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            REQUEST_LOCATION_PERMISSION ->             // If the permission is granted, get the location,
                // otherwise, show a Toast
                if (grantResults.size > 0
                    && grantResults[0] === PackageManager.PERMISSION_GRANTED
                ) {
                    Debug.getLocation()
                    Log.e("Location","Success")
                } else {
                    Log.e("Location", "Error")
                    Toast.makeText(
                        requireContext(),
                        com.example.tracemykid.R.string.location_permission_denied,
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }

    fun getLocation( textLocation: TextView) {
        Log.e("getLocation function","Checked")
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(), arrayOf<String>(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                REQUEST_LOCATION_PERMISSION
            )
        } else {
            mFusedLocationClient!!.lastLocation.addOnSuccessListener { location ->
                if (location != null) {

                    mLastLocation = location
                    val locationText =
                        getString(
                            com.example.tracemykid.R.string.location_text,
                            mLastLocation?.getLatitude(),
                            mLastLocation?.getLongitude(),
                            mLastLocation?.getTime())

                    // binding.textviewLocation.text = locationText
                    coroutineScope.launch(Dispatchers.Main) {
                        val addressObject = FetchAddressTask(requireContext(), location)

                        addressObject.fetchAddress()

                        result = addressObject.resultMessage
                        textLocation.setText(result)
                    }
                }
                else {
                    textLocation.setText(com.example.tracemykid.R.string.no_location)
                }
            }


        }
    }
}