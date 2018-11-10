//
//  RCTAgora.h
//  RCTAgora
//
//  Created by 邓博 on 2017/6/13.
//  Copyright © 2017年 Syan. All rights reserved.
//
#if __has_include("RCTBridgeModule.h")
#import "RCTBridgeModule.h"
#else
#import <React/RCTBridgeModule.h>
#endif
#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import <AgoraSigKit/AgoraSigKit.h>

@interface RCTSignaling : NSObject<RCTBridgeModule>

@end
