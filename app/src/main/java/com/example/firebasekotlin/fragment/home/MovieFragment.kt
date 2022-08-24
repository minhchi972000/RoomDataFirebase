package com.example.firebasekotlin.fragment.home


import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.firebasekotlin.R
import com.example.firebasekotlin.adapter.MovieAdapter
import com.example.firebasekotlin.data.db.MovieDBO
import com.example.firebasekotlin.databinding.MovieListBinding
import com.example.firebasekotlin.fragment.detail.DetailVM


class MovieFragment : Fragment() {

    lateinit var vb: MovieListBinding

    private val movieVM: MovieVM by lazy {
        ViewModelProvider(this).get(MovieVM::class.java)
    }

    private val detailVM: DetailVM by lazy {
        ViewModelProvider(requireActivity()).get(DetailVM::class.java)
    }

    private val adapter = MovieAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        vb = MovieListBinding.inflate(inflater, container, false)
        return vb.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        movieVM.syncDataMovie()
        // movieVM.syncDataGame()

        setUpMovieList()
        registerData()
    }

    private fun setUpMovieList() {
        val lm = LinearLayoutManager(context)
        vb.recyclerViewMovie.layoutManager = lm
        vb.recyclerViewMovie.adapter = adapter

        adapter.onItemClick = { movie ->
            println(movie.title)
            Log.d("movieFragment", "${movie.title}")
            Log.d("movie", "${movie.vote.toString().toDouble()}")

//            val bundle = bundleOf(
//                "movieTitle" to movie.title,
//                "movieDescription" to movie.description,
//                "movieBackground" to movie.background,
//                "movievote" to movie.vote.toString(),
//            )
//
            detailVM.selectedItemLiveData.value = movie
           // detailVM.selectedItem =movie
            findNavController().navigate(R.id.action_movieFragment_to_detailFragment)

        }

        adapter.onItemLongClick = this::onItemLongClick
        // hoặc
        adapter.onItemLongClick = {
            onItemLongClick(it)
        }
    }

    private fun onItemLongClick(movie: MovieDBO){

        val dialogLayout = layoutInflater.inflate(R.layout.movie_list_item_longclick, null)
        val edtMovieTitle: EditText = dialogLayout.findViewById(R.id.edtTitleMovie)
        val edtMovieDescription: EditText = dialogLayout.findViewById(R.id.edtMovieDescription)
        val edtMovieVote: EditText = dialogLayout.findViewById(R.id.edtMovieVote)
        val imgMovie: ImageView = dialogLayout.findViewById(R.id.imgMovie)

        edtMovieTitle.text = Editable.Factory.getInstance().newEditable(movie.title)
        edtMovieDescription.text = Editable.Factory.getInstance().newEditable(movie.description)
        edtMovieVote.text = Editable.Factory.getInstance().newEditable(movie.vote.toString())

        val urlImage = "https://image.tmdb.org/t/p/w500${movie.background}"
        Glide.with(dialogLayout).load(urlImage).into(imgMovie)

        val builder = AlertDialog.Builder(requireContext())
            .setView(dialogLayout)
            .setTitle("Change Information")

        builder.apply {
            setPositiveButton(
                "Edit",
                DialogInterface.OnClickListener { _: DialogInterface?, _: Int ->
                    Toast.makeText(activity, "Edit", Toast.LENGTH_LONG).show()
                    movieVM.updateData(
                        edtMovieTitle.text.toString(),
                        edtMovieDescription.text.toString(),
                        edtMovieVote.text.toString().toDouble()
                    )
                })
            setNegativeButton(
                "Delete",
                DialogInterface.OnClickListener { _: DialogInterface?, _: Int ->
                    Toast.makeText(activity, "Delete", Toast.LENGTH_LONG).show()
                })
        }
        builder.show()
    }

    private fun registerData() {
        movieVM.movieListLiveData.observe(viewLifecycleOwner) {
            adapter.submitList(it)

        }
    }
}

