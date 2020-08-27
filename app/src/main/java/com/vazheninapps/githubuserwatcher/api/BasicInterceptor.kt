package com.vazheninapps.githubuserwatcher.api

import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.Response

class BasicInterceptor(user: String, password: String) :Interceptor {

    private var credentials = Credentials.basic(user, password)

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val authenticatedRequest = request.newBuilder()
            .header("Authorization", credentials).build()
        return chain.proceed(authenticatedRequest)
    }
}