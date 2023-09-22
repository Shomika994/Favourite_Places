package com.example.favouriteplaces


import android.Manifest
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.lifecycleScope
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.shouldShowRationale
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.DatePickerColors
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class AddPlace : AppCompatActivity() {


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AddFavouritePlaceScreen()
        }


    }


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @Composable
    fun AddFavouritePlaceScreen() {

        val primaryColor = colorResource(id = R.color.colorPrimary)
        val colorWhite = colorResource(id = R.color.white)

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Add Places", color = colorWhite) },
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
                    }
                    // You can also set navigationIcon if you need a navigation icon on the toolbar.
                )
            },
            content = { innerPadding ->
                Surface(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                        .background(primaryColor)
                ) {
                    AddFavouritePlaceContent()
                }
            }
        )
    }


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @Preview
    @Composable
    fun MyPreview() {
        AddFavouritePlaceScreen()
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @Composable
    private fun AddFavouritePlaceContent() {
        val titleText = remember { mutableStateOf("") }
        val descriptionText = remember { mutableStateOf("") }
        val locationText = remember { mutableStateOf("") }
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
        var textClicked by remember {
            mutableStateOf(false)
        }


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
            MaterialDialog(dialogState = dateDialogState,
                buttons = {
                    positiveButton(text = "Ok") {
                        date = datePicked.toString()
                    }
                    negativeButton(text = "Cancel") {
                        dateDialogState.hide()
                    }
                }) {
                datepicker(
                    initialDate = LocalDate.now(),
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

                    }
                ) {
                    datePicked = it
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = locationText.value,
                onValueChange = { locationText.value = it },
                label = { Text("Location") },
                enabled = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Image(
                painter = painterResource(id = R.drawable.add_screen_image_placeholder),
                contentDescription = "Place Image",
                modifier = Modifier
                    .size(150.dp)
                    .clip(shape = RoundedCornerShape(8.dp))
                    .background(Color.White)
                    .align(Alignment.CenterHorizontally)
                    .border(2.dp, Color.Blue, shape = RoundedCornerShape(8.dp))
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Add Image",
                color = Color.Blue,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier.clickable {
                    textClicked = true

                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { },
                colors = ButtonDefaults.buttonColors(backgroundColor = primaryColor),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text("SAVE", color = Color.White)
            }
        }
        if (textClicked) {
            val context = LocalContext.current
            PermissionHandling(context = context)
            textClicked = false
        }

    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @OptIn(ExperimentalPermissionsApi::class)
    @Composable
    fun PermissionHandling(context: Context) {

        val permissionState =
            rememberMultiplePermissionsState(
                permissions =
                listOf(Manifest.permission.CAMERA, Manifest.permission.READ_MEDIA_IMAGES)
            )


        val lifecycleOwner = LocalLifecycleOwner.current


        DisposableEffect(key1 = lifecycleOwner,
            effect = {
                val observer = LifecycleEventObserver { _, event ->
                    if (event == Lifecycle.Event.ON_START) {

                        lifecycleScope.launch {

                            permissionState.launchMultiplePermissionRequest()

                            while (!permissionState.allPermissionsGranted &&
                                !permissionState.permissions.any { it.status.shouldShowRationale || !it.status.isGranted }
                            ) {
                                delay(1000)
                            }
                            permissionState.permissions.forEach { perm ->
                                when (perm.permission) {

                                    Manifest.permission.CAMERA -> {
                                        when {
                                            perm.status.isGranted -> {
                                                Toast.makeText(
                                                    context,
                                                    "Camera permission has been granted",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }

                                            perm.status.shouldShowRationale -> {
                                                Toast.makeText(
                                                    context,
                                                    "Camera permission is needed to upload images",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        }
                                    }

                                    Manifest.permission.READ_MEDIA_IMAGES -> {
                                        when {
                                            perm.status.isGranted -> {
                                                Toast.makeText(
                                                    context,
                                                    "Storage permission has been granted",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }

                                            perm.status.shouldShowRationale -> {
                                                Toast.makeText(
                                                    context,
                                                    "Storage permission is needed to upload images",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        }
                                    }


                                }
                            }

                        }

                    }
                }
                lifecycleOwner.lifecycle.addObserver(observer)
                onDispose {
                    lifecycleOwner.lifecycle.removeObserver(observer)
                }
            })


        if (permissionState.allPermissionsGranted) {
            AlertDialog(
                onDismissRequest = { },
                title = { Text(text = "Dialog") },
                text = { Text(text = "Please choose from the following:") },
                buttons = {
                    Column {
                        Text(text = "Choose from storage",
                            modifier = Modifier.clickable { })

                        Spacer(modifier = Modifier.height(5.dp))

                        Text(text = "Take a picture",
                            modifier = Modifier.clickable { })
                    }
                },
                shape = RectangleShape
            )
        }
    }
}










