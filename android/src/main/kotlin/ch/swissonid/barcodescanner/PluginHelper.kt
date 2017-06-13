package ch.swissonid.barcodescanner

import io.flutter.plugin.common.MethodChannel

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

/**
 * Handles an error result
 *
 * @param errorCode An error code String.
 * @param errorMessage A human-readable error message String, possibly null
 * @param errorDetails - Error details, possibly null
 */
fun MethodChannel.Result.onError(errorCode: String, errorMessage: String? = null, errorDetails: Any? = null) {
    this.error(errorCode, errorMessage, errorDetails)
}