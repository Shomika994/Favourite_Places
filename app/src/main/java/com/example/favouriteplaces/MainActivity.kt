package com.example.favouriteplaces

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.favouriteplaces.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolbarMainActivity)

        binding?.floatingActionButton?.setOnClickListener {
            val intent = Intent(this, AddPlace::class.java)
            startActivity(intent)
        }



    }
}