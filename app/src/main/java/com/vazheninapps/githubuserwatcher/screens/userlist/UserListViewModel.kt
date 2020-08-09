package com.vazheninapps.githubuserwatcher.screens.userlist

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.vazheninapps.githubuserwatcher.api.ApiFactory
import com.vazheninapps.githubuserwatcher.database.UserDatabase
import com.vazheninapps.githubuserwatcher.pojo.User
import com.vazheninapps.githubuserwatcher.pojo.UserDetailed
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


class UserListViewModel(application: Application) : AndroidViewModel(application) {

    private val db = UserDatabase.getInstance(application)
    private val compositeDisposable = CompositeDisposable()
    val userListLD = db.userDao().getUsers()

    init {
        loadData()
    }

    fun loadData(since:Int = 0) {
        val disposable: Disposable = ApiFactory
            .apiService
            .getUsers(since)
            .retry()
            .subscribeOn(Schedulers.io())
            .subscribe({
                db.userDao().insertUsers(it)},{Log.d("TEST", it.message)})
        compositeDisposable.add(disposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}