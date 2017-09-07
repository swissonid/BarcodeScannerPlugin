#import "BarcodeScannerPlugin.h"

static NSString *const CHANNEL_NAME = @"barcodescanner";

@implementation BarcodeScannerPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar> *)registrar {
    FlutterMethodChannel *channel = [FlutterMethodChannel methodChannelWithName: CHANNEL_NAME
                                                                binaryMessenger:[registrar messenger]];
    
    UIViewController *viewController = (UIViewController *)registrar.messenger;
    BarcodeScannerPlugin *instance = [[BarcodeScannerPlugin alloc] initWithViewController:viewController];
    [registrar addMethodCallDelegate:instance channel:channel];
}

- (instancetype)initWithViewController:(UIViewController *)viewController {
    self = [super init];
    if (self) {
        self.viewController = viewController;
    }
    return self;
}

- (void)handleMethodCall:(FlutterMethodCall *)call result:(FlutterResult)result {
    if ([@"scanBarcode" isEqualToString:call.method]) {
        //[self showBarcodeScanner:call];
        //result(nil);
        result(@"1234567891234||gtin");
    } else if ([@"getPlatformVersion" isEqualToString:call.method]){
        NSString *currSysVer = [[UIDevice currentDevice] systemVersion];
        result(currSysVer);
    } else {
        result(FlutterMethodNotImplemented);
    }
}
@end
