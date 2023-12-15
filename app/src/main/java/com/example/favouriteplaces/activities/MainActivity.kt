package com.example.favouriteplaces.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.com.example.favouriteplaces.R
import com.example.com.example.favouriteplaces.databinding.ActivityMainBinding
import com.example.favouriteplaces.FavouritePlacesManager
import com.example.favouriteplaces.models.FavouritePlaceModel

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MainActivityLayout()
        }


    }

    @Composable
    fun FavouritePlacesList() {

        val getData = FavouritePlacesManager.getFavouritePlaces()

        LazyColumn(contentPadding = PaddingValues(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(getData , key = null) { place ->
                FavouritePlaceItem(place)
            }
        }
    }

    @Composable
    fun FavouritePlaceItem(place: FavouritePlaceModel) {

        Card(modifier = Modifier
                        .padding()) {

            AsyncImage(model = place.image,
                       contentDescription = "Saved image",
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = place.title,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = place.description,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp
            )
        }



    }


    @Composable
    fun MainActivityLayout() {


        val primaryColor = colorResource(id = R.color.colorPrimary)
        val colorWhite = colorResource(id = R.color.white)

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Favourite Places",
                            color = colorWhite,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    backgroundColor = primaryColor,
                    elevation = 4.dp
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        val intent = Intent(this, AddPlaceActivity::class.java)
                        startActivity(intent)
                    },
                    modifier = Modifier
                        .padding(8.dp)

                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null
                    )
                }
            },
         content = { innerPadding ->
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