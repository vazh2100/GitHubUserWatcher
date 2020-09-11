package com.vazheninapps.githubuserwatcher.api

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object ApiFactoryNoApi {

    private const val BASE_URL_NOT_API = "https://github.com/"

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .baseUrl(BASE_URL_NOT_API)
        .build()

    val  loginServiceWebFlow = retrofit.create(LoginServiceWebFlow::class.java)
}