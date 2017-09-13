#import <Flutter/Flutter.h>
#import "BarcodeScannerViewController.h"

@interface BarcodeScannerPlugin : NSObject<FlutterPlugin, BarcodeScannerPluginDelegate>
@property (nonatomic, retain) UIViewController *viewController;
@property (nonatomic, retain) ViewController *scannerViewController;
@property (weak, nonatomic) NSString *scanResult;
@property (nonatomic, retain) FlutterResult resultCallback;

@end
