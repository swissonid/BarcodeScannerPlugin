import 'dart:async';

import 'package:flutter/services.dart';

class BarcodeScanner {
  static const errorCodePermissionDenied = "PERMISSION_DENIED";
  static const errorCodePermissionDeniedNeverAskAgain = "NEVER_ASK_AGAIN";
  static const errorCameraShowRational = "SHOW_RATIONAL";
  static const errorScanningCanceled = "SCANNING_CANCELED";

  static const MethodChannel _channel =
      const MethodChannel('barcodescanner');

  static Future<Null> get requestPermission =>
      _channel.invokeMethod("requestCameraPermission");
  
  static Future<String> get scanBarcode =>
      _channel.invokeMethod("scanBarcode");

  static Future<bool> get hasCameraPermission =>
      _channel.invokeMethod("hasCameraPermission");

  static Future<Null> get openPermissionSettings => 
      _channel.invokeMethod("openPermissionSettings");

}
