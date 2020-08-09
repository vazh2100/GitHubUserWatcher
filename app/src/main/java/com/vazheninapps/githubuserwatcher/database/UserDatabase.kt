package com.vazheninapps.githubuserwatcher.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.vazheninapps.githubuserwatcher.pojo.User
import com.vazheninapps.githubuserwatcher.pojo.UserDetailed


@Database(entities = [User::class, UserDetailed::class], version = 1, exportSchema = false)
abstract class UserDatabase : RoomDatabase() {

    companion object {
        private var db: UserDatabase? = null
        private const val DB_NAME = "user.db"
        private val LOCK = Any()

        fun getInstance(context: Context): UserDatabase {
            synchronized(LOCK) {
                db?.let { return it }
                val instance = Room
                    .databaseBuilder(context, UserDatabase::class.java, DB_NAME)
                    .allowMainThreadQueries()
                    .build()
                db = instance
                return instance
            }
        }
    }

    abstract fun userDao(): UserDao
}
