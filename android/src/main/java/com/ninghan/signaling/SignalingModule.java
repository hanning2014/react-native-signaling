package com.ninghan.signaling;

import android.support.annotation.Nullable;
import android.util.Log;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import io.agora.AgoraAPI;
import io.agora.AgoraAPIOnlySignal;
import io.agora.IAgoraAPI;

import static com.facebook.react.bridge.UiThreadUtil.runOnUiThread;

public class SignalingModule extends ReactContextBaseJavaModule {
    private final String TAG ="logs";
    private boolean stateSingleMode = true; // single mode or channel mode
    private AgoraAPIOnlySignal agoraAPI;
    private AgoraAPIOnlySignal m_agoraAPI;
    private static String appId = null;

    public SignalingModule(ReactApplicationContext context) {
        super(context);
    }

    @Override
    public String getName() {
        return "RCTSignaling";
    }


    private void commonEvent(WritableMap map) {
        sendEvent(getReactApplicationContext(), "SignalingEvent", map);
    }

    private void sendEvent(ReactContext reactContext,
                           String eventName,
                           @Nullable WritableMap params) {
        reactContext
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit(eventName, params);
    }

    // init callback
    @ReactMethod
    public void init(final String appIds) {
        appId = appIds;
        agoraAPI =  AgoraAPIOnlySignal.getInstance(getReactApplicationContext(), appId);
        Callback();
    }

    // login siginal
    @ReactMethod
    public void login(final String userId) {
        agoraAPI.login2(appId, userId, "_no_need_token", 0, "", 5, 1);
        Callback();
    }

    // join channel
    @ReactMethod
    public void channelJoin(final String roomId) {
        if(roomId == null || roomId.equals("")) {
            Log.i(TAG, "roomId is null");
        } else {
            agoraAPI.channelJoin(roomId);
        }
    }


    // send signal message
    @ReactMethod
    public void sendSignalMsg(final String channelName, String msg) {
        if(msg == null || msg.equals("")) {
            Log.i(TAG, "msg is null");
        } else {
            agoraAPI.messageInstantSend(channelName, 0, msg, "");
        }
    }


    // send channel message
    @ReactMethod
    public void sendChannelMsg(final String channelName, String msg) {
        if(msg == null || msg.equals("")) {
            Log.i(TAG, "mg is null");
        } else {
            agoraAPI.messageChannelSend(channelName, msg, "");
        }
    }


    // login callback
    private void Callback() {
        try {

            if (agoraAPI == null) {
                return;
            }

            agoraAPI.callbackSet(new AgoraAPI.CallBack() {
                @Override
                public void onLoginSuccess(final int i, final int i1) {
                    Log.i(TAG, "onLoginSuccess " + i + "  " + i1);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            WritableMap map = Arguments.createMap();
                            map.putString("type", "onLoginSuccess");
                            map.putInt("uid", i);
                            map.putInt("uid1", i1);
                            commonEvent(map);
                        }
                    });
                }

                @Override
                public void onLoginFailed(final int i) {
                    Log.i(TAG, "onLoginFailed " + i);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            WritableMap map = Arguments.createMap();
                            map.putString("type", "onLoginFailed");
                            map.putInt("uid", i);
                            commonEvent(map);

                        }
                    });
                }

                @Override
                public void onChannelJoined(final String channelID) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            WritableMap map = Arguments.createMap();
                            map.putString("type", "onChannelJoined");
                            map.putString("channelID", channelID);
                            commonEvent(map);
                        }
                    });

                }

                @Override
                public void onChannelJoinFailed(final String channelID, final int ecode) {
                    super.onChannelJoinFailed(channelID, ecode);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            WritableMap map = Arguments.createMap();
                            map.putString("type", "onChannelJoinFailed");
                            map.putString("channelID", channelID);
                            map.putInt("channelID", ecode);
                            commonEvent(map);
                        }
                    });
                }

                @Override
                public void onChannelUserList(final String[] accounts, final int[] uids) {
                    super.onChannelUserList(accounts, uids);
                    // convert list to string
                    final StringBuilder sb = new StringBuilder();
                    for (String s : accounts)
                    {
                        sb.append(s);
                        sb.append(",");
                    }
                    final StringBuilder sb1 = new StringBuilder();
                    for (int s : uids)
                    {
                        sb1.append(Integer.toString(s));
                        sb1.append(",");
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            WritableMap map = Arguments.createMap();
                            map.putString("type", "onChannelUserList");
                            map.putString("accounts", sb.toString());
                            map.putString("uids", sb1.toString());
                            commonEvent(map);
                        }
                    });
                }

                @Override
                public void onLogout(final int i) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (i == IAgoraAPI.ECODE_LOGOUT_E_KICKED) { //other login the account
                                WritableMap map = Arguments.createMap();
                                map.putString("type", "onLogoutForceout");
                                map.putInt("code", i);
                                commonEvent(map);

                            } else if (i == IAgoraAPI.ECODE_LOGOUT_E_NET) { //net
                                WritableMap map = Arguments.createMap();
                                map.putString("type", "onLogoutForNetwork");
                                map.putInt("code", i);
                                commonEvent(map);
                            }

                        }
                    });

                }

                @Override
                public void onChannelUserJoined(final String account, final int uid) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            WritableMap map = Arguments.createMap();
                            map.putString("type", "onChannelUserJoined");
                            map.putInt("uid", uid);
                            map.putString("account", account);
                            commonEvent(map);
                        }
                    });
                }

                @Override
                public void onChannelUserLeaved(final String account, final int uid) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            WritableMap map = Arguments.createMap();
                            map.putString("type", "onChannelUserLeaved");
                            map.putInt("uid", uid);
                            map.putString("account", account);
                            commonEvent(map);
                        }
                    });
                }


                @Override
                public void onQueryUserStatusResult(final String name, final String status) {
                    Log.i(TAG, "onQueryUserStatusResult  name = " + name + "  status = " + status);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            WritableMap map = Arguments.createMap();
                            map.putString("type", "onQueryUserStatusResult");
                            map.putString("status", status);
                            map.putString("name", name);
                            commonEvent(map);
                        }
                    });
                }

                // get signal message
                @Override
                public void onMessageInstantReceive(final String account, final int uid, final String msg) {
                    Log.i(TAG, "onMessageInstantReceive  account = " + account + " uid = " + uid + " msg = " + msg);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            WritableMap map = Arguments.createMap();
                            map.putString("type", "onReceiveMessage");
                            map.putString("account", account);
                            map.putInt("uid", uid);
                            map.putString("msg", msg);
                            commonEvent(map);
                        }
                    });
                }

                @Override
                public void onMessageChannelReceive(final String channelID, final String account, final int uid, final String msg) {
                    Log.i(TAG, "onMessageChannelReceive  account = " + account + " uid = " + uid + " msg = " + msg);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //self message had added
                            WritableMap map = Arguments.createMap();
                            map.putString("type", "onMessageChannelReceive");
                            map.putString("account", account);
                            map.putString("channelID", channelID);
                            map.putInt("uid", uid);
                            map.putString("msg", msg);
                            commonEvent(map);
                        }
                    });
                }

                @Override
                public void onMessageSendSuccess(final String messageID) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //self message had added
                            WritableMap map = Arguments.createMap();
                            map.putString("type", "onMessageSendSuccess");
                            map.putString("messageID", messageID);
                            commonEvent(map);
                        }
                    });
                }

                @Override
                public void onMessageSendError(final String messageID, final int ecode) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //self message had added
                            WritableMap map = Arguments.createMap();
                            map.putString("type", "onMessageSendError");
                            map.putString("messageID", messageID);
                            map.putInt("ecode", ecode);
                            commonEvent(map);
                        }
                    });
                }




                @Override
                public void onError(String s, int i, String s1) {
                    Log.i(TAG, "onError s:" + s + " s1:" + s1);
                }

            });

        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));

            throw new RuntimeException("NEED TO check rtc sdk init fatal error\n" + Log.getStackTraceString(e));
        }
    }

}
