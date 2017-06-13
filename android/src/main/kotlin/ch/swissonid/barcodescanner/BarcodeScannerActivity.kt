package ch.swissonid.barcodescanner

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.zxing.Result
import me.dm7.barcodescanner.zxing.ZXingScannerView

class BarcodeScannerActivity : AppCompatActivity(), ZXingScannerView.ResultHandler {
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
        mScannerView.stopCamera()
        finish()
    }

    override fun onResume() {
        super.onResume()
        startCamera()
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
    }

    private fun replaceContentView(view: View) {
        mContentViewGroup.addView(view)
    }

    override fun onPause() {
        mScannerView.stopCamera()
        super.onPause()
    }

    override fun onBackPressed() {
        setResult(Activity.RESULT_CANCELED)
        super.onBackPressed()
    }

    companion object {
        @JvmStatic
        fun startForResult(activity: Activity,requestCode: Int){
            val barcodeActivity = Intent(activity, BarcodeScannerActivity::class.java)
            activity.startActivityForResult(barcodeActivity, requestCode)
        }
    }
}
