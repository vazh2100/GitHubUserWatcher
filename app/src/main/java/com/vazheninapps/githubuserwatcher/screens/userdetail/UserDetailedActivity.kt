package com.vazheninapps.githubuserwatcher.screens.userdetail

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.squareup.picasso.Picasso
import com.vazheninapps.githubuserwatcher.R
import kotlinx.android.synthetic.main.activity_user_detailed.*

class UserDetailedActivity : AppCompatActivity() {

    private lateinit var userDetailedViewModel: UserDetailedViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_detailed)

        if (!intent.hasExtra(EXTRA_ID) || !intent.hasExtra(EXTRA_LOGIN)) {
            finish()
            return
        }
        val id = intent.getIntExtra(EXTRA_ID, 0)
        val login = intent.getStringExtra(EXTRA_LOGIN)


        userDetailedViewModel = ViewModelProvider.AndroidViewModelFactory(application).create(
            UserDetailedViewModel::class.java
        )

        userDetailedViewModel.loadData(login)

        userDetailedViewModel.getUserDetailed(id).observe(this, Observer {
            it?.let {
                textViewLogin.text = it.login
                textViewName.text = it.name
                textViewLocation.text = it.getLocation()
                textViewCompany.text = it.getCompany()
                textViewEmail.text = it.getEmail()
                textViewTwitter.text = it.getTwitterUsername()
                textViewBlog.text = it.getBlog()
                textViewFollowers.text = it.followers.toString()
                textViewFollowing.text = it.following.toString()
                textViewCreatedAt.text = it.createdAt
                Picasso.get().load(it.avatarUrl).into(imageViewAvatar)
            }
        })
    }

    companion object {
        private const val EXTRA_ID = "id"
        private const val EXTRA_LOGIN = "login"

        fun newIntent(context: Context, id: Int, login: String): Intent {
            val intent = Intent(context, UserDetailedActivity::class.java)
            intent.putExtra(EXTRA_ID, id)
            intent.putExtra(EXTRA_LOGIN, login)
            return intent
        }
    }


}


