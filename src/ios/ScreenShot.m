

#import "ScreenShot.h"
#import <Cordova/CDV.h>

@implementation ScreenShot

-(void)pluginInitialize{
    NSLog(@"xxx plugin initial");
    
    [self registerTakeScreenShotNotice];
}



//监听 截屏

- (void)registerTakeScreenShotNotice
{
    __weak __typeof(self) weakSelf = self;
    NSOperationQueue *mainQueue = [NSOperationQueue mainQueue];
    [[NSNotificationCenter defaultCenter] addObserverForName:UIApplicationUserDidTakeScreenshotNotification
                                                      object:nil
                                                       queue:mainQueue
                                                  usingBlock:^(NSNotification *note) {
                                                      // executes after screenshot
                                                      NSLog(@"截屏咯");
                                                      [weakSelf userDidTakeScreenshot];
                                                  }];
}

//截屏响应
- (void)userDidTakeScreenshot
{
    NSLog(@"检测到截屏");
    
    //人为截屏, 模拟用户截屏行为, 获取所截图片
    UIImage *image_ = [self imageWithScreenshot];
    NSData *imageData = UIImagePNGRepresentation(image_);
    
    NSString *path = [self saveToImage:imageData];
    if (path != nil){
        
        dispatch_async(dispatch_get_main_queue(), ^{
            
            [self.commandDelegate evalJs:[NSString stringWithFormat:@"cordova.plugins.ScreenShot.receiveScreenShotCallback('%@')",path]];
            
        });

    }
    
}


- (NSString*)saveToImage:(NSData*)screenshotPNG{
    NSArray *paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
    NSString *documentsDirectory = [paths objectAtIndex:0];
    
    
    NSDate *date = [NSDate date];
    
    NSString* imageName = [NSString stringWithFormat:@"screenshot_%f.png", [date timeIntervalSince1970]];
    
    NSError *error = nil;
    [screenshotPNG writeToFile:[documentsDirectory stringByAppendingPathComponent:imageName] options:NSAtomicWrite error:&error];
    
    NSString *fullPath = nil;
    if (error == nil){
        fullPath = [documentsDirectory stringByAppendingPathComponent:imageName];
    }
    return fullPath;
}


/**
 *  返回截取到的图片
 *
 *  @return UIImage *
 */
- (UIImage *)imageWithScreenshot
{
    NSData *imageData = [self dataWithScreenshotInPNGFormat];
    UIImage *rawImage = [UIImage imageWithData:imageData];
    
    return [self imageWithImage:rawImage scaledToSize:CGSizeMake(160, 240)];
}

//对图片尺寸进行压缩--
-(UIImage*)imageWithImage:(UIImage*)image scaledToSize:(CGSize)newSize
{
    // Create a graphics image context
    UIGraphicsBeginImageContext(newSize);
    
    // Tell the old image to draw in this new context, with the desired
    // new size
    [image drawInRect:CGRectMake(0,0,newSize.width,newSize.height)];
    
    // Get the new image from the context
    UIImage* newImage = UIGraphicsGetImageFromCurrentImageContext();
    
    // End the context
    UIGraphicsEndImageContext();
    
    // Return the new image.
    return newImage;
}

/**
 *  截取当前屏幕
 *
 *  @return NSData *
 */
- (NSData *)dataWithScreenshotInPNGFormat
{
    CGSize imageSize = CGSizeZero;
    UIInterfaceOrientation orientation = [UIApplication sharedApplication].statusBarOrientation;
    if (UIInterfaceOrientationIsPortrait(orientation))
        imageSize = [UIScreen mainScreen].bounds.size;
    else
        imageSize = CGSizeMake([UIScreen mainScreen].bounds.size.height, [UIScreen mainScreen].bounds.size.width);
    
    UIGraphicsBeginImageContextWithOptions(imageSize, NO, 0);
    CGContextRef context = UIGraphicsGetCurrentContext();
    for (UIWindow *window in [[UIApplication sharedApplication] windows])
    {
        CGContextSaveGState(context);
        CGContextTranslateCTM(context, window.center.x, window.center.y);
        CGContextConcatCTM(context, window.transform);
        CGContextTranslateCTM(context, -window.bounds.size.width * window.layer.anchorPoint.x, -window.bounds.size.height * window.layer.anchorPoint.y);
        if (orientation == UIInterfaceOrientationLandscapeLeft)
        {
            CGContextRotateCTM(context, M_PI_2);
            CGContextTranslateCTM(context, 0, -imageSize.width);
        }
        else if (orientation == UIInterfaceOrientationLandscapeRight)
        {
            CGContextRotateCTM(context, -M_PI_2);
            CGContextTranslateCTM(context, -imageSize.height, 0);
        } else if (orientation == UIInterfaceOrientationPortraitUpsideDown) {
            CGContextRotateCTM(context, M_PI);
            CGContextTranslateCTM(context, -imageSize.width, -imageSize.height);
        }
        if ([window respondsToSelector:@selector(drawViewHierarchyInRect:afterScreenUpdates:)])
        {
            [window drawViewHierarchyInRect:window.bounds afterScreenUpdates:YES];
        }
        else
        {
            [window.layer renderInContext:context];
        }
        CGContextRestoreGState(context);
    }
    
    UIImage *image = UIGraphicsGetImageFromCurrentImageContext();
    UIGraphicsEndImageContext();
    
    return UIImagePNGRepresentation(image);
}


@end

