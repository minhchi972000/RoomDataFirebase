package com.example.firebasekotlin.fragment.home


import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.firebasekotlin.data.db.AppDatabase
import com.example.firebasekotlin.data.db.MovieDBO
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MovieVM : ViewModel() {

    private var _movieData: MutableLiveData<List<MovieDBO>> = MutableLiveData<List<MovieDBO>>()
    val movieData: LiveData<List<MovieDBO>>
        get() = _movieData

    private var _errEvent: MutableLiveData<String> = MutableLiveData<String>()
    val errEvent: LiveData<String>
        get() = _errEvent

    val movieDao get() = AppDatabase.getDatabase().movieDao()

    fun syncDataMovie() {
        val db = Firebase.firestore
        val docRef = db.collection("Movies")
        docRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w("home", "Listen failed.", e)
                return@addSnapshotListener
            }
            if (snapshot != null) {
                for (document in snapshot) {
                    val characters = MovieDBO().also {
                        it.id = document.id
                        it.background = document.data.getValue("background").toString()
                        it.description = document.data.getValue("description").toString()
                        it.title = document.data.getValue("title").toString()
                        it.vote = document.data.getValue("vote").toString().toDouble()
                        it.poster = document.data.getValue("poster").toString()
                    }
                    // insert vao database
                    Log.d("home", "insert to movie table item: ${characters.title}")
                    movieDao.insertAll(characters)
                    _movieData.postValue(listOf(characters))
                }
                // kiem tra database co may record
                val itemCount = movieDao.count()
                Log.d("home", "Movie table has been updated, item count: $itemCount")
            }


        }
    }


    fun updateData(title: String, description: String, vote: Double) {
        val db = Firebase.firestore
        db.collection("Movies")
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful && !it.result.isEmpty) {

                    val document = it.result.documents.get(0)
                    val documentID: String = document.id
                    db.collection("Movies")
                        .document(documentID)
                        .update(
                            "title", title,
                            "description", description,
                            "vote", vote
                        )
                        .addOnSuccessListener {
                            Log.d("Movies", "${document.data}")
                        }
                }
            }
        Log.d("log","${FieldPath.documentId()}")


    }


    fun getMovies(): LiveData<List<MovieDBO>> {
        return movieDao.getAll()
    }

}
