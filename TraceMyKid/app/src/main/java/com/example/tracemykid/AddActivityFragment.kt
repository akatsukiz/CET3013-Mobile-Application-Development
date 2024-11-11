package com.example.tracemykid

import android.Manifest
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.ImageDecoder
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.Debug.getLocation
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.example.tracemykid.data.KidActivityViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddActivityFragment : Fragment() {
    // For Camera
    var startForResult: ActivityResultLauncher<Intent>? = null
    private val TAG = "CameraXMain"
    lateinit var bitmap:Bitmap
    var isImage = false

    // For Location
    var mLastLocation: Location? = null
    var mFusedLocationClient: FusedLocationProviderClient? = null
    var result = "" //result returned from the thread

    // For Dataroom
    val coroutineScope = CoroutineScope(Dispatchers.Main)
    private lateinit var viewModel: KidActivityViewModel
    private lateinit var selectedDate: String
    private lateinit var selectedTime: String

    companion object {
        private const val REQUEST_LOCATION_PERMISSION = 1
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
                    getLocation()
                    Log.e("Location","Success")
                } else {
                    Log.e("Location", "Error")
                    Toast.makeText(
                        requireContext(),
                        R.string.location_permission_denied,
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }
    fun getLocation( textLocation: EditText) {
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
                        getString(R.string.location_text,
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
                    textLocation.setText(R.string.no_location)
                }
            }


        }
    }
    @SuppressLint("UseSwitchCompatOrMaterialCode") // As we are using compat instead of default widget
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.e("Start of createview: ", "Checked")
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_activity, container, false)

        // Location
        // Initialize the FusedLocationClient.
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(
            requireContext()
        )

        // Switch OnChecked Listener for user to choose whether to include current location or no
        val switchGetLocation: Switch = view.findViewById<Switch>(R.id.switch_location)
        val textLocation: EditText = view.findViewById<EditText>(R.id.text_location)
        switchGetLocation.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                getLocation(textLocation)
            }
            else {
                textLocation.setText(R.string.no_location)
            }
        }

        // Spinner for Activity Category
        val items= resources.getStringArray(R.array.category)
        val spinnerAdapter= object : ArrayAdapter<String>(requireContext(),android.R.layout.simple_spinner_item, items) {

            override fun isEnabled(position: Int): Boolean {
                // Disable the first item from Spinner
                // First item will be used for hint
                return position != 0
            }

            override fun getDropDownView(
                position: Int,
                convertView: View?,
                parent: ViewGroup
            ): View {
                val view: TextView = super.getDropDownView(position, convertView, parent) as TextView
                //set the color of first item in the drop down list to gray as use it as a prompt
                if(position == 0) {
                    view.setTextColor(Color.GRAY)
                } else {
                }
                return view
            }


            }
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        val categorySpinner: Spinner = view.findViewById<Spinner>(R.id.spinner_category)
        categorySpinner.adapter = spinnerAdapter

        // Camera
        val photo: ImageView = view.findViewById<ImageView>(R.id.photo)
        val buttonCapture: Button = view.findViewById<Button>(R.id.button_add_photo)
        startForResult = registerForActivityResult<Intent, ActivityResult>(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult ->

            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                val intent = result.data
                val file_uri = intent!!.getStringExtra("URL")
                Log.d(TAG, file_uri!!)
                try {
                    val imageUri = Uri.parse(file_uri)

                    val source = ImageDecoder.createSource(requireContext().contentResolver, imageUri)
                    bitmap = ImageDecoder.decodeBitmap(source)
                    photo.setImageBitmap(bitmap)
                    isImage=true

                    Log.d(TAG, imageUri.toString())
                } catch (e: Exception) {
                    Log.d(
                        TAG,"Uri Error:" + e.localizedMessage
                    )
                }
            }
        }
        // If user choose to include photo
        buttonCapture.setOnClickListener {
            val intent = Intent(requireContext(), CameraActivity::class.java)
            startForResult!!.launch(intent)
        }

        // For navigation bar
        val navigationBar = view.findViewById<BottomNavigationView>(R.id.navigation_below1)
        navigationBar.selectedItemId = R.id.menu_add
        navigationBar.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.menu_add -> {
                    true
                }

                R.id.menu_search -> {
                    view.findNavController().navigate(R.id.action_addActivityFragment_to_searchActivityFragment)
                    true
                }

                R.id.menu_setting -> {
                    view.findNavController().navigate(R.id.action_addActivityFragment_to_settingFragment)
                    true
                }

                else -> {
                    false
                }
            }
        }

        // Validation for user input (Required Field)
        val activityNameEditText = view.findViewById<EditText>(R.id.inputtext_activity_name)
        val dateText = view.findViewById<EditText>(R.id.text_date)
        val timeText = view.findViewById<EditText>(R.id.text_time)
        val reporterNameEditText = view.findViewById<EditText>(R.id.inputtext_reporter_name)
        val submitButton = view.findViewById<Button>(R.id.button_submit)

        // Boolean Variable
        var validActivityName = false // Activity Name
        var validCategory = false // Activity Category
        var validDate = false // Date
        var validTime = false // Time
        var validReporterName = false // Reporter Name

        // Check whether the activity name is not empty and only contains alphabets
        activityNameEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val activityName = activityNameEditText.text.toString().trim()
                // Regex to allow user key in 1 words or 2 words seperated by space
                // Eg: Ching Han, ChingHan
                val regex = Regex("^[a-zA-Z]+(\\s[a-zA-Z]+)?\$")

                validActivityName = !(activityName.isBlank() || !regex.matches(activityName))
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Do nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Do nothing
            }
        })

        // Check whether the activity category is selected
        categorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val activityCategory = categorySpinner.selectedItem.toString()

                validCategory = activityCategory != "Activity Category"
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }

        // Check if date is selected
        dateText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                validDate = dateText.text.toString().isNotBlank()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Do nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Do nothing
            }
        })

        // Check if time is selected
        timeText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                validTime = timeText.text.toString().isNotBlank()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Do nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Do nothing
            }
        })

        // Check whether the name of the reporter is not empty and contains only alphabets
        reporterNameEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val reporterName = reporterNameEditText.text.toString().trim()
                // Regex to allow user key in 1 words or 2 words seperated by space
                // Eg: Ching Han, ChingHan
                val regex = Regex("^[a-zA-Z]+(\\s[a-zA-Z]+)?\$")

                validReporterName = !(reporterName.isBlank() || !regex.matches(reporterName))
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Do nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Do nothing
            }
        })
        // OnClickListener for submit button
        submitButton.setOnClickListener {
            // Check whether required input is valid or not
            Log.e("Start of submit button: ", "Checked")
            if (validActivityName && validCategory && validDate && validTime && validReporterName) {
                val ActivityName = activityNameEditText.text.toString()
                val ActivityCategory = categorySpinner.selectedItem.toString()
                val DateAndTime = dateText.text.toString() + " " + timeText.text.toString()
                val Location = textLocation?.text.toString() ?: "No Data"
                var photo=""
                if(isImage)
                photo = convertBitmap(bitmap).toString()
                else
                    photo=""
                val notes = ""
                val reporterName = reporterNameEditText.text.toString()

                // Navigate to let user confirm details before inserting the data to database
                val direction = AddActivityFragmentDirections.actionAddActivityFragmentToDetailsFragment(ActivityName, ActivityCategory
                ,DateAndTime, Location, photo, notes, reporterName,isImage)
                view.findNavController()
                    .navigate(direction)
            } else {
                // Display error messages
                if (!validActivityName) {
                    activityNameEditText.error = getString(R.string.activity_name_error)
                }
                if (!validReporterName) {
                    reporterNameEditText.error = getString(R.string.reporter_name_error)
                }
                if (!validCategory && (!validDate || !validTime)) {
                    if(!validDate) {
                        // As it is EditText (To use the line make it to seem better), and not enabled (as editable cannot be used),
                        // hence just having icon to indicate error, as the error could not be touch to see details
                        dateText.error = ""
                    }
                    if(!validTime) {
                        // As it is EditText (To use the line make it to seem better), and not enabled (as editable cannot be used),
                        // hence just having icon to indicate error, as the error could not be touch to see details
                        timeText.error = ""
                    }
                    Toast.makeText(requireContext(), R.string.date_and_time_error2, Toast.LENGTH_LONG).show()
                }
                else if (!validDate || !validTime) {
                    if(!validDate) {
                        // As it is EditText (To use the line make it to seem better), and not enabled (as editable cannot be used),
                        // hence just having icon to indicate error, as the error could not be touch to see details
                        dateText.error = ""
                    }
                    if (!validTime) {
                        // As it is EditText (To use the line make it to seem better), and not enabled (as editable cannot be used),
                        // hence just having icon to indicate error, as the error could not be touch to see details
                        timeText.error = ""
                    }
                    Toast.makeText(requireContext(), R.string.date_and_time_error, Toast.LENGTH_LONG).show()
                }
                else if (!validCategory) {
                    Toast.makeText(requireContext(), R.string.category_error, Toast.LENGTH_LONG).show()
                }
            }
        }
        return view
    }
    fun convertBitmap(bitmap: Bitmap): String? {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val b = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize ViewModel
        viewModel = ViewModelProvider(this).get(KidActivityViewModel::class.java)

        // Time and Date Picker
        val btnDatePicker = view.findViewById<Button>(R.id.button_date)
        val btnTimePicker = view.findViewById<Button>(R.id.button_time)
        val txtDate = view.findViewById<EditText>(R.id.text_date)
        val txtTime = view.findViewById<EditText>(R.id.text_time)

        btnDatePicker.setOnClickListener {
            showDatePicker(txtDate)
        }

        btnTimePicker.setOnClickListener {
            showTimePicker(txtTime)
        }
    }
    // For user to pick date
    private fun showDatePicker(txtDate: EditText) {
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
    private fun showTimePicker(txtTime: EditText) {
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
}
