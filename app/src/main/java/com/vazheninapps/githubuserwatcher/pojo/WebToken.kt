package com.vazheninapps.githubuserwatcher.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class WebToken (
    @SerializedName("access_token")
    @Expose var accessToken: String?,
    @SerializedName("scope")
    @Expose var scope: String?,
    @SerializedName("token_type")
    @Expose var tokenType: String?
)