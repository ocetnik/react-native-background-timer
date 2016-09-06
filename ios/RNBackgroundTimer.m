//
//  RNBackgroundTimer.m
//  react-native-background-timer
//
//  Created by IjzerenHein on 06-09-2016.
//  Copyright (c) ATO Gear. All rights reserved.
//

@import UIKit;
#import "RNBackgroundTimer.h"
#import "RCTEventDispatcher.h"

@implementation RNBackgroundTimer {
    UIBackgroundTaskIdentifier bgTask;
    int delay;
}

@synthesize bridge = _bridge;

RCT_EXPORT_MODULE()

- (void) _start
{
    [self _stop];
    bgTask = [[UIApplication sharedApplication] beginBackgroundTaskWithName:@"RNBackgroundTimer" expirationHandler:^{
        // Clean up any unfinished task business by marking where you
        // stopped or ending the task outright.
        [[UIApplication sharedApplication] endBackgroundTask:bgTask];
        bgTask = UIBackgroundTaskInvalid;
    }];
    
    UIBackgroundTaskIdentifier thisBgTask = bgTask;
    dispatch_after(dispatch_time(DISPATCH_TIME_NOW, delay * NSEC_PER_MSEC), dispatch_get_main_queue(), ^{
        if (thisBgTask == bgTask) {
            [self.bridge.eventDispatcher sendDeviceEventWithName:@"backgroundTimer" body:[NSNumber numberWithInt:thisBgTask]];
            [self _start];
        }
    });
}

- (void) _stop
{
    if (bgTask != UIBackgroundTaskInvalid) {
        [[UIApplication sharedApplication] endBackgroundTask:bgTask];
        bgTask = UIBackgroundTaskInvalid;
    }
}

RCT_EXPORT_METHOD(start:(int)_delay
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject)
{
    delay = _delay;
    [self _start];
    resolve([NSNumber numberWithBool:YES]);
}

RCT_EXPORT_METHOD(stop:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject)
{
    [self _stop];
    resolve([NSNumber numberWithBool:YES]);
}

RCT_EXPORT_METHOD(setTimeout:(int)timeoutId
                     timeout:(int)timeout
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject)
{
    UIBackgroundTaskIdentifier task = [[UIApplication sharedApplication] beginBackgroundTaskWithName:@"RNBackgroundTimer" expirationHandler:^{
        [[UIApplication sharedApplication] endBackgroundTask:task];
    }];

    dispatch_after(dispatch_time(DISPATCH_TIME_NOW, timeout * NSEC_PER_MSEC), dispatch_get_main_queue(), ^{
        [self.bridge.eventDispatcher sendDeviceEventWithName:@"backgroundTimer.timeout" body:[NSNumber numberWithInt:timeoutId]];
    });
    resolve([NSNumber numberWithBool:YES]);
}

/*
RCT_EXPORT_METHOD(clearTimeout:(int)timeoutId
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject)
{
    // Do nothing :)
    // timeout will be ignored in javascript anyway :)
}*/

@end
