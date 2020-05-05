package com.muklas.probabidanstat

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.SurfaceHolder
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.Detector.Detections
import com.google.android.gms.vision.text.TextBlock
import com.google.android.gms.vision.text.TextRecognizer
import kotlinx.android.synthetic.main.activity_ocr.*
import java.io.IOException


class OCRActivity : AppCompatActivity(), View.OnClickListener {

    private var text = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ocr)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        startCameraSource()
        btnAdd.setOnClickListener(this)
    }

    private fun startCameraSource() { //Create the TextRecognizer
        val textRecognizer = TextRecognizer.Builder(applicationContext).build()
        if (!textRecognizer.isOperational) {
            Log.w(FragmentActivity::class.java.simpleName, "Detector dependencies not loaded yet")
        } else { //Initialize camerasource to use high resolution and set Autofocus on.
            val mCameraSource = CameraSource.Builder(applicationContext, textRecognizer)
                .setFacing(CameraSource.CAMERA_FACING_BACK)
                .setRequestedPreviewSize(1280, 1024)
                .setAutoFocusEnabled(true)
                .setRequestedFps(2.0f)
                .build()
            /**
             * Add call back to SurfaceView and check if camera permission is granted.
             * If permission is granted we can start our cameraSource and pass it to surfaceView
             */
            surfaceView.holder.addCallback(object : SurfaceHolder.Callback {
                override fun surfaceCreated(holder: SurfaceHolder) {
                    try {
                        if (ActivityCompat.checkSelfPermission(
                                applicationContext,
                                Manifest.permission.CAMERA
                            ) != PackageManager.PERMISSION_GRANTED
                        ) {
                            ActivityCompat.requestPermissions(
                                this@OCRActivity,
                                arrayOf(Manifest.permission.CAMERA),
                                100
                            )
                            return
                        }
                        mCameraSource.start(surfaceView.getHolder())
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }

                override fun surfaceChanged(
                    holder: SurfaceHolder,
                    format: Int,
                    width: Int,
                    height: Int
                ) {
                }

                /**
                 * Release resources for cameraSource
                 */
                override fun surfaceDestroyed(holder: SurfaceHolder) {
                    mCameraSource.stop()
                }
            })
            //Set the TextRecognizer's Processor.
            textRecognizer.setProcessor(object : Detector.Processor<TextBlock> {
                override fun release() {}
                /** Detect all the text from camera using TextBlock and the values into a stringBuilder
                 * which will then be set to the textView. */
                override fun receiveDetections(detections: Detections<TextBlock>) {
                    Log.i("Receive Detections", detections.toString());
                    val items = detections.detectedItems
                    if (items.size() != 0) {
                        text_view.post {
                            val stringBuilder =
                                StringBuilder()
                            for (i in 0 until items.size()) {
                                val item = items.valueAt(i)
                                stringBuilder.append(item.value)
                                stringBuilder.append("\n")
                            }
                            text_view.text = stringBuilder.toString()
                            text = stringBuilder.toString()
                        }
                    }
                }
            })
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnAdd -> {
                if (intent.hasExtra("data"))
                    setResult(100, intent.putExtra("data", text))
                else if (intent.hasExtra("leaf"))
                    setResult(100, intent.putExtra("leaf", text))
                finish()
            }
        }
    }
}
