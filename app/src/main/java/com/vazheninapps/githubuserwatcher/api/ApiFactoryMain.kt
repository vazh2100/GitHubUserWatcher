package com.vazheninapps.githubuserwatcher.api

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object ApiFactoryMain {

    private const val BASE_URL = "https://api.github.com/"

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .baseUrl(BASE_URL)
        .build()

    val userService = retrofit.create(UserService::class.java)
}