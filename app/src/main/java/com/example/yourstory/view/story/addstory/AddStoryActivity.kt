package com.example.yourstory.view.story.addstory

import android.Manifest
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.yourstory.R
import com.example.yourstory.databinding.StoryAddActivityBinding
import com.example.yourstory.model.repository.Repository
import com.example.yourstory.model.utils.SessionManager
import com.example.yourstory.view.story.StoryActivity
import com.example.yourstory.view.story.camera.CameraActivity
import com.example.yourstory.view.story.camera.utils.reduceFileImage
import com.example.yourstory.view.story.camera.utils.rotateFile
import com.example.yourstory.view.story.camera.utils.uriToFile
import com.example.yourstory.viewmodel.story.addstory.AddStoryViewModel
import com.example.yourstory.viewmodel.story.addstory.AddStoryViewModelFactory
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class AddStoryActivity : AppCompatActivity() {

    private lateinit var binding: StoryAddActivityBinding
    private lateinit var ViewModel: AddStoryViewModel
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private var latitude: Float? = null
    private var longitude: Float? = null


    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg = result.data?.data as Uri
            selectedImg.let { uri ->
                val myFile = uriToFile(uri, this@AddStoryActivity)
//                rotateFile(myFile, true)
                ViewModel.setFileImage(myFile)
                binding.ivPreview.setImageBitmap(BitmapFactory.decodeFile(ViewModel._fileImage?.path))
            }
        }
    }

    companion object {
        const val CAMERA_X_RESULT = 200
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = StoryAddActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Add Story"
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        val repository = Repository()
        val sessionManager = SessionManager(this)
        val viewModelFactory = AddStoryViewModelFactory(repository, sessionManager)
        ViewModel = ViewModelProvider(this, viewModelFactory)[AddStoryViewModel::class.java]

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
            )
        }

        binding.OpenCameraButton.setOnClickListener {
            takePhoto()
        }

        binding.OpenGalleryButton.setOnClickListener {
            startGallery()
        }

        binding.btUpload.setOnClickListener {
            binding.tvStatus.text = "Uploading..."
            binding.tvStatus.setTextColor(
                ContextCompat.getColor(
                    this, android.R.color.holo_green_dark
                )
            )
            binding.tvStatus.visibility = android.view.View.VISIBLE
            uploadImage(latitude, longitude)
        }

        binding.enableLocation.setOnClickListener {
            if (binding.enableLocation.isChecked) {
                getCurrentLocation()
                Toast.makeText(this, "Location enabled", Toast.LENGTH_SHORT).show()
                Handler(Looper.getMainLooper()).postDelayed({
                    if (latitude == null || longitude == null) {
                        Toast.makeText(this, "Location not found", Toast.LENGTH_SHORT).show()
                        binding.enableLocation.isChecked = false
                    } else {
                        Toast.makeText(
                            this, "Location found: $latitude, $longitude", Toast.LENGTH_SHORT
                        ).show()
                    }
                }, 2000)
            }
        }

        binding.ivPreview.setImageResource(R.drawable.border_line_fill)

        ViewModel._message.observe(this) {
            if (it.error == false) {
                binding.tvStatus.text = it.message
                binding.tvStatus.setTextColor(
                    ContextCompat.getColor(
                        this, android.R.color.holo_green_dark
                    )
                )
                binding.tvStatus.visibility = android.view.View.VISIBLE
                Handler(Looper.getMainLooper()).postDelayed({
                    // Restart StoryActivity
                    val intent = Intent(this, StoryActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    finish() // Close the current AddStoryActivity
                }, 2000)
            } else {
                binding.tvStatus.text = it.message
                binding.tvStatus.setTextColor(
                    ContextCompat.getColor(
                        this, android.R.color.holo_red_dark
                    )
                )
                binding.tvStatus.visibility = android.view.View.VISIBLE
            }
        }
    }

    private fun getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1
            )
            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                latitude = location.latitude.toFloat()
                longitude = location.longitude.toFloat()

//                uploadImage(latitudeFloat, longitudeFloat)
            } else {
                Toast.makeText(this, "Unable to get current location.", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun startGallery() {
        val intent = Intent()
        intent.action = ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private fun takePhoto() {
        val intent = Intent(this, CameraActivity::class.java)
        launcherIntentCamera.launch(intent)
    }

    private fun uploadImage(latitude: Float?, longitude: Float?) {
        if (ViewModel.getFileImage() != null) {

            val file = reduceFileImage(ViewModel.getFileImage() as File)
            val desc = binding.etDescImage.text.toString()

            if (desc.isEmpty()) {
                Toast.makeText(
                    this, "Silakan masukkan deskripsi terlebih dahulu.", Toast.LENGTH_SHORT
                ).show()
                binding.tvStatus.visibility = android.view.View.INVISIBLE
                return
            }

            val requestImageFile = file.asRequestBody("image/jpeg".toMediaType())
            val description = desc.toRequestBody("text/plain".toMediaType())

            val imageMultipart: MultipartBody.Part =
                MultipartBody.Part.createFormData("photo", file.name, requestImageFile)


            ViewModel.uploadImagePost(imageMultipart, description, latitude, longitude)

        } else {
            Toast.makeText(
                this, "Silakan masukkan berkas gambar terlebih dahulu.", Toast.LENGTH_SHORT
            ).show()
            binding.tvStatus.visibility = android.view.View.INVISIBLE
        }
    }


    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_X_RESULT) {
            val myFile = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.data?.getSerializableExtra("picture", File::class.java)
            } else {
                @Suppress("DEPRECATION") it.data?.getSerializableExtra("picture")
            } as? File
            val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean
            myFile?.let { file ->
                rotateFile(file, isBackCamera)
                ViewModel.setFileImage(file)
                binding.ivPreview.setImageBitmap(BitmapFactory.decodeFile(ViewModel._fileImage?.path))
            }
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this, "Tidak mendapatkan permission.", Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

}