package com.vazheninapps.githubuserwatcher.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


 data class BasicToken(
     @SerializedName("id")
     @Expose var id: Int?,
     @SerializedName("url")
     @Expose var url: String?,
     @SerializedName("scopes")
     @Expose var scopes: List<String>?,
     @SerializedName("token")
     @Expose var token: String?
 )