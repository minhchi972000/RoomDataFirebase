package com.example.firebasekotlin.fragment.detail

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.firebasekotlin.databinding.MovieListDetailBinding

class DetailFragment : Fragment() {

    lateinit var vb: MovieListDetailBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        vb = MovieListDetailBinding.inflate(inflater, container, false)
        return vb.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vb.txtMovieName.text = arguments?.getString("movieTitle")
        vb.txtMovieDescription.text = arguments?.getString("movieDescription")
        vb.txtvote.text=arguments?.getString("movievote")

        Log.d("detail","${arguments?.getString("movievote")}")
        val imgBackground = arguments?.getString("movieBackground")

        val urlImage = "https://image.tmdb.org/t/p/w500${imgBackground}"
        Glide.with(this).load(urlImage)
            .into(vb.imgMovie)
    }

}