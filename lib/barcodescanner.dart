import 'dart:async';

import 'package:flutter/services.dart';

class Barcodescanner {
  static const MethodChannel _channel = const MethodChannel('barcodescanner');

  static Future<String> get platformVersion => _channel.invokeMethod('getPlatformVersion');

  static Future<Map<String, dynamic>> get scanBarcode async {
    String barcodeData = await _channel.invokeMethod("scanBarcode");
    if (barcodeData != null && barcodeData.length > 0 && barcodeData.contains('||')) {
      return {'barcode': barcodeData.split('||')[0], 'barcodeFormat': barcodeData.split('||')[1]};
    } else {
      return {};
    }
  }
}
