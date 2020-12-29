package com.example.smiledetectorapp

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetector
import com.google.mlkit.vision.face.FaceDetectorOptions

class MainActivity : AppCompatActivity() {

    lateinit var cameraButton: Button
    private val REQUEST_IMAGE_CAPTURE = 123
    private lateinit var image:InputImage
    private lateinit var detector:FaceDetector

    @SuppressLint("QueryPermissionsNeeded")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        FirebaseApp.initializeApp(this)
        cameraButton = findViewById(R.id.camera_button)
        cameraButton.setOnClickListener {
            val takePicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (takePicture.resolveActivity(packageManager)!=null) {
                startActivityForResult(takePicture,REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==REQUEST_IMAGE_CAPTURE && resultCode==Activity.RESULT_OK){
            val bitmap = data?.extras?.get("data") as Bitmap
            detectFace(bitmap)
        }
    }

    private fun detectFace(bitmap: Bitmap) {
        val options = FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
            .setClassificationMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
            .setMinFaceSize(0.15f)
            .enableTracking()
            .build()

        try {
            image = InputImage.fromBitmap(bitmap,0)
            detector = FaceDetection.getClient(options)
        }catch (e:Exception){
            e.printStackTrace()
        }
        detector.process(image).addOnSuccessListener {
            var resultText = ""
            var i = 1

            if (it !=null){
                for (face in it){
                    resultText = resultText.plus("\n$i.").plus("\nSmile : "+face.smilingProbability!!*100+"%")
                    i++
                }
            }
            if (it!=null){
                if (it.isEmpty()){
                    Toast.makeText(this@MainActivity,"No Face Detected",Toast.LENGTH_LONG).show()
                }else{
                    val bundle = Bundle()
                    bundle.putString(LCOFaceDetection.RESULT_TEXT,resultText)
                    val resultDialog = ResultDialog()
                    resultDialog.arguments = bundle
                    resultDialog.isCancelable = false
                    resultDialog.show(supportFragmentManager,LCOFaceDetection.RESULT_DIALOG)
                }
            }
        }
    }
}