package com.vazheninapps.githubuserwatcher.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vazheninapps.githubuserwatcher.pojo.User
import com.vazheninapps.githubuserwatcher.pojo.UserDetailed


@Dao
interface UserDao {

    @Query("SELECT * FROM users")
    fun getUsers(): LiveData<List<User>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUsers(users: List<User>)

    @Query("SELECT * FROM users_detailed WHERE id == :id")
    fun getUserDetailed(id: Int?): LiveData<UserDetailed>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUserDetailed(user: UserDetailed)
}