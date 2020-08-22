package com.vazheninapps.githubuserwatcher.screens

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.vazheninapps.githubuserwatcher.api.ApiFactory
import com.vazheninapps.githubuserwatcher.database.UserDatabase
import com.vazheninapps.githubuserwatcher.pojo.UserDetailed
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


class UserViewModel constructor(application: Application) : AndroidViewModel(application) {

    private val db = UserDatabase.getInstance(application)
    private val compositeDisposable = CompositeDisposable()
    val userListLD = db.userDao().getUsers()

    fun getUserDetailed(id: Int): LiveData<UserDetailed> {
        return db.userDao().getUserDetailed(id)
    }

    init {
        loadUsersData()
    }

    fun loadUsersData(since: Int = 0) {

        if (ApiFactory.isInternetConnection(getApplication())) {

            val disposable: Disposable = ApiFactory
                .apiService
                .getUsers(since)
                .retry()
                .subscribeOn(Schedulers.io())
                .subscribe({
                    db.userDao().insertUsers(it)
                    Log.d("TEST", it.toString())
                }, {
                    Log.d("TEST", it.message)
                })
            compositeDisposable.add(disposable)
        } else {
            Toast.makeText(getApplication(), "Нет интернет соединения", Toast.LENGTH_SHORT).show()
        }
    }

    fun loadUserData(login: String?) {

        if (ApiFactory.isInternetConnection(getApplication())) {
            val disposable: Disposable = ApiFactory
                .apiService
                .getUserDetail(login)
                .retry()
                .subscribeOn(Schedulers.io())
                .subscribe({ db.userDao().insertUserDetailed(it) }, {
                    Log.d("TEST", it.message)
                })
            compositeDisposable.add(disposable)
        } else {
            Toast.makeText(getApplication(), "Нет интернет соединения", Toast.LENGTH_SHORT).show()
        }

    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}