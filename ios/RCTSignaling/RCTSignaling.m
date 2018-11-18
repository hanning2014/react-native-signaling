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
#import <AgoraSigKit/AgoraSigKit.h>

@interface RCTSignaling ()
@property (strong, nonatomic) AgoraAPI *agoraApi;
@end

@implementation RCTSignaling

RCT_EXPORT_MODULE();

@synthesize bridge = _bridge;

// todo destroy

//导出常量
- (NSDictionary *)constantsToExport {
    return @{};
}

- (void)sendEvent:(NSDictionary *)params {
    [_bridge.eventDispatcher sendDeviceEventWithName:@"agoraSignaling" body:params];
}

- (dispatch_queue_t)methodQueue {
    return dispatch_get_main_queue();
}

/**
 *  初始化AgoraSigKit
 *
 *  @param appid
 *  @return 0 when executed successfully. return negative value if failed.
 */
RCT_EXPORT_METHOD(init:(NSString *)AppId) {
    // 赋值appid
    _appid = AppId;
    _agoraApi = [AgoraAPI getInstanceWithoutMedia: AppId];

}

// 登录
RCT_EXPORT_METHOD(login: (NSString *)account) {
    [_agoraApi login2: _appid account:account token: @"_no_need_token" uid:0  deviceID: @""  retry_time_in_s:5  retry_count:1];
}

// 登出
RCT_EXPORT_METHOD(logout) {
    [_agoraApi logout];
}

// 加入频道
RCT_EXPORT_METHOD(channelJoin: (NSString *)roomId) {
    [_agoraApi channelJoin: roomId];
}

// 发送单个消息
RCT_EXPORT_METHOD(sendSignalMsg: (NSString *)channelName msg: (NSString *) msg) {
    [_agoraApi messageInstantSend: channelName uid:0 msg:msg msgID: @""];
}

// 发送频道消息
RCT_EXPORT_METHOD(sendChannelMsg: (NSString *)channelName msg: (NSString *) msg) {
    [_agoraApi messageChannelSend: channelName msg:msg msgID: @""];
}

// 以下是回调
//-（void）onLogout:(NSUInteger)ecode {
//    NSMutableDictionary *params = @{}.mutableCopy;
//    params[@"type"] = @"onLogout";
//    params[@"ecode"] = [NSNumber numberWithInteger:ecode];
//    [self sendEvent:params];
//}

- (void)_agoraApi:(AgoraAPI * )engine onLogout:(NSUInteger)ecode{
    NSMutableDictionary *params = @{}.mutableCopy;
    params[@"type"] = @"onLogout";
    params[@"uid"] = [NSNumber numberWithInteger:ecode];

    [self sendEvent:params];
}


@end

