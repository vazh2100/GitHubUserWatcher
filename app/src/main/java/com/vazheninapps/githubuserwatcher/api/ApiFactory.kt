package com.vazheninapps.githubuserwatcher.api

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class ApiFactory  {

    private var retrofit: Retrofit
    val loginService:LoginService?
    val userService: UserService?

    private constructor(){
        retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl(BASE_URL)
            .build()
       userService = retrofit.create(UserService::class.java)
        loginService = null

    }

     constructor (login: String, password: String) {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(BasicInterceptor(login, password))
            .build()

        retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .build()

        loginService = retrofit.create(LoginService::class.java)
         userService = null
    }

    companion object{
       private const val BASE_URL = "https://api.github.com/"
       private  var apiFactory: ApiFactory? = null

        fun getInstance(): ApiFactory {
            apiFactory?.let{return it }
            val instance = ApiFactory()
            apiFactory = instance
            return  instance
        }

        fun isInternetConnection(context: Context): Boolean {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val networkCapabilities = connectivityManager.activeNetwork ?: return false
                val actNw = connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
                actNw.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            } else{
                @Suppress("DEPRECATION")
                val networkInfo = connectivityManager.activeNetworkInfo
                @Suppress("DEPRECATION")
                networkInfo != null && networkInfo.isConnected
            }
        }
    }
}