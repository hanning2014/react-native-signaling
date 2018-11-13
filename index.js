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