package com.vazheninapps.githubuserwatcher.screens.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.navGraphViewModels
import com.vazheninapps.githubuserwatcher.R
import com.vazheninapps.githubuserwatcher.adapters.UserAdapter
import com.vazheninapps.githubuserwatcher.pojo.User
import com.vazheninapps.githubuserwatcher.screens.UserViewModel
import kotlinx.android.synthetic.main.fragment_user_list.*


class UserListFragment : Fragment() {
    val viewModel: UserViewModel by navGraphViewModels(R.id.navigation)

    private lateinit var adapter: UserAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = UserAdapter()
        adapter.onUserClickListener = object : UserAdapter.OnUserClickListener {
            override fun onUserClick(user: User) {
                UserDetailedFragment.goTo(this@UserListFragment, user.id, user.login)
        }}

        adapter.onReachEndListener = object : UserAdapter.OnReachEndListener {
            override fun onReachEnd() {
                viewModel.loadUsersData(adapter.getUserList().last().id)
            }
        }


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_user_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerViewUsers.adapter = adapter

    }

    override fun onResume() {
        super.onResume()
        viewModel.userListLD.observe(this, Observer {
            adapter.setUserList(it)
        })

    }
}