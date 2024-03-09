package com.example.favouriteplaces.activities

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.example.com.example.favouriteplaces.R
import com.example.favouriteplaces.models.FavouritePlaceModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

class MapActivity : ComponentActivity() {

    private var favouritePlaces: FavouritePlaceModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val favPlace = intent.getSerializableExtra("favPlace") as? FavouritePlaceModel

        if (favPlace != null) {
            favouritePlaces = favPlace
        }
        setContent {
            Screen()
        }

    }

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    @Composable
    private fun Screen() {

        val colorPrimary = colorResource(id = R.color.colorPrimary)

        Surface(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Scaffold(topBar = {
                TopAppBar(title = {
                    Text(
                        text = favouritePlaces!!.location,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                },
                    backgroundColor = colorPrimary,
                    navigationIcon = {
                        Icon(imageVector = Icons.Default.ArrowBack,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier
                                .clickable {
                                    val click = onBackPressedDispatcher
                                    click.onBackPressed()
                                })
                    })
            },
                content = {
                    MapScreen(favouritePlaces!!.latitude, favouritePlaces!!.longitude)
                })

        }

    }

    @Composable
    private fun MapScreen(lag: Double, log: Double) {

        val place = LatLng(lag, log)
        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(place, 10f)
        }

        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = MapProperties()
        ) {
            Marker(
                state = MarkerState(position = place),
                title = favouritePlaces!!.title,
                snippet = favouritePlaces!!.location
            )
        }

    }

    @Preview
    @Composable
    fun Preview() {
        Screen()
    }

}