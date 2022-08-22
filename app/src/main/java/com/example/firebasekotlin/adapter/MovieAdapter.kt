package com.example.firebasekotlin.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.firebasekotlin.R
import com.example.firebasekotlin.data.db.MovieDBO
import com.example.firebasekotlin.databinding.MovieListItemBinding

class MovieAdapter : ListAdapter<MovieDBO, BaseVH>(MovieDBO.itemCallback) {

    var onItemClick: ((MovieDBO) -> Unit)? = null
    //var onItemLongClick: ((MovieDBO, Int) -> Unit)? = null
   var onItemLongClick: ((MovieDBO) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseVH {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.movie_list_item, parent, false)
        return BaseVH(view)
    }

    override fun onBindViewHolder(holder: BaseVH, position: Int) {
        val vb = MovieListItemBinding.bind(holder.itemView)
        val movie = getItem(position)
        vb.txtMovieName.text = movie.title
        vb.txtMovieDescription.text=movie.description
        val urlImage = "https://image.tmdb.org/t/p/w500${movie.poster}"
        Glide.with(holder.itemView.context).load(urlImage)
            .into(vb.imgMovie)

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(movie)
        }

        holder.itemView.setOnLongClickListener {
            onItemLongClick?.invoke(movie)//invoke(movie,position)

            true
        }
    }

    fun getMovie(position: Int): MovieDBO {
        return getItem(position);
    }
}