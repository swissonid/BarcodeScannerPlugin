/*
 * Copyright 2012 ZXing authors
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

#import <ZXingObjC/ZXingObjC.h>

@class BarcodeScannerViewController;

@protocol BarcodeScannerPluginDelegate <NSObject>
- (void)addItemViewController:(BarcodeScannerViewController *)controller didFinishEnteringItem:(NSString *)item;
@end

@interface ViewController : UIViewController <ZXCaptureDelegate>

@property (nonatomic, weak) id <BarcodeScannerPluginDelegate> delegate;
@property (nonatomic, strong) ZXCapture *capture;
@property (strong, nonatomic) IBOutlet UIView *view;
@property (nonatomic, weak) IBOutlet UIView *scanRectView;
@property (nonatomic, weak) IBOutlet UILabel *decodedLabel;

@end
