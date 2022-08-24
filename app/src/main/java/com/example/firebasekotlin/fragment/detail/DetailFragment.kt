package com.example.firebasekotlin.fragment.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.firebasekotlin.databinding.DetailListBinding

class DetailFragment : Fragment() {

    lateinit var vb: DetailListBinding
    private val detailVM: DetailVM by lazy {
        ViewModelProvider(requireActivity()).get(DetailVM::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        vb = DetailListBinding.inflate(inflater, container, false)
        return vb.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //detailVM.selectedItem
        detailVM.selectedItemLiveData.observe(viewLifecycleOwner) {
            vb.txtMovieName.text = it.title.toString()
            vb.txtMovieDescription.text = it.description.toString()
            vb.txtvote.text = it.vote.toString()

            val imgBackground =it.background.toString()

            val urlImage = "https://image.tmdb.org/t/p/w500${imgBackground}"
            Glide.with(this).load(urlImage)
                .into(vb.imgMovie)
        }


    }

}