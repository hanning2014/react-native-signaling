//
//  RCTAgora.h
//  RCTAgora
//
//  Created by 邓博 on 2017/6/13.
//  Copyright © 2017年 Syan. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import <React/RCTBridgeModule.h>
#import <AgoraSigKit/AgoraSigKit.h>

@interface RCTSignaling : NSObject<RCTBridgeModule>

@property (nonatomic, copy) NSString *appid;

@end
