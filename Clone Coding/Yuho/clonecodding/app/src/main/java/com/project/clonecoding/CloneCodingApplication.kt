package com.project.clonecoding

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class CloneCodingApplication: Application() {
    companion object{
        @SuppressLint("StaticFieldLeak")
        private lateinit var context: Context
    }
}