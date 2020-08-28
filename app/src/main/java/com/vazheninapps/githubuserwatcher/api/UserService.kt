package com.vazheninapps.githubuserwatcher.api

import com.vazheninapps.githubuserwatcher.pojo.User
import com.vazheninapps.githubuserwatcher.pojo.UserDetailed
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface UserService {

    @GET("user")
    fun getLoggedUser(@Header(HEADER_PARAM_AUTORIZATION) token:String? ):Single<UserDetailed>

    @GET("users")
    fun getUsers(@Query(QUERY_PARAM_SINCE) since: Int, @Header(HEADER_PARAM_AUTORIZATION) token:String? ): Single<List<User>>

    @GET("/users/{username}")
    fun getUserDetail(@Path("username") login: String?, @Header(HEADER_PARAM_AUTORIZATION) token:String?): Single<UserDetailed>

    companion object {
        private const val QUERY_PARAM_SINCE = "since"
        private const val HEADER_PARAM_AUTORIZATION = "Authorization"
    }
}