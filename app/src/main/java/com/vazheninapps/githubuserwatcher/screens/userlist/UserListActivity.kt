package com.vazheninapps.githubuserwatcher.screens.userlist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.vazheninapps.githubuserwatcher.R
import com.vazheninapps.githubuserwatcher.adapters.UserAdapter
import com.vazheninapps.githubuserwatcher.pojo.User
import com.vazheninapps.githubuserwatcher.screens.userdetail.UserDetailedActivity
import kotlinx.android.synthetic.main.activity_user_list.*

class UserListActivity : AppCompatActivity() {

    private lateinit var userViewModel: UserListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_list)
        val adapter = UserAdapter(this)
        adapter.onUserClickListener = object : UserAdapter.OnUserClickListener {
            override fun onUserClick(user: User) {
                intent = UserDetailedActivity.newIntent(
                    this@UserListActivity, user.id, user.login
                )
                startActivity(intent)
            }
        }
        adapter.onReachEndListener = object : UserAdapter.OnReachEndListener {
            override fun onReachEnd() {
                Log.d("TEST", adapter.getUserList().last().toString())
                userViewModel.loadData(adapter.getUserList().last().id)
            }
        }

        recyclerViewUsers.adapter = adapter
        userViewModel = ViewModelProvider.AndroidViewModelFactory(application).create(
            UserListViewModel::class.java
        )
        userViewModel.userListLD.observe(this, Observer {
            adapter.setUserList(it)
        })

    }
}