# barcodescanner

A simple flutter plugin allowing a user to scan barcodes (e.g. EAN-13 or QR) and return them to his app. Uses the [Zxing framework](https://github.com/zxing/zxing) for Barcode detection.

Note: This is still a work in progress. At the moment only the android functionality is developed and the code is far from perfect. Use with caution, but do use it :-).

## Example:
```dart
import 'dart:async';
import 'package:barcodescanner/barcodescanner.dart';

Map<String, dynamic> barcodeData = await Barcodescanner.scanBarcode;
println('Barcode: ' + barcodeData['barcode']);
println('BarcodeFormat: ' + barcodeData['barcodeFormat']);
```

## Getting Started

### Add the barcode dependency to flutter by adding this to your pubspec.yaml:
```yaml
dependencies:
  barcodescanner:
    #this will be replaced with a proper dependency, once the plugin is release to dart-pub
    git: https://github.com/swissonid/BarcodeScannerPlugin.git
```

If you'd like to modify the plugin for dev reasons, you can also easily add a dependency to the checked-out project folder using a path-dependency:

```yaml
dependencies:
  barcodescanner:
    path: pathToYourGitFolder/BarcodeScannerPlugin
```

### Add kotlin compile instructions to your gradle.build if your project doesn't use kotlin yet:
In the android folder of your project add this dependency to your build.gradle in your buildscript:
```gradle
classpath 'org.jetbrains.kotlin:kotlin-gradle-plugin:1.1.2-4'
```

e.g.
```gradle
buildscript {
    repositories {
        jcenter()
    }

    dependencies {
        classpath 'com.android.tools.build:gradle:2.2.3'
        classpath 'com.google.gms:google-services:3.1.0'
        classpath 'org.jetbrains.kotlin:kotlin-gradle-plugin:1.1.2-4'
    }
}
```

### Call the barcodescanner in your code:
e.g. like this:

```dart
import 'dart:async';
import 'package:barcodescanner/barcodescanner.dart';

[...]

Future _scanBarcode() async {
  Map<String, dynamic> barcodeData;
  try {
    //barcodeData is a JSON (Map<String,dynamic>) like this:
    //{barcode: '12345', barcodeFormat: 'ean-13'}
    barcodeData = await Barcodescanner.scanBarcode;
  } on PlatformException {
    barcodeData = {'barcode': 'Could not scan barcode'};
  }

  if (!mounted) return null;
  return barcodeData['barcode'];
}

String barcode = await _scanBarcode();
```

### Have fun
If there are any problems, write an issue or better yet: Try to fix it yourself and create a pull-request.
