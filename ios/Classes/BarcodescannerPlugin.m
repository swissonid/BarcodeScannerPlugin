#import "BarcodeScannerPlugin.h"

static NSString *const CHANNEL_NAME = @"barcodescanner";

@implementation BarcodeScannerPlugin

- (void)addItemViewController:(ViewController *)controller didFinishEnteringItem:(NSString *)item {
    //return results to dart interface
    self.resultCallback(item);
}

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
    self.resultCallback = result;
    if ([@"scanBarcode" isEqualToString:call.method]) {
        [self showBarcodeScanner:call];
    } else if ([@"getPlatformVersion" isEqualToString:call.method]){
        NSString *currSysVer = [[UIDevice currentDevice] systemVersion];
        result(currSysVer);
    } else {
        result(FlutterMethodNotImplemented);
    }
}

- (IBAction)Back
{
    [self.scannerViewController dismissViewControllerAnimated:YES completion:nil]; // ios 6
}

- (void)showBarcodeScanner:(FlutterMethodCall*)call {
    
    //TODO: Find a way to load a default view and also allow user to specify a custom view
    //load bundle created through barcodescanner.podspec
    NSString *bundlePath = [[NSBundle bundleForClass:[ViewController class]]
                            pathForResource:@"flutter_barcodescanner" ofType:@"bundle"];
    
    NSBundle *bundle = [NSBundle bundleWithPath:bundlePath];
    
    
    self.scannerViewController = [[ViewController alloc] initWithNibName:@"BarcodeScannerViewController" bundle:bundle];
    self.scannerViewController.delegate = self;

    self.scannerViewController.title = @"Barcode Scanner";
    
    UINavigationController *navigationController = [[UINavigationController alloc] initWithRootViewController:self.scannerViewController];
    
    //manually set backButton from image,because I am apparently
    //too stupid to get that back arrow working with the regular function -.-
    UIBarButtonItem *backButton = [[UIBarButtonItem alloc] initWithImage:[UIImage imageNamed:@"flutter_barcodescanner.bundle/Icon-Back"]
                                                                   style:UIBarButtonItemStylePlain
                                                                  target:self
                                                                  action:@selector(Back)];
    
    [navigationController.navigationBar.topItem setLeftBarButtonItem: backButton];
    
    navigationController.navigationBar.userInteractionEnabled = YES;
    navigationController.navigationBar.tintColor = [UIColor blackColor];

    
    //Draw simple red scanner line
    double padding = self.scannerViewController.view.bounds.size.width / 4;
    double x = padding / 2;
    double y = self.scannerViewController.view.bounds.size.height / 2;
    double thickness = 2.5;
    
    UIView *lineView = [[UIView alloc] initWithFrame:CGRectMake(x, y, self.scannerViewController.view.bounds.size.width - padding, thickness)];
    lineView.backgroundColor = [UIColor redColor];
    [self.scannerViewController.view addSubview:lineView];
    
    
    [self.viewController presentModalViewController:navigationController animated:YES];
}

@end
