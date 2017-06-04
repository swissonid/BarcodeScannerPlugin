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
        if (call.method == "getPlatformVersion") {
            result.success("Android ${android.os.Build.VERSION.RELEASE}")
        }
        if (call.method == "scanBarcode") {
            BarcodeScannerActivity.start(activity = mActivity)

            result.success("Start Activty")
        } else {
            result.notImplemented()
        }
    }

    override fun onActivityResult(p0: Int, p1: Int, p2: Intent?): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
