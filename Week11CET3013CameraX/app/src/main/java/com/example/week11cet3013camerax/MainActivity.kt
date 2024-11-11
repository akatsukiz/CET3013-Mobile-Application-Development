package com.example.week11cet3013camerax

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.example.week11cet3013camerax.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    var binding: ActivityMainBinding? = null
    var startForResult: ActivityResultLauncher<Intent>? = null
    private val TAG = "CameraXMain"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding?.root
        setContentView(view)

        startForResult = registerForActivityResult<Intent, ActivityResult>(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult ->

            if (result.resultCode == RESULT_OK) {
                val intent = result.data
                val file_uri = intent!!.getStringExtra("URL")
                Log.d(TAG, file_uri!!)
                try {
                    val imageUri = Uri.parse(file_uri)

                    val source = ImageDecoder.createSource(this.contentResolver, imageUri)
                    val bitmap: Bitmap = ImageDecoder.decodeBitmap(source)
                    binding!!.imageView.setImageBitmap(bitmap)

                    Log.d(TAG, imageUri.toString())
                } catch (e: Exception) {
                    Log.d(
                        TAG,"Uri Error:" + e.localizedMessage
                    )
                }

            }
        }

        binding?.buttonCamera?.setOnClickListener {
            val intent = Intent(this, CameraActivity::class.java)
            startForResult!!.launch(intent)
        }

    }
}