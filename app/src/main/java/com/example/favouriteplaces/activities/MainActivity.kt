package com.example.favouriteplaces.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import coil.compose.AsyncImage
import com.example.com.example.favouriteplaces.R
import com.example.favouriteplaces.FavouritePlacesManager
import com.example.favouriteplaces.models.FavouritePlaceModel
import kotlinx.coroutines.launch

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
            contentPadding = PaddingValues(12.dp), verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(favouritePlaceState, key = null) { place ->
                FavouritePlaceItem(place)
            }
        }
    }

    @Composable
    fun FavouritePlaceItem(place: FavouritePlaceModel) {

        Card(
            modifier = Modifier
                .padding(10.dp)
                .size(width = 350.dp, height = 150.dp),
            elevation = 10.dp
        ) {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
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
                        contentScale = ContentScale.FillBounds,

                        )

                    Spacer(modifier = Modifier.width(40.dp))

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
                            overflow = TextOverflow.Clip
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Text(
                            text = place.description,
                            fontWeight = FontWeight(1),
                            fontSize = 15.sp,
                            style = MaterialTheme.typography.h6,
                            textAlign = TextAlign.End,
                            overflow = TextOverflow.Clip
                        )
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



