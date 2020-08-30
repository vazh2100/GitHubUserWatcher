package com.vazheninapps.githubuserwatcher.screens.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.squareup.picasso.Picasso
import com.vazheninapps.githubuserwatcher.R
import com.vazheninapps.githubuserwatcher.database.LoggedUser
import kotlinx.android.synthetic.main.fragment_user_detailed.*

class UserDetailedFragment : Fragment() {
    private val viewModel: UserViewModel by navGraphViewModels(R.id.main_graph)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_user_detailed, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.isLoading.observe(viewLifecycleOwner,  {
            when (it) {
                true -> progressBarLoading.visibility = View.VISIBLE
                false -> progressBarLoading.visibility = View.INVISIBLE
            }
        })

        arguments?.let { bundle ->
            val id = bundle.getInt(EXTRA_ID)
            val login = bundle.getString(EXTRA_LOGIN)
            viewModel.getUserDetailed(id,login).observe(viewLifecycleOwner,  {
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
                    expandableTextView.setText(it.getBio())
                    if(it.id == LoggedUser.id){
                        buttonLogOut.visibility = View.VISIBLE
                        buttonLogOut.setOnClickListener {
                            requireActivity().getSharedPreferences("main", Context.MODE_PRIVATE).edit().clear().apply()
                            LoggedUser.clear()
                            findNavController().navigate(R.id.action_userDetailedFragment_to_loginFragment)
                        }
                    }
                }
            })
        }
    }

    companion object {
        private const val EXTRA_ID = "id"
        private const val EXTRA_LOGIN = "login"

        fun goTo(fragment: Fragment, id: Int, login: String?) {
            val bundle = Bundle()
            bundle.putInt(EXTRA_ID, id)
            bundle.putString(EXTRA_LOGIN, login)
            fragment.findNavController()
                .navigate(R.id.action_userListFragment_to_userDetailedFragment, bundle)
        }

    }


}