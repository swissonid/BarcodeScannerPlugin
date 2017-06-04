#import "BarcodescannerPlugin.h"
#import <barcodescanner/barcodescanner-Swift.h>

@implementation BarcodescannerPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftBarcodescannerPlugin registerWithRegistrar:registrar];
}
@end
