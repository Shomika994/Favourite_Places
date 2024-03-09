package com.example.favouriteplaces.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.android.volley.BuildConfig
import com.example.com.example.favouriteplaces.R
import com.example.favouriteplaces.FavouritePlacesManager
import com.example.favouriteplaces.models.FavouritePlaceModel
import com.google.android.gms.maps.MapsInitializer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.Serializable
import kotlin.math.roundToInt


class MainActivity : AppCompatActivity() {


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            FavouritePlacesManager.fetchAllSavedPlaces(applicationContext)
        }

        MapsInitializer.initialize(this@MainActivity)
        setContent {
            MainActivityLayout()
        }
    }


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @Composable
    fun FavouritePlacesList() {

        val favouritePlaceState by FavouritePlacesManager.favouritePlaces.collectAsState()

        LazyColumn(
            contentPadding = PaddingValues(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        )
        {
            items(favouritePlaceState, key = null) { place ->
                FavouritePlaceItem(place)
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("SuspiciousIndentation")
    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun FavouritePlaceItem(place: FavouritePlaceModel) {

        val primaryColor = colorResource(id = R.color.colorPrimary)
        val redColor = colorResource(id = R.color.red)
        val swipeableState = rememberSwipeableState(0)
        val swipe = remember { mutableStateOf(false) }
        val sizePx = with(LocalDensity.current) { 350.dp.toPx() }
        val anchors = mapOf(0f to 0, sizePx to 1)
        val coroutineScope = rememberCoroutineScope()


        Card(
            modifier = Modifier
                .padding(10.dp)
                .size(width = 350.dp, height = 150.dp)
                .swipeable(
                    state = swipeableState,
                    anchors = anchors,
                    orientation = Orientation.Horizontal
                ),
            elevation = 10.dp
        ) {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .offset {
                        IntOffset(swipeableState.offset.value.roundToInt(), 0)
                    }
            ) {

                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.Start
                ) {

                    AsyncImage(
                        modifier = Modifier
                            .size(width = 100.dp, height = 100.dp),
                        model = place.image,
                        contentDescription = "Saved image",
                        contentScale = ContentScale.FillBounds
                    )

                    Spacer(modifier = Modifier.width(30.dp))

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .align(Alignment.CenterVertically)
                    ) {

                        Text(
                            text = place.title,
                            fontWeight = FontWeight(1),
                            fontSize = 30.sp,
                            style = MaterialTheme.typography.h1,
                            textAlign = TextAlign.Center,
                            maxLines = 2,
                            overflow = TextOverflow.Clip
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = place.description,
                            fontWeight = FontWeight(1),
                            fontSize = 20.sp,
                            style = MaterialTheme.typography.h6,
                            textAlign = TextAlign.End,
                            maxLines = 2,
                            overflow = TextOverflow.Clip
                        )
                    }

                }
            }

            LaunchedEffect(swipeableState.offset.value) {
                swipe.value = swipeableState.offset.value > sizePx / 2
            }

            AnimatedVisibility(
                visible = swipe.value,
                enter = fadeIn(),
                exit = fadeOut()
            ) {

                Row(
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Button(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f),
                        colors = ButtonDefaults.buttonColors(primaryColor),
                        onClick = {
                            val intent = Intent(this@MainActivity, AddPlaceActivity::class.java)
                            intent.putExtra("favPlace", place)
                            startActivity(intent)
                            lifecycleScope.launch {
                                withContext(coroutineScope.coroutineContext) {
                                    swipeableState.animateTo(0)
                                }
                            }
                        }
                    ) {
                        Text(text = "EDIT", color = Color.White, fontSize = 15.sp)
                    }

                    Button(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f),
                        colors = ButtonDefaults.buttonColors(primaryColor),
                        onClick = {
                            val intent = Intent(this@MainActivity, ShowSavedPlace::class.java)
                            intent.putExtra("savPlace", place)
                            startActivity(intent)
                            lifecycleScope.launch {
                                withContext(coroutineScope.coroutineContext) {
                                    swipeableState.animateTo(0)
                                }
                            }
                        }
                    ) {
                        Text(text = "VIEW", color = Color.White, fontSize = 15.sp)

                    }

                    Button(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f),
                        colors = ButtonDefaults.buttonColors(redColor),
                        onClick = {
                            lifecycleScope.launch {
                                withContext(coroutineScope.coroutineContext) {
                                    FavouritePlacesManager.deleteFavouritePlace(favouritePlaceId = place.id)
                                    swipeableState.animateTo(0)
                                }
                            }
                            Toast.makeText(
                                applicationContext,
                                "Favourite place successfully deleted.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    ) {
                        Text(text = "DELETE", color = Color.White, fontSize = 15.sp)
                    }
                }
            }
        }


    }


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @Composable
    fun MainActivityLayout() {

        val primaryColor = colorResource(id = R.color.colorPrimary)
        val colorWhite = colorResource(id = R.color.white)

        Scaffold(topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Favourite Places",
                        color = colorWhite,
                        fontWeight = FontWeight.Bold
                    )
                }, backgroundColor = primaryColor, elevation = 4.dp
            )
        }, floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    val intent = Intent(applicationContext, AddPlaceActivity::class.java)
                    startActivity(intent)
                }, modifier = Modifier.padding(8.dp)

            ) {
                Icon(
                    imageVector = Icons.Default.Add, contentDescription = null
                )
            }
        }, content = { innerPadding ->
            Surface(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .background(primaryColor)
            ) {
                FavouritePlacesList()
            }
        })
    }


}



