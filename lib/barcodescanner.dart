import 'dart:async';

import 'package:flutter/services.dart';

class Barcodescanner {
  static const MethodChannel _channel =
      const MethodChannel('barcodescanner');

  static Future<String> get platformVersion =>
      _channel.invokeMethod('getPlatformVersion');
  
  static Future<String> get scanBarcode =>
      _channel.invokeMethod("scanBarcode");
}
