package ch.swissonid.barcodescanner

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityCompat.OnRequestPermissionsResultCallback
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.ViewGroup
import android.widget.Toast
import com.google.zxing.Result
import me.dm7.barcodescanner.zxing.ZXingScannerView


class BarcodeScannerActivity : AppCompatActivity(), ZXingScannerView.ResultHandler, OnRequestPermissionsResultCallback {
    private val  REQUEST_CAMERA = 112
    private lateinit var mScannerView: ZXingScannerView

    fun setupToolbar() {
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }

    override fun handleResult(rawResult: Result) {
        Toast.makeText(this, "Contents = ${rawResult.text} Format = ${rawResult.barcodeFormat}"
                        , Toast.LENGTH_SHORT).show()
        finishWithResult(rawResult)
        // Note:
        // * Wait 2 seconds to resume the preview.
        // * On older devices continuously stopping and resuming camera preview can result in freezing the app.
        // * I don't know why this is the case but I don't have the time to figure out.
       /* val handler = Handler()
        handler.postDelayed( { mScannerView.resumeCameraPreview(BarcodeScannerActivity@this) }, 2000)*/
    }

    private fun finishWithResult(rawResult: Result) {
        val resultIntent = Intent()
        resultIntent.putExtra("text", rawResult.text)
        resultIntent.putExtra("barcodeFormat", rawResult.barcodeFormat.toString())
        setResult(Activity.RESULT_OK, resultIntent)
    }

    override fun onResume() {
        super.onResume()
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            requestCameraPermission()
        } else {
            startCamera()
        }
    }





    private fun requestCameraPermission() {

        // BEGIN_INCLUDE(camera_permission_request)
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CAMERA)) {
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // For example if the user has previously denied the permission.

            Toast.makeText(this, "bla",Toast.LENGTH_SHORT).show()
        } else {

            // Camera permission has not been granted yet. Request it directly.
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA),
                    REQUEST_CAMERA)
        }
        // END_INCLUDE(camera_permission_request)
    }


    private fun startCamera() {
        mScannerView.setResultHandler(this)
        mScannerView.startCamera()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simple_scanner)
        setupToolbar()
        val viewGroup = findViewById(R.id.content_frame) as ViewGroup
        mScannerView =  ZXingScannerView(this)
        viewGroup.addView(mScannerView)
    }

    override fun onPause() {
        mScannerView.stopCamera()
        super.onPause()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    companion object {
        @JvmStatic
        fun start(activity: Activity){
            val barcodeActivity = Intent(activity, BarcodeScannerActivity::class.java)
            activity.startActivity(barcodeActivity)
        }
    }
}
