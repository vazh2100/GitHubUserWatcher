package com.vazheninapps.githubuserwatcher.screens

import android.os.Bundle

import androidx.navigation.fragment.findNavController

import androidx.fragment.app.FragmentActivity

import com.vazheninapps.githubuserwatcher.R
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}