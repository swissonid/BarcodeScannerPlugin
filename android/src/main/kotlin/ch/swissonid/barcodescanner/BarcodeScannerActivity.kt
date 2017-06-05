package ch.swissonid.barcodescanner

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v4.app.ActivityCompat.OnRequestPermissionsResultCallback
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.google.zxing.Result
import me.dm7.barcodescanner.zxing.ZXingScannerView


class BarcodeScannerActivity : AppCompatActivity(), ZXingScannerView.ResultHandler, OnRequestPermissionsResultCallback {
    private val REQUEST_CAMERA = 112
    private var alreadyAsked = false
    private lateinit var mScannerView: ZXingScannerView
    private lateinit var mContentViewGroup: ViewGroup

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
        if(alreadyAsked) { return }
        if(hasNotCameraPermission()){
            requestCameraPermission(REQUEST_CAMERA, {this.onRational()})
        } else {
            startCamera()
        }
    }

    fun onCameraPermissionDenied(){
        finish()
    }

    fun onCameraNeverAskAgain() {
        replaceContentView(R.layout.view_permission_not_granted)
        val button = findViewById(R.id.button) as Button
        button.text = "Open Permission Settings"
        button.setOnClickListener {
            this.alreadyAsked = false
            openPermissionSettings()
        }
    }

    fun onRational() {
        //TODO Start - move it to it's own widget
        replaceContentView(R.layout.view_permission_not_granted)
        val button = findViewById(R.id.button) as Button
        button.setOnClickListener { this.requestCameraPermission(REQUEST_CAMERA) }
        //TODO END - move it to it's own widget
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){
            REQUEST_CAMERA -> {
                alreadyAsked = true
                handlePermission(grantResults)
            }
            else -> { return }
        }
    }

    private fun handlePermission(grantResults: IntArray) {
        if (verifyPermissions(grantResults)) {
            startCamera()
        } else {
            if (!shouldShowRational(Manifest.permission.CAMERA)) {
                onCameraNeverAskAgain()
            } else {
                onCameraPermissionDenied()
            }
        }
    }

    private fun startCamera() {
        replaceContentView(mScannerView)
        mScannerView.setResultHandler(this)
        mScannerView.startCamera()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simple_scanner)
        setupToolbar()
        mContentViewGroup = findViewById(R.id.content_frame) as ViewGroup
        mScannerView = ZXingScannerView(this)
        mScannerView.id = R.id.scanner_preview
    }

    private fun replaceContentView(view: View) {
        if(mContentViewGroup.childCount > 0){
            mContentViewGroup.removeAllViews()
        }
        mContentViewGroup.addView(view)
    }

    private fun replaceContentView(@LayoutRes layoutId: Int) {
        val newView = layoutInflater.inflate(layoutId,mContentViewGroup,false)
        replaceContentView(newView)
    }

    override fun onPause() {
        mScannerView.stopCamera()
        super.onPause()
    }

    companion object {
        @JvmStatic
        fun start(activity: Activity){
            val barcodeActivity = Intent(activity, BarcodeScannerActivity::class.java)
            activity.startActivity(barcodeActivity)
        }
    }
}
