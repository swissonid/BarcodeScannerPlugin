package ch.swissonid.barcodescanner

import android.Manifest
import android.app.Activity
import android.content.Intent
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry
import io.flutter.plugin.common.PluginRegistry.ActivityResultListener
import io.flutter.plugin.common.PluginRegistry.Registrar

private const val REQUEST_CAMERA_PERMISSION = 32
private const val BARCODE_REQUEST_CODE = 123
private const val CAMERA_PERMISSION = Manifest.permission.CAMERA
private const val ERROR_CAMERA_PERMISSION_DENIED = "PERMISSION_DENIED"
private const val ERROR_CAMERA_PERMISSION_DENIED_NEVER_ASK_AGAIN = "NEVER_ASK_AGAIN"
private const val ERROR_SCANNING_CANCELED = "SCANNING_CANCELED"
private const val ERROR_CAMERA_SHOW_RATIONAL = "SHOW_RATIONAL"
class BarcodescannerPlugin private constructor(activity: Activity) : MethodCallHandler, ActivityResultListener, PluginRegistry.RequestPermissionResultListener {


    private val mActivity = activity
    private lateinit var mResult: Result

    companion object {
        @JvmStatic
        fun registerWith(registrar: Registrar): Unit {
            val channel = MethodChannel(registrar.messenger(), "barcodescanner")
            val barcodeScannerPlugin = BarcodescannerPlugin(registrar.activity())
            channel.setMethodCallHandler(barcodeScannerPlugin)
            registrar.addActivityResultListener(barcodeScannerPlugin)
            registrar.addRequestPermissionResultListener(barcodeScannerPlugin)
        }
    }

    override fun onMethodCall(call: MethodCall, result: Result): Unit {
        mResult = result
        when(call.method) {
            "hasCameraPermission" -> { result.success(mActivity.hasPermission(CAMERA_PERMISSION)) }
            "scanBarcode" -> { startBarcodeScanner(result) }
            "requestCameraPermission" -> { mActivity.requestCameraPermission(REQUEST_CAMERA_PERMISSION) }
            "openPermissionSettings" -> { mActivity.openPermissionSettings() }
            else -> result.notImplemented()
        }
    }

    fun startBarcodeScanner(result: Result) {
        if(mActivity.hasPermission(CAMERA_PERMISSION)){
            BarcodeScannerActivity.startForResult(mActivity, BARCODE_REQUEST_CODE)
        }else {
            if(mActivity.shouldShowRational(CAMERA_PERMISSION)){
                result.onError(ERROR_CAMERA_SHOW_RATIONAL)
            }
            else {
                mActivity.requestCameraPermission(REQUEST_CAMERA_PERMISSION)
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean {
        if(requestCode != BARCODE_REQUEST_CODE) return false
        when(resultCode) {
            Activity.RESULT_CANCELED -> { mResult.onError(ERROR_SCANNING_CANCELED)}
            else -> {
                val barcode: String? = data?.extras?.getString("text")
                mResult.success(barcode)
            }
        }
        return true
    }

    /**
     * @return true if the result has been handled.
     */
    override fun onRequestPermissionResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray): Boolean {
        when(requestCode) {
            REQUEST_CAMERA_PERMISSION -> {
                handleRequestResult(grantResults)
                return true
            }
            else -> return false
        }
    }

    fun handleRequestResult(grantResults: IntArray){
        if(verifyPermissions(grantResults)) {
            BarcodeScannerActivity.startForResult(mActivity, REQUEST_CAMERA_PERMISSION)
        }
        else {
            if(mActivity.shouldShowRational(CAMERA_PERMISSION)){
                mResult.onError(ERROR_CAMERA_PERMISSION_DENIED_NEVER_ASK_AGAIN)
            }
            else {
                mResult.onError(ERROR_CAMERA_PERMISSION_DENIED)
            }
        }

    }
}
