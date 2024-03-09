package com.example.favouriteplaces.activities


import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import coil.compose.AsyncImage
import com.example.com.example.favouriteplaces.BuildConfig
import com.example.com.example.favouriteplaces.R
import com.example.favouriteplaces.FavouritePlacesManager
import com.example.favouriteplaces.models.FavouritePlaceModel
import com.example.favouriteplaces.utils.GetAddressFromLatLng
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.DatePickerColors
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.UUID


class AddPlaceActivity : AppCompatActivity() {

    private var addImageClicked by mutableStateOf(false)
    private var dialogsShown by mutableStateOf(false)
    private var storageAndCameraPermissions by mutableStateOf(false)
    private var permissionsDeniedBoolean by mutableStateOf(false)
    private var checkIfUserChangedPermissions by mutableStateOf(false)
    private var photoTakenByCameraBoolean by mutableStateOf(false)
    private var selectedImageUri by mutableStateOf<Uri?>(null)
    private var takenImageBitmap by mutableStateOf<Bitmap?>(null)
    private var location by mutableStateOf("")
    private var imageUri: Uri? = null
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    private lateinit var favouritePlace: FavouritePlaceModel
    private var placeToEdit: FavouritePlaceModel? = null
    private val locationPermissionGranted = mutableStateOf(false)
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val retFavPlace = intent.getSerializableExtra("favPlace") as? FavouritePlaceModel

        placeToEdit = retFavPlace

        val key = BuildConfig.GOOGLE_MAPS_API_KEY

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        if (!Places.isInitialized()) {
            Places.initialize(this@AddPlaceActivity, key)
        }

        setContent {
            AddFavouritePlaceScreen()
        }

    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @Preview
    @Composable
    fun MyPreview() {
        AddFavouritePlaceScreen()
    }


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @Composable
    fun AddFavouritePlaceScreen() {

        val primaryColor = colorResource(id = R.color.colorPrimary)
        val colorWhite = colorResource(id = R.color.white)

        Scaffold(topBar = {
            TopAppBar(title = { Text("Add Places", color = colorWhite) },
                backgroundColor = primaryColor,
                navigationIcon = {
                    Icon(imageVector = Icons.Default.ArrowBack,
                        contentDescription = null,
                        tint = colorWhite,
                        modifier = Modifier
                            .background(primaryColor)
                            .clickable {
                                val click = onBackPressedDispatcher
                                click.onBackPressed()
                            })
                })
        }, content = { innerPadding ->
            Surface(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .background(primaryColor)
            ) {
                AddFavouritePlaceContent()
            }
        })
    }


    @SuppressLint("SuspiciousIndentation")
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @Composable
    private fun AddFavouritePlaceContent() {

        val titleText = remember { mutableStateOf(placeToEdit?.title.orEmpty()) }
        val descriptionText = remember { mutableStateOf(placeToEdit?.description.orEmpty()) }
        val primaryColor = colorResource(id = R.color.colorPrimary)
        val colorBlack = colorResource(id = R.color.black)
        val colorWhite = colorResource(id = R.color.white)
        var datePicked by remember { mutableStateOf(LocalDate.now()) }
        val formattedDate by remember {
            derivedStateOf {
                DateTimeFormatter.ofPattern("dd MMM yyyy").format(datePicked)
            }
        }
        val dateDialogState = rememberMaterialDialogState()
        var date = formattedDate
        val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickVisualMedia(),
            onResult = { uri ->
                selectedImageUri = uri
                if (uri != null) {
                    val bitmap = ImagingHelper.loadBitmap(this@AddPlaceActivity, uri)
                    imageUri = saveImageToInternalStorage(bitmap)
                    Log.e("Saved Image : ", "Path :: $imageUri")
                }
            })


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,

            ) {
            OutlinedTextField(
                value = titleText.value,
                onValueChange = { titleText.value = it },
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = descriptionText.value,
                onValueChange = { descriptionText.value = it },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = formattedDate,
                onValueChange = { date },
                enabled = false,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        dateDialogState.show()
                    },
                textStyle = TextStyle(color = colorBlack)
            )
            MaterialDialog(dialogState = dateDialogState, buttons = {
                positiveButton(text = "Ok") {
                    date = datePicked.toString()
                }
                negativeButton(text = "Cancel") {
                    dateDialogState.hide()
                }
            }) {
                datepicker(initialDate = LocalDate.now(),
                    title = "PICK A DATE",
                    colors = object : DatePickerColors {
                        override val calendarHeaderTextColor: Color
                            get() = colorBlack
                        override val headerBackgroundColor: Color
                            get() = primaryColor
                        override val headerTextColor: Color
                            get() = colorWhite

                        @Composable
                        override fun dateBackgroundColor(active: Boolean): State<Color> {
                            return rememberUpdatedState(newValue = if (active) primaryColor else colorWhite)
                        }

                        @Composable
                        override fun dateTextColor(active: Boolean): State<Color> {
                            return rememberUpdatedState(newValue = if (active) colorWhite else colorBlack)
                        }

                    }) {
                    datePicked = it
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = location,
                onValueChange = { location = it },
                label = { Text("Location") },
                textStyle = TextStyle(color = Color.Black),
                readOnly = true,
                enabled = false,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        try {
                            val fields = listOf(
                                Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG,
                                Place.Field.ADDRESS
                            )
                            val intent =
                                Autocomplete
                                    .IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                                    .build(this@AddPlaceActivity)
                            startActivityForResult(intent, MAPS_REQUEST_KEY)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Set current location",
                maxLines = 1,
                style = MaterialTheme.typography.h6.copy(fontStyle = FontStyle.Normal),
                color = primaryColor,
                modifier = Modifier
                    .wrapContentWidth(align = Alignment.CenterHorizontally)
                    .clickable {

                        if (!isLocationEnabled()) {
                            Toast
                                .makeText(
                                    applicationContext,
                                    "Your location provider is turned off. Please turn it on.",
                                    Toast.LENGTH_SHORT
                                )
                                .show()
                            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                            startActivity(intent)
                        } else {
                            Dexter
                                .withActivity(this@AddPlaceActivity)
                                .withPermissions(
                                    Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_COARSE_LOCATION
                                )
                                .withListener(object : MultiplePermissionsListener {
                                    override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                                        if (report!!.areAllPermissionsGranted()) {
                                            requestNewLocationData()
                                        }
                                    }

                                    override fun onPermissionRationaleShouldBeShown(
                                        permissions: MutableList<PermissionRequest>?,
                                        token: PermissionToken?
                                    ) {
                                        showRationaleDialogForPermission()
                                    }
                                })
                                .onSameThread()
                                .check()
                        }

                    })

            Spacer(modifier = Modifier.height(16.dp))

            AsyncImage(
                model =
                placeToEdit?.image ?: run {
                    if (photoTakenByCameraBoolean) {
                        takenImageBitmap
                    } else selectedImageUri
                },
                contentDescription = "SelectedImage",
                modifier = Modifier
                    .size(200.dp)
                    .clip(shape = RoundedCornerShape(8.dp))
                    .background(Color.White)
                    .align(Alignment.CenterHorizontally)
                    .border(2.dp, Color.Blue, shape = RoundedCornerShape(8.dp)),
                contentScale = ContentScale.FillBounds,
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Add Image",
                color = Color.Blue,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier.clickable {

                    addImageClicked = true
                    dialogsShown = true

                    if (permissionsDeniedBoolean) {
                        showRationaleDialogForPermission()
                        permissionsDeniedBoolean = false
                    }

                    if (checkIfUserChangedPermissions) {
                        if (ContextCompat.checkSelfPermission(
                                applicationContext, Manifest.permission.CAMERA
                            ) == PackageManager.PERMISSION_DENIED || ContextCompat.checkSelfPermission(
                                applicationContext, Manifest.permission.READ_MEDIA_IMAGES
                            ) == PackageManager.PERMISSION_DENIED
                        ) {
                            permissionsDeniedBoolean = true
                            storageAndCameraPermissions = false
                        }
                        checkIfUserChangedPermissions = false
                    }

                    if (ContextCompat.checkSelfPermission(
                            applicationContext, Manifest.permission.CAMERA
                        ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                            applicationContext, Manifest.permission.READ_MEDIA_IMAGES
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        permissionsDeniedBoolean = false
                        storageAndCameraPermissions = true
                    }
                })

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    when {
                        titleText.value.isEmpty() -> {
                            Toast.makeText(
                                applicationContext, "Please enter title", Toast.LENGTH_SHORT
                            ).show()
                        }

                        descriptionText.value.isEmpty() -> {
                            Toast.makeText(
                                applicationContext, "Please enter description", Toast.LENGTH_SHORT
                            ).show()
                        }

                        location.isEmpty() -> {
                            Toast.makeText(
                                applicationContext, "Please enter location", Toast.LENGTH_SHORT
                            ).show()
                        }

                        imageUri == null && placeToEdit == null -> {
                            Toast.makeText(
                                applicationContext, "Please select an image", Toast.LENGTH_SHORT
                            ).show()
                        }

                        else -> {

                            favouritePlace = FavouritePlaceModel(
                                placeToEdit?.id ?: 0,
                                titleText.value,
                                imageUri.toString(),
                                descriptionText.value,
                                date,
                                Date().time,
                                location,
                                latitude,
                                longitude
                            )

                            lifecycleScope.launch {
                                placeToEdit?.let {
                                    FavouritePlacesManager.updateFavouritePlace(
                                        favouritePlace, applicationContext
                                    )
                                } ?: run {
                                    FavouritePlacesManager.addFavouritePlace(
                                        favouritePlace, applicationContext
                                    )
                                }
                            }

                            Toast.makeText(
                                applicationContext, "Favourite place added!", Toast.LENGTH_SHORT
                            ).show()
                            val intent = Intent(this@AddPlaceActivity, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = primaryColor),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text("SAVE", color = Color.White)
            }
        }

        if (addImageClicked) {
            getPermission(context = this)
            addImageClicked = false
        }
        if (dialogsShown) {
            getPermission(context = this)
            if (storageAndCameraPermissions) {
                SelectActionDialog(image = singlePhotoPickerLauncher)
            }
        }


    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {

        val mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 0
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationClient.requestLocationUpdates(
            mLocationRequest, locationCallback,
            Looper.myLooper()
        )
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val lastLocation: Location? = locationResult.lastLocation
            latitude = lastLocation!!.latitude
            longitude = lastLocation.longitude

            val addressTask =
                GetAddressFromLatLng(this@AddPlaceActivity, latitude, longitude)

            addressTask.setAddressListener(object :
                GetAddressFromLatLng.AddressListener {
                override fun onAddressFound(address: String?) {
                    Log.e("Address ::", "" + address)
                    if (address != null) {
                        location = address
                    }
                }

                override fun onError() {
                    Log.e("Get Address ::", "Something is wrong...")
                }
            })

            addressTask.getAddress()
        }
    }


    @SuppressLint("SuspiciousIndentation")
    @Composable
    private fun SelectActionDialog(image: ManagedActivityResultLauncher<PickVisualMediaRequest, Uri?>) {

        if (dialogsShown) {
            Dialog(onDismissRequest = { dialogsShown = false }, content = {
                Box(
                    modifier = Modifier.background(
                        shape = RoundedCornerShape(16.dp), color = Color.White
                    )
                ) {

                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.Center,

                        ) {
                        Text(
                            style = TextStyle(fontWeight = FontWeight.Bold),
                            text = "Select Action",
                            color = Color.Black,
                            modifier = Modifier.align(Alignment.Start),
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        Text(text = "Select photo from gallery", modifier = Modifier.clickable {
                            dialogsShown = false
                            image.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                            photoTakenByCameraBoolean = false

                        })
                        Spacer(modifier = Modifier.height(16.dp))

                        Text(text = "Capture photo from camera", modifier = Modifier.clickable {
                            dialogsShown = false
                            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                            startActivityForResult(intent, CAMERA)
                        })
                    }
                }
            }, properties = DialogProperties(
                dismissOnClickOutside = true, dismissOnBackPress = true
            )

            )

        }

    }


    @Deprecated("Deprecated in Java")
    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when {

            requestCode == CAMERA && resultCode == Activity.RESULT_OK -> {
                val thumbnail: Bitmap = data!!.extras!!.get("data") as Bitmap
                takenImageBitmap = thumbnail
                imageUri = saveImageToInternalStorage(thumbnail)
                Log.e("Saved Image : ", "Path :: $imageUri")
                photoTakenByCameraBoolean = true
            }

            requestCode == MAPS_REQUEST_KEY && resultCode == Activity.RESULT_OK -> {
                val place: Place = Autocomplete.getPlaceFromIntent(data!!)
                location = place.address ?: "Default Location"
                latitude = place.latLng?.latitude ?: 0.0
                longitude = place.latLng?.longitude ?: 0.0
            }

            requestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION && resultCode == Activity.RESULT_OK -> {
                locationPermissionGranted.value = true
            }

            resultCode == Activity.RESULT_CANCELED -> {
                Log.e("Cancelled", "Cancelled")
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun getPermission(context: Context) {

        val dexter = Dexter.withContext(context).withPermissions(
            Manifest.permission.CAMERA, Manifest.permission.READ_MEDIA_IMAGES
        ).withListener(object : MultiplePermissionsListener {

            override fun onPermissionsChecked(report: MultiplePermissionsReport?) {

                report.let {

                    if (report!!.areAllPermissionsGranted()) {
                        storageAndCameraPermissions = true
                    }
                }
            }

            override fun onPermissionRationaleShouldBeShown(
                permission: MutableList<PermissionRequest>?,
                token: PermissionToken?
            ) {
                showRationaleDialogForPermission()
            }
        }).onSameThread()
        dexter.check()
    }

    private fun showRationaleDialogForPermission() {

        AlertDialog.Builder(this)
            .setMessage("It Looks like you have turned off permissions required for this feature. It can be enabled under Application Settings")
            .setPositiveButton("GO TO SETTINGS") { _, _ ->
                try {
                    checkIfUserChangedPermissions = true
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", packageName, null)
                    intent.data = uri
                    startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    e.printStackTrace()
                }
            }.setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
                checkIfUserChangedPermissions = true
            }.setCancelable(true).setOnCancelListener {
                checkIfUserChangedPermissions = false
            }.show()
    }

    private fun saveImageToInternalStorage(bitmap: Bitmap): Uri {

        val wrapper = ContextWrapper(applicationContext)
        var file = wrapper.getDir(IMAGE_DIRECTORY, Context.MODE_PRIVATE)
        file = File(file, "${UUID.randomUUID()}.jpg")

        try {
            val stream: OutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.flush()
            stream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return Uri.parse(file.absolutePath)
    }


    companion object {
        private const val CAMERA = 1
        private const val IMAGE_DIRECTORY = "FavouritePlacesImages"
        private const val MAPS_REQUEST_KEY = 2
        private const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 3
    }

}




















