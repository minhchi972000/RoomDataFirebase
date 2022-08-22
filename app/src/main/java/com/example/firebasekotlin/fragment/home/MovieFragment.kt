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
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.firebasekotlin.R
import com.example.firebasekotlin.adapter.MovieAdapter
import com.example.firebasekotlin.data.db.MovieDBO
import com.example.firebasekotlin.databinding.MovieListBinding


class MovieFragment : Fragment() {

    lateinit var vb: MovieListBinding
    private val movieVM: MovieVM by lazy {
        ViewModelProvider(this).get(MovieVM::class.java)
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

        //TODO: phải viết cách method cụ thể để dễ đọc code và tìm
        //  vd:
        adapter.onItemClick = this::onMovieItemClick
        // hoặc
        adapter.onItemClick = {
            onMovieItemClick(it)
        }


        adapter.onItemClick = { movie ->
            println(movie.title)
            Log.d("movieFragment", "${movie.title}")
            Log.d("movie", "${movie.vote.toString().toDouble()}")

            //TODO:
            /**
             * Có rất nhiều cách để share data giữa các fragment:
             * bundle, navigation arguments, sử sụng viewModel...
             * Cách nào cũng chạy được nhưng thay vì sử dụng nhiều cách khác nhau ở nhiều chỗ code khác nhau
             * mình thống nhất là dự dụng cách này:
             * [https://developer.android.com/guide/fragments/communicate]
             * cho nó tiện và ngắn gọn :)
             */
            val bundle = bundleOf(
                "movieTitle" to movie.title,
                "movieDescription" to movie.description,
                "movieBackground" to movie.background,
                "movievote" to movie.vote.toString(),
            )
            findNavController().navigate(R.id.action_movieFragment_to_detailFragment, bundle)
        }

        adapter.onItemLongClick = { movie->

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
    }

    private fun onMovieItemClick(movie: MovieDBO){
        println(movie.title)
        Log.d("movieFragment", "${movie.title}")
        Log.d("movie", "${movie.vote.toString().toDouble()}")

        //TODO:
        /**
         * Có rất nhiều cách để share data giữa các fragment:
         * bundle, navigation arguments, sử sụng viewModel...
         * Cách nào cũng chạy được nhưng thay vì sử dụng nhiều cách khác nhau ở nhiều chỗ code khác nhau
         * mình thống nhất là dự dụng cách này:
         * [https://developer.android.com/guide/fragments/communicate]
         * cho nó tiện :)
         */
        val bundle = bundleOf(
            "movieTitle" to movie.title,
            "movieDescription" to movie.description,
            "movieBackground" to movie.background,
            "movievote" to movie.vote.toString(),
        )
        findNavController().navigate(R.id.action_movieFragment_to_detailFragment, bundle)
    }

    private fun registerData() {
        movieVM.movieListLiveData.observe(viewLifecycleOwner) {
            adapter.submitList(it)
            //TODO: ListAdapter.submitList là nó có so sánh list mới và cũ để update rồi
            // ko cần notifyDataSetChanged
            adapter.notifyDataSetChanged()
        }

//        movieVM.movieData.observe(viewLifecycleOwner) {
//            Toast.makeText(activity, "Successfully", Toast.LENGTH_LONG).show()
//        }

    }


}

