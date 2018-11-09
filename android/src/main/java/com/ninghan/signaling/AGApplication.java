package com.ninghan.signaling;

import android.app.Application;
import android.util.Log;

import io.agora.AgoraAPIOnlySignal;

public class AGApplication extends Application {
    private final String TAG = AGApplication.class.getSimpleName();


    private static AGApplication mInstance;
    public AgoraAPIOnlySignal m_agoraAPI;


    public static AGApplication the() {
        return mInstance;
    }

    public AGApplication() {
        mInstance = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        setupAgoraEngine();
    }

    public AgoraAPIOnlySignal getmAgoraAPI() {
        return m_agoraAPI;
    }


    private void setupAgoraEngine() {
        String appID = "15a59de6b6b8406582710db1317e9e34";

        try {
            m_agoraAPI = AgoraAPIOnlySignal.getInstance(this, appID);


        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));

            throw new RuntimeException("NEED TO check rtc sdk init fatal error\n" + Log.getStackTraceString(e));
        }
    }


}

