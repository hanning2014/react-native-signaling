//
//  RCTSignaling.m
//  RCTSignaling
//
//  Created by 韩宁 on 2018/11/08.
//  Copyright © 2018年 Sw. All rights reserved.
//

#import "RCTSignaling.h"
#import <React/RCTEventDispatcher.h>
#import <React/RCTBridge.h>
#import <React/RCTUIManager.h>
#import <React/RCTView.h>

@implementation RCTSignaling

RCT_EXPORT_MODULE();

@synthesize bridge = _bridge;
@property (nonatomic, copy) NSString *appid;
//销毁引擎实例
- (void)dealloc {
    [AgoraSigKit destroy];
}

//导出常量
- (NSDictionary *)constantsToExport {
    return @{};
}

/**
 *  初始化AgoraSigKit
 *
 *  @param appid
 *  @return 0 when executed successfully. return negative value if failed.
 */
RCT_EXPORT_METHOD(init:(NSDictionary *)options) {
    // 赋值appid
    appid = options[@"appid"];
}

//加入房间
RCT_EXPORT_METHOD(login:(NSString *)AppId account:(NSString)account) {
    //保存一下uid 在自定义视图使用
     AgoraSignal.Kit.login2(AppId, account: account, token: "_no_need_token", uid: 0, deviceID: nil, retry_time_in_s: 60, retry_count: 5)
}


@end

