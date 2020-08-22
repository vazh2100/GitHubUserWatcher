package com.vazheninapps.githubuserwatcher.screens

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.vazheninapps.githubuserwatcher.R

class MainActivity : AppCompatActivity() {

    lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        userViewModel = ViewModelProvider.AndroidViewModelFactory(application).create(
            UserViewModel::class.java
        )

    }
    override fun onSupportNavigateUp() = findNavController(R.id.fragment).navigateUp()

}