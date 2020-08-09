package com.vazheninapps.githubuserwatcher.pojo

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName


@Entity(tableName = "users")
data class User(

    @SerializedName("login")
    @Expose
    val login: String,

    @PrimaryKey
    @SerializedName("id")
    @Expose
    val id: Int,

    @SerializedName("avatar_url")
    @Expose
    val avatarUrl: String? = null

)