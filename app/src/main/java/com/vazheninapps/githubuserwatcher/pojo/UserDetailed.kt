package com.vazheninapps.githubuserwatcher.pojo

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


@Entity(tableName = "users_detailed")
data class UserDetailed(

    @SerializedName("login")
    @Expose
    val login: String? = null,

    @PrimaryKey
    @SerializedName("id")
    @Expose
    val id: Int,

    @SerializedName("avatar_url")
    @Expose
    val avatarUrl: String? = null,

    @SerializedName("name")
    @Expose
    val name: String? = null,

    @SerializedName("company")
    @Expose
    private val company: String? = null,

    @SerializedName("blog")
    @Expose
    private val blog: String? = null,

    @SerializedName("location")
    @Expose
    private val location: String? = null,

    @SerializedName("email")
    @Expose
    private val email: String? = null,

    @SerializedName("twitter_username")
    @Expose
    private val twitterUsername: String? = null,

    @SerializedName("followers")
    @Expose
    val followers: Int? = null,

    @SerializedName("following")
    @Expose
    val following: Int? = null,

    @SerializedName("created_at")
    @Expose
    val createdAt: String? = null,
    @SerializedName("bio")
    @Expose
   private val bio:String? = null



) {

    fun getCompany(): String? = company ?: "Отсутствует"
    fun getBlog(): String? = if (blog==null || blog.isEmpty() ) { "Отсутствует"} else {blog}
    fun getLocation(): String? = location ?: "Отсутствует"
    fun getEmail(): String? = email ?: "Отсутствует"
    fun getTwitterUsername(): String? = twitterUsername ?: "Отсутствует"
    fun getBio():String? = bio ?: "Отсутствует"
}