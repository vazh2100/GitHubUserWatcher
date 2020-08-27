package com.vazheninapps.githubuserwatcher.database

object LoggedUser {
    var token:String? = null
    var id: Int? = null

    fun clear(){
        token = null
        id = null
    }
}