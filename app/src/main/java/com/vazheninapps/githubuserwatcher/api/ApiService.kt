package com.vazheninapps.githubuserwatcher.api

import com.vazheninapps.githubuserwatcher.pojo.User
import com.vazheninapps.githubuserwatcher.pojo.UserDetailed
import io.reactivex.Single
import retrofit2.http.*


interface ApiService {

    @Headers("Authorization: token $QUERY_PARAM_TOKEN")
    @GET("users")
    fun getUsers(@Query(QUERY_PARAM_SINCE) since: Int): Single<List<User>>

    @Headers("Authorization: token $QUERY_PARAM_TOKEN")
    @GET("/users/{username}")
    fun getUserDetail(@Path("username") login: String?): Single<UserDetailed>

    companion object {
        private const val QUERY_PARAM_TOKEN = "ecf07e7a5fda2e90247a2acee62fa00d49eb379b"
        private const val QUERY_PARAM_SINCE = "since"
    }
}