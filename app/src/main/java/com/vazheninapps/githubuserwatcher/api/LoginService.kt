package com.vazheninapps.githubuserwatcher.api

import com.vazheninapps.githubuserwatcher.pojo.BasicToken
import com.vazheninapps.githubuserwatcher.pojo.WebToken
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface LoginService {

    @POST("authorizations")
    fun createAuthorizationToken(@Body authBody: AuthBody): Observable<Response<BasicToken>>

    @POST("login/oauth/access_token")
    @Headers("Accept: application/json")
    fun loadWebToken(@Query("client_id") clientId: String?, @Query("client_secret") clientSecret: String?, @Query("code") code: String?, @Query("state") state: String?): Observable<Response<WebToken>>
}