package com.example.smiledetectorapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment

class ResultDialog : DialogFragment() {
    lateinit var okButton: Button
    lateinit var resultTV :TextView
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_resultdialog,container,false)
        okButton = view.findViewById(R.id.result_ok_button)
        resultTV = view.findViewById(R.id.result_text_view)
        val resultText = arguments!!.getString(LCOFaceDetection.RESULT_TEXT)
        resultTV.text = resultText
        okButton.setOnClickListener {
            dismiss()
        }
        return view
    }
}