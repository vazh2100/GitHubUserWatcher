package com.vazheninapps.githubuserwatcher.api

import com.vazheninapps.githubuserwatcher.BuildConfig
import java.util.*

class AuthBody {

    private var note: String? = null
    private var note_url: String? = null
    private var client_id: String? = null
    private var client_secret: String? = null

    companion object {
        private const val NOTE = BuildConfig.APPLICATION_ID
        private const val NOTE_URL = "app://login"
        const val CLIENT_ID = "15cd599c5fa67e6c0154"
        const val CLIENT_SECRET = "1e432bb7b7cb02d42bc3afbc8ede65fd06cd1fbd"

        fun generate(): AuthBody {
            val body = AuthBody()
            body.note = NOTE
            body.note_url = NOTE_URL
            body.client_id = CLIENT_ID
            body.client_secret = CLIENT_SECRET
            return body
        }

        fun getUrlToWebAuth(): String? {
            val randomState = UUID.randomUUID().toString()
            return "https://github.com/login/oauth/authorize?client_id=$CLIENT_ID&state=$randomState"
        }


    }
}

