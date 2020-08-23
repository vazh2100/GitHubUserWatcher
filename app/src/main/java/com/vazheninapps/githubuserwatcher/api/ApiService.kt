package com.vazheninapps.githubuserwatcher.api

import com.vazheninapps.githubuserwatcher.pojo.User
import com.vazheninapps.githubuserwatcher.pojo.UserDetailed
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query


interface ApiService {

    @Headers("Authorization: token $QUERY_PARAM_TOKEN")
    @GET("users")
    fun getUsers(@Query(QUERY_PARAM_SINCE) since: Int): Single<List<User>>

    @Headers("Authorization: token $QUERY_PARAM_TOKEN")
    @GET("/users/{username}")
    fun getUserDetail(@Path("username") login: String?): Single<UserDetailed>

    companion object {
        private const val QUERY_PARAM_TOKEN = "7f5989106f53860bdac8539a097b935ac8fd80ca"
        private const val QUERY_PARAM_SINCE = "since"
    }
}