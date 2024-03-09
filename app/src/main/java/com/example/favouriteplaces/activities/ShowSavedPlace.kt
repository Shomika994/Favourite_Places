package com.example.favouriteplaces.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.com.example.favouriteplaces.R
import com.example.favouriteplaces.models.FavouritePlaceModel


class ShowSavedPlace : AppCompatActivity() {

    private var favouritePlaces: FavouritePlaceModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val savePlace = intent.getSerializableExtra("savPlace") as? FavouritePlaceModel
        if (savePlace != null) {
            favouritePlaces = savePlace
        }

        setContent {
            MainActivityLayout()
        }

    }

    @SuppressLint("SuspiciousIndentation")
    @Composable
    fun MainActivityLayout() {

        val colorWhite = colorResource(id = R.color.white)
        val colorPrimary = colorResource(id = R.color.colorPrimary)

        Scaffold(topBar = {
            TopAppBar(title = {
                Text(
                    "Saved Places",
                    color = colorWhite,
                    fontWeight = FontWeight.Bold
                )
            },
                backgroundColor = colorPrimary,
                navigationIcon = {
                    Icon(imageVector = Icons.Default.ArrowBack,
                        contentDescription = null,
                        tint = colorWhite,
                        modifier = Modifier
                            .background(colorPrimary)
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
                    .background(colorPrimary)
            ) {
                favouritePlaces?.let { AllComponents(place = it, colorPrimary) }
            }
        })

    }

    @Composable
    private fun AllComponents(place: FavouritePlaceModel, color: Color) {

        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.Start
        ) {

            AsyncImage(
                model = place.image,
                contentScale = ContentScale.FillBounds,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp)
            ) {

                Text(
                    text = place.title,
                    color = color,
                    fontSize = 50.sp,
                )

                Spacer(modifier = Modifier.height(7.dp))

                Text(
                    text = place.description,
                    color = color,
                    fontSize = 30.sp
                )

                Spacer(modifier = Modifier.height(45.dp))

                Button(
                    onClick = {
                        val intent = Intent(this@ShowSavedPlace, MapActivity::class.java)
                        intent.putExtra("favPlace", place)
                        startActivity(intent)
                        //** TODO //
                    },
                    colors = ButtonDefaults.buttonColors(color),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .padding(4.dp)
                ) {
                    Text("View on the map", color = Color.White)
                }
            }
        }
    }
}