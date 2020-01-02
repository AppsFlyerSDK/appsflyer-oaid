package com.appsflyer.oaidtest;

import android.app.Application;
import android.os.StrictMode;

import com.appsflyer.AFLogger;
import com.appsflyer.AppsFlyerLib;

public class AFApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        StrictMode.enableDefaults();
        AppsFlyerLib appsflyer = AppsFlyerLib.getInstance();
        appsflyer.setLogLevel(AFLogger.LogLevel.VERBOSE);
        //noinspection SpellCheckingInspection
        appsflyer.init("WdpTVAcYwmxsaQ4WeTspmh", null, this);
        appsflyer.startTracking(this);
        appsflyer.setCollectOaid(true);
    }
}