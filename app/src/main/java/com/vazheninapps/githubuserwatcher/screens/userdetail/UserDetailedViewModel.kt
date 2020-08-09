package com.vazheninapps.githubuserwatcher.screens.userdetail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.vazheninapps.githubuserwatcher.api.ApiFactory
import com.vazheninapps.githubuserwatcher.database.UserDatabase
import com.vazheninapps.githubuserwatcher.pojo.UserDetailed
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class UserDetailedViewModel(application: Application) : AndroidViewModel(application) {

    private val db = UserDatabase.getInstance(application)
    private val compositeDisposable = CompositeDisposable()

    fun getUserDetailed(id: Int): LiveData<UserDetailed> {
        return db.userDao().getUserDetailed(id)
    }

    fun loadData(login: String) {
        val disposable: Disposable = ApiFactory
            .apiService
            .getUserDetail(login)
            .retry()
            .subscribeOn(Schedulers.io())
            .subscribe({ db.userDao().insertUserDetailed(it) }, {})
        compositeDisposable.add(disposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

}