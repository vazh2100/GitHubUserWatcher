package com.vazheninapps.githubuserwatcher.screens.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.squareup.picasso.Picasso
import com.vazheninapps.githubuserwatcher.R

import com.vazheninapps.githubuserwatcher.screens.UserViewModel
import kotlinx.android.synthetic.main.fragment_user_detailed.*

class UserDetailedFragment : Fragment() {

    val viewModel: UserViewModel by navGraphViewModels(R.id.navigation)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            val id = it.getInt(EXTRA_ID)
            val login = it.getString(EXTRA_LOGIN)
            viewModel.loadUserData(login)
            viewModel.getUserDetailed(id).observe(this, Observer {
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
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_user_detailed, container, false)
    }


    companion object {
        private const val EXTRA_ID = "id"
        private const val EXTRA_LOGIN = "login"


        fun goTo(fragment: Fragment, id: Int, login: String) {
            val bundle = Bundle()
            bundle.putInt(EXTRA_ID, id)
            bundle.putString(EXTRA_LOGIN, login)
            fragment.findNavController()
                .navigate(R.id.action_userListFragment_to_userDetailedFragment, bundle)
        }

    }


}