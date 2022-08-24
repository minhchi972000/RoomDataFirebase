package com.example.firebasekotlin.data.db

import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.room.*

/**
 * DBO (Database object)
 */
@Entity(tableName = "movies")
data class MovieDBO(

    @PrimaryKey
    @ColumnInfo(name = "movie_id")
    var id: String = "",

    @ColumnInfo(name = "movie_background")
    var background: String? = null,

    @ColumnInfo(name = "movie_description")
    var description: String? = null,

    @ColumnInfo(name = "movie_title")
    var title: String? = null,

    @ColumnInfo(name = "movie_poster")
    var poster: String? = null,

    @ColumnInfo(name = "movie_vote")
    var vote: Double? = null,
) {

    // Data access object

    //TODO: strick for like a pro
    @Dao
    interface DAO :  BaseDAO<MovieDBO> {
        @Query("SELECT * FROM movies")
        fun liveData(): LiveData<List<MovieDBO>>

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        fun insertAll(vararg movies: MovieDBO)

        @Query("SELECT COUNT (*) FROM movies")
        fun count(): Int
    }

    companion object {

        val itemCallback
            get() = object : DiffUtil.ItemCallback<MovieDBO>() {
                override fun areItemsTheSame(oldItem: MovieDBO, newItem: MovieDBO): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(oldItem: MovieDBO, newItem: MovieDBO): Boolean {
                    return oldItem == newItem
                }
            }
    }
}

