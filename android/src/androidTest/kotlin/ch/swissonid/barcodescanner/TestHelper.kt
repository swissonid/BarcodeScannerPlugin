package ch.swissonid.barcodescanner

import android.support.test.uiautomator.UiDevice
import android.support.test.uiautomator.UiSelector
import android.support.test.uiautomator.UiObject
import android.support.test.uiautomator.UiObjectNotFoundException





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

val TEXT_ALLOW = "ALLOW"
val TEXT_DENY = "DENY"
val TEXT_NEVER_ASK_AGAIN = "Don't ask again"
val TEXT_PERMISSIONS = "Permissions"

fun UiDevice.denyCurrentPermission() {
    this.findObject(UiSelector().text(TEXT_DENY)).click()
}

fun UiDevice.allowCurrentPermission() {
    this.findObject(UiSelector().text(TEXT_ALLOW)).click()
}

fun UiDevice.assertViewWithTextIsVisible(text: String) {
    val allowButton = this.findObject(UiSelector().text(text))
    if (!allowButton.exists()) {
        throw AssertionError("View with text <$text> not found!")
    }
}

@Throws(UiObjectNotFoundException::class)
fun UiDevice.denyCurrentPermissionPermanently() {
    val neverAskAgainCheckbox = this.findObject(UiSelector().text(TEXT_NEVER_ASK_AGAIN))
    neverAskAgainCheckbox.click()
    this.denyCurrentPermission()
}

@Throws(UiObjectNotFoundException::class)
fun UiDevice.grantPermission(permissionTitle: String) {
    val permissionEntry = this.findObject(UiSelector().text(permissionTitle))
    permissionEntry.click()
}

@Throws(UiObjectNotFoundException::class)
fun UiDevice.openPermissionsSettings() {
    this.findObject(UiSelector().text(TEXT_PERMISSIONS)).click()
}

fun UiDevice.toggleCameraPermissionSettings(){
    this.findObject(UiSelector().text("Camera")).click()
}