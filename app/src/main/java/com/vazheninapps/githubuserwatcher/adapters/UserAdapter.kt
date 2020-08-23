package com.vazheninapps.githubuserwatcher.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView
import com.squareup.picasso.Picasso
import com.vazheninapps.githubuserwatcher.R
import com.vazheninapps.githubuserwatcher.pojo.User
import kotlinx.android.synthetic.main.item_user_simple.view.*

class UserAdapter : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    var onUserClickListener: OnUserClickListener? = null
    var onReachEndListener: OnReachEndListener? = null
    private var userList: List<User> = arrayListOf()

    interface OnUserClickListener {
        fun onUserClick(user: User)
    }

    interface OnReachEndListener {
        fun onReachEnd()
    }

    fun getUserList(): List<User> {
        return userList
    }

    fun setUserList(userList: List<User>) {
        this.userList = userList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_user_simple, parent, false)
        return UserViewHolder(view)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        if (userList.size >= 25 && onReachEndListener != null && position > userList.size - 5) {
            onReachEndListener!!.onReachEnd()
        }

        val user = userList[position]
        with(holder) {
            Picasso.get().load(user.avatarUrl).into(imageViewAvatar)
            textViewLogin.text = user.login
            textViewId.text = user.id.toString()
            itemView.setOnClickListener {
                onUserClickListener?.onUserClick(user)
            }

        }
    }

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageViewAvatar: ShapeableImageView = itemView.imageViewAvatar
        val textViewLogin: TextView = itemView.textViewLogin
        val textViewId: TextView = itemView.textViewId
    }

}