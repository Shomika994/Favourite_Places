package com.example.favouriteplaces.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import coil.compose.AsyncImage
import com.example.com.example.favouriteplaces.R
import com.example.favouriteplaces.FavouritePlacesManager
import com.example.favouriteplaces.models.FavouritePlaceModel
import kotlinx.coroutines.launch
import kotlin.math.roundToInt


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            FavouritePlacesManager.fetchAllSavedPlaces(applicationContext)
        }
        setContent {
            MainActivityLayout()
        }


    }


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


    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun FavouritePlaceItem(place: FavouritePlaceModel) {

        val primaryColor = colorResource(id = R.color.colorPrimary)
        val redColor = colorResource(id = R.color.red)
        val swipeableState = rememberSwipeableState(0)
        var swipe by remember { mutableStateOf(false) }
        val sizePx = with(LocalDensity.current) { 350.dp.toPx() }
        val anchors = mapOf(0f to 0, sizePx to 1)



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
                    .offset { IntOffset(swipeableState.offset.value.roundToInt(), 0) }
            ) {

                Row(
                    modifier = Modifier.fillMaxSize(), horizontalArrangement = Arrangement.Start
                ) {

                    AsyncImage(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .size(width = 140.dp, height = 140.dp),
                        model = place.image,
                        contentDescription = "Saved image",
                        contentScale = ContentScale.Inside,

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
                            fontSize = 20.sp,
                            style = MaterialTheme.typography.h1,
                            textAlign = TextAlign.Center,
                            maxLines = 1,
                            overflow = TextOverflow.Clip
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = place.description,
                            fontWeight = FontWeight(1),
                            fontSize = 15.sp,
                            style = MaterialTheme.typography.h6,
                            textAlign = TextAlign.End,
                            maxLines = 1,
                            overflow = TextOverflow.Clip
                        )

                    }

                }
                LaunchedEffect(swipeableState.offset.value) {
                    swipe = swipeableState.offset.value > sizePx / 2
                }

                AnimatedVisibility(
                    visible = swipe,
                    enter = fadeIn(),
                    exit = fadeOut(),
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
                            onClick = { /*TODO*/ }
                        ) {
                            Text(text = "SEE", color = Color.White, fontSize = 10.sp)
                        }

                        Button(
                            modifier = Modifier
                                .fillMaxSize()
                                .weight(1f),
                            colors = ButtonDefaults.buttonColors(redColor),
                            onClick = { /*TODO*/ }
                        ) {
                            Text(text = "DELETE", color = Color.White, fontSize = 10.sp)
                        }
                    }
                }


            }


        }
    }


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
                    val intent = Intent(this, AddPlaceActivity::class.java)
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


    @Preview
    @Composable
    fun MainActivityLayoutPreview() {
        MainActivityLayout()
    }


}



