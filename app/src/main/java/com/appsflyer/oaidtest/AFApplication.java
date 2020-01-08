package com.appsflyer.oaidtest;

import android.app.Application;
import android.os.StrictMode;

import com.appsflyer.AFLogger;
import com.appsflyer.AppsFlyerLib;
import com.appsflyer.OaidClient;

public class AFApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        StrictMode.enableDefaults();
        AppsFlyerLib appsflyer = AppsFlyerLib.getInstance();
        appsflyer.setLogLevel(AFLogger.LogLevel.VERBOSE);
        // tell to appsflyer to collect the OAID
        appsflyer.setCollectOaid(true);
        appsflyer.init("<DEV KEY>", null, this);
        appsflyer.startTracking(this);


        // or get it directly:
        OaidClient.Info oaidInfo = OaidClient.fetch(this);
        if (oaidInfo != null) {
            final String oaid = oaidInfo.getId();
        }
    }
}