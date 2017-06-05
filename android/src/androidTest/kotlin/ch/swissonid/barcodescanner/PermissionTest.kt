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


import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.support.test.uiautomator.UiDevice
import org.junit.*
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters

@RunWith(AndroidJUnit4::class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class PermissionTest {

    private lateinit var mDevice: UiDevice
    @Rule @JvmField val barcodeScannerRule = ActivityTestRule(BarcodeScannerActivity::class.java)


    @Before fun setUp(){
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
    }

    @After fun shutDown() {
        barcodeScannerRule.activity.finish()
    }

    @Test fun a_should_display_permission_request() {
        mDevice.assertViewWithTextIsVisible(TEXT_ALLOW)
        mDevice.assertViewWithTextIsVisible(TEXT_DENY)
        mDevice.denyCurrentPermission()
    }

    @Test fun b_should_rational() {
        onView(withText(R.string.rational_text)).check(matches(isDisplayed()))
        onView(withText(R.string.grant_button)).check(matches(isDisplayed()))
    }

    @Test fun c_should_rational_and_ask_again_if_button_clicked() {
        onView(withText(R.string.rational_text)).check(matches(isDisplayed()))
        onView(withText(R.string.grant_button)).check(matches(isDisplayed()))
        onView(withId(R.id.button)).perform(click())
        mDevice.assertViewWithTextIsVisible(TEXT_ALLOW)
        mDevice.assertViewWithTextIsVisible(TEXT_DENY)
        mDevice.assertViewWithTextIsVisible(TEXT_NEVER_ASK_AGAIN)
        mDevice.denyCurrentPermissionPermanently()
    }

    @Test fun d_should_rational_and_go_to_permission_settings() {
        onView(withText(R.string.rational_text)).check(matches(isDisplayed()))
        onView(withId(R.id.button)).check(matches(isClickable()))
        onView(withId(R.id.button)).perform(click())
        mDevice.openPermissionsSettings()
        mDevice.toggleCameraPermissionSettings()
        mDevice.pressBack()
        mDevice.pressBack()
        onView(withId(R.id.scanner_preview)).check(matches(isDisplayed()))
    }

}