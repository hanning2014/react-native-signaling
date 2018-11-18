import {
    NativeModules,
    findNodeHandle,
    NativeAppEventEmitter
}
    from 'react-native';

const {
    Signaling
} = NativeModules

export
default {...Signaling,
init(appId = null) {
    Signaling.init(appId);
},
login(userId) {
    this.listener && this.listener.remove();
    Signaling.login(userId);
},
// logout
logout() {
    Signaling.logout();
},
// channel join
channelJoin(channelID) {
    Signaling.channelJoin(channelID);
},
// send instant msg
sendSignalMsg(account, msg) {
    Signaling.sendSignalMsg(account, msg);
},
// send channel msg
sendChannelMsg(channelID, msg) {
    Signaling.sendChannelMsg(channelID, msg);
},
invoke(name, req, callID){
    Signaling.invoke(name, req, callID);
},
channelLeave(channelID) {
    Signaling.channelLeave(channelID);
},
queryUserStatus(account) {
    Signaling.queryUserStatus(account);
},
channelQueryUserNum(channelID) {
    Signaling.channelQueryUserNum(channelID);
},
channelInviteUser2(channelID, account, extras) {
    Signaling.channelInviteUser2(channelID, account, extras);
},
channelInviteAccept(channelID, account, extras) {
    Signaling.channelInviteAccept(channelID, account, extras);
},
channelInviteRefuse(channelID, account, extras) {
    Signaling.channelInviteRefuse(channelID, account, extras);
},
channelInviteEnd(channelID, account) {
    Signaling.channelInviteEnd(channelID, account, 0);
},
/* 回调类型如下，具体使用参考
 　android: https://docs.agora.io/cn/Signaling/signal_android?platform=Android#oninvitereceived-android
 ["onLogout","onLoginSuccess", "onLoginFailed", "onChannelJoined", "onChannelJoinFailed", "onChannelLeaved", "onChannelUserList","onChannelUserJoined","onChannelUserLeaved","onQueryUserStatusResult","onChannelQueryUserNumResult",
 "onReceiveMessage","onMessageChannelReceive", "onMessageSendSuccess", "onMessageSendError", "onInviteReceived", "onInviteReceivedByPeer","onInviteAcceptedByPeer", "onInviteRefusedByPeer", "onInviteFailed", "onInviteEndByPeer","onInviteEndByMyself", "onInviteMsg", "onError"
 ]
 */
eventEmitter(fnConf) {
    //there are no `removeListener` for NativeAppEventEmitter & DeviceEventEmitter
    this.listener && this.listener.remove();
    this.listener = NativeAppEventEmitter.addListener('SignalingEvent', event =>{
            fnConf[event['type']] && fnConf[event['type']](event);
});
},
removeEmitter() {
    this.listener && this.listener.remove();
}
};