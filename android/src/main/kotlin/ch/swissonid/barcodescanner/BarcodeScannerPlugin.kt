package ch.swissonid.barcodescanner

import android.app.Activity
import android.content.Intent
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry.ActivityResultListener
import io.flutter.plugin.common.PluginRegistry.Registrar

class BarcodeScannerPlugin private constructor(activity: Activity) : MethodCallHandler, ActivityResultListener {

    private val mActivity = activity
    //the requestCode used to identify the type of return message
    private val SCAN_BARCODE = 0
    private lateinit var _result : MethodChannel.Result

    companion object {
        @JvmStatic
        fun registerWith(registrar: Registrar): Unit {
            val channel = MethodChannel(registrar.messenger(), "barcodescanner")
            val barcodeScannerPlugin = BarcodeScannerPlugin(registrar.activity())
            channel.setMethodCallHandler(barcodeScannerPlugin)
            registrar.addActivityResultListener(barcodeScannerPlugin)
        }
    }

    override fun onMethodCall(call: MethodCall, result: Result): Unit {
        this._result = result
        if (call.method == "getPlatformVersion") {
            result.success("Android ${android.os.Build.VERSION.RELEASE}")
        } else if (call.method == "scanBarcode") {
          val barcodeActivity = Intent(mActivity, BarcodeScannerActivity::class.java)
          mActivity.startActivityForResult(barcodeActivity, SCAN_BARCODE)
        } else {
            result.notImplemented()
        }
    }

    //Called after result is set and finish() is called in activity
    override fun onActivityResult(requestCode: Int, resultCode: Int, barcodeData: Intent) :Boolean {
        if (requestCode == SCAN_BARCODE) {
          // the barcode was scanned properly.
            if (resultCode == Activity.RESULT_OK) {
                val barcodeValue = barcodeData.getStringExtra("text")
                val barcodeFormat = barcodeData.getStringExtra("barcodeFormat")
                //Pseudo-serialize BarcodeScanner
                this._result.success(barcodeValue + "||" + barcodeFormat)
            }
        }
        return true
    }

}
