package com.example.firebasekotlin

import android.app.Application
import com.example.firebasekotlin.data.db.AppDatabase


lateinit var app: App

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        app= this
    }

    override fun onTerminate() {
        super.onTerminate()
    }

}