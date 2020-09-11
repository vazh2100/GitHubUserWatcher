package com.vazheninapps.githubuserwatcher.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


object ApiFactoryWithCredentials {

    private const val BASE_URL = "https://api.github.com/"

    private lateinit var okHttpClient: OkHttpClient
    private lateinit var retrofit: Retrofit
    lateinit var loginServiceBase: LoginServiceBase


    fun init(login:String, password:String):ApiFactoryWithCredentials{
        okHttpClient = OkHttpClient.Builder()
            .addInterceptor(BasicInterceptor(login, password))
            .build()

        retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .build()

        loginServiceBase = retrofit.create(LoginServiceBase::class.java)
        return this
    }





}