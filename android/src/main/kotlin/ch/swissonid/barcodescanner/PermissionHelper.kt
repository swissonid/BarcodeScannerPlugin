package ch.swissonid.barcodescanner

/*
 * Copyright (C) 2017 Patrice Müller.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.support.v4.app.ActivityCompat

private const val ANDROID_M = 23

fun Activity.hasPermission(permission: String): Boolean {
    if(android.os.Build.VERSION.SDK_INT < ANDROID_M) return true
    return ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
}

fun Activity.shouldShowRational(permission: String): Boolean {
   return ActivityCompat.shouldShowRequestPermissionRationale(this,
            permission)
}

fun Activity.requestCameraPermission(permissionRequest: Int) {
    ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA),
            permissionRequest)
}

/**
 * Checks all given permissions haven been granted
 *
 * @param grantResults the result of the requested permissions
 * @return returns true if all permission have been granted other wise false.
 */
fun verifyPermissions(grantResults: IntArray): Boolean {
    if (grantResults.isEmpty()) return false
    return grantResults.none { it != PackageManager.PERMISSION_GRANTED }
}

fun Context.openPermissionSettings() {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    val uri = Uri.fromParts("package", packageName, null)
    intent.data = uri
    this.startActivity(intent)

}