package com.example.firebasekotlin.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.firebasekotlin.app

@Database(
    entities = [MovieDBO::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase :RoomDatabase() {

    abstract fun movieDao(): MovieDBO.DAO

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    app.applicationContext,
                    AppDatabase::class.java, "movie_database"
                ).allowMainThreadQueries().build()
                INSTANCE = instance
                instance
            }
        }
    }
}