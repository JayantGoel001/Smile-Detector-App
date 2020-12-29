package com.example.smiledetectorapp

import android.app.Application
import com.google.firebase.FirebaseApp

class LCOFaceDetection: Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)

    }
    companion object {
        const val RESULT_TEXT = "RESULT_TEXT"
        const val RESULT_DIALOG = "RESULT_DIALOG"
    }
}