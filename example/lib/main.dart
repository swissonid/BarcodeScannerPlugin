import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:barcodescanner/barcodescanner.dart';

void main() {
  runApp(new MyApp());
}

class MyApp extends StatelessWidget {
  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return new MaterialApp(
      title: 'Flutter Demo',
      theme: new ThemeData(
        // This is the theme of your application.
        //
        // Try running your application with "flutter run". You'll see
        // the application has a blue toolbar. Then, without quitting
        // the app, try changing the primarySwatch below to Colors.green
        // and then invoke "hot reload" (press "r" in the console where
        // you ran "flutter run", or press Run > Hot Reload App in IntelliJ).
        // Notice that the counter didn't reset back to zero -- the application
        // is not restarted.
        primarySwatch: Colors.blue,
      ),
      home: new MyHomePage(title: 'Flutter Demo Home Page'),
    );
  }
}

class MyHomePage extends StatefulWidget {
  MyHomePage({Key key, this.title}) : super(key: key);

  // This widget is the home page of your application. It is stateful,
  // meaning that it has a State object (defined below) that contains
  // fields that affect how it looks.

  // This class is the configuration for the state. It holds the
  // values (in this case the title) provided by the parent (in this
  // case the App widget) and used by the build method of the State.
  // Fields in a Widget subclass are always marked "final".

  final String title;

  @override
  _MyHomePageState createState() => new _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  String _errorCode = "";
  String _message = "";

  @override
  initState() {
    super.initState();
    initPlatformState();
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  initPlatformState() async {
    String initMessage;
    // Platform messages may fail, so we use a try/catch PlatformException.
    try {
      initMessage = "Didn't call yet anything";
    } on PlatformException {
      initMessage = "Failed to get platform version";
    }

    // If the widget was removed from the tree while the asynchronous platform
    // message was in flight, we want to discard the reply rather than calling
    // setState to update our non-existent appearance.
    if (!mounted) return;

    setState(() {
      _message = initMessage;
    });
  }

  _scanBarcode() async {
    String barcode;
    try {
      barcode = await BarcodeScanner.scanBarcode;
    } on PlatformException catch (exception) {
      barcode = handleBarcodeScannerExcpetion(exception);
    }
    if (!mounted) {
      return;
    }

    setState(() {
      _message = barcode;
    });
  }

  String handleBarcodeScannerExcpetion(PlatformException exception) {
    var message = "";
    var errorCode = exception.code;
    switch (exception.code) {
      case BarcodeScanner.errorCameraShowRational:
        message = "We need permission to camera to scann the barcode";
        break;
      case BarcodeScanner.errorCodePermissionDenied:
        message = "Permission Denied";
        break;
      case BarcodeScanner.errorCodePermissionDeniedNeverAskAgain:
        message = "Do not show it again please";
        break;
      case BarcodeScanner.errorScanningCanceled:
        message = "Scanning canceld";
        break;
      default:
        errorCode = "";
        message = "Deafult ....";
    }
    _errorCode = errorCode;
    return message;
  }

  _requestPermission() async {
    try {
      await BarcodeScanner.requestPermission;
    } on PlatformException catch (exception) {
      var message = handleBarcodeScannerExcpetion(exception);
      setState(() {
        _message = message;
      });
    }
  }

  _openPermissionSettings() async {
    try {
      await BarcodeScanner.openPermissionSettings;
    } on PlatformException catch (exception) {
      var message = handleBarcodeScannerExcpetion(exception);
      setState(() {
        _message = message;
      });
    }
  }

  Widget _getWidget() {
    var widgets = new List<Widget>();
    widgets.add(new Container(
      padding: const EdgeInsets.all(8.0),
      child: new Text(_message),
    ));
    switch (_errorCode) {
      case BarcodeScanner.errorCodePermissionDenied:
      case BarcodeScanner.errorCameraShowRational:
        widgets.add(new RaisedButton(
            child: new Text("Request Permissions Again"),
            onPressed: _requestPermission));
        break;

      case BarcodeScanner.errorCodePermissionDeniedNeverAskAgain:
        widgets.add(new RaisedButton(
            child: new Text("Open Permission Settings"),
            onPressed: _openPermissionSettings));
        break;
      default:
        break;
    }

    return new Row(
      mainAxisAlignment: MainAxisAlignment.center,
      children: <Widget>[
        new Expanded(
            child: new Column(
          children: <Widget>[
            new Expanded(
                child: new Column(
              mainAxisAlignment: MainAxisAlignment.center,
              children: widgets,
            ))
          ],
        ))
      ],
    );
  }

  @override
  Widget build(BuildContext context) {
    return new Scaffold(
      appBar: new AppBar(
        title: new Text('BarcodeScannerPlugin example '),
      ),
      floatingActionButton: new FloatingActionButton(
          child: new Icon(Icons.flip), onPressed: _scanBarcode),
      body: _getWidget(),
    );
  }
}
