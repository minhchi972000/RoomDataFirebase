package com.example.firebasekotlin

import android.app.Application


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