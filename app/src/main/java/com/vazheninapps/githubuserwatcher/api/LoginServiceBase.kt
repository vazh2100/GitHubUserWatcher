package com.vazheninapps.githubuserwatcher.api

import com.vazheninapps.githubuserwatcher.pojo.BasicToken
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginServiceBase {

    @POST("authorizations")
    fun createAuthorizationToken(@Body authBody: AuthBody): Observable<Response<BasicToken>>


}