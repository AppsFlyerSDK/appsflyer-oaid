package com.appsflyer.oaidtest;

import android.app.Application;
import android.os.StrictMode;
import android.util.Log;


import com.appsflyer.AFLogger;
import com.appsflyer.AppsFlyerConversionListener;
import com.appsflyer.AppsFlyerLib;

import org.json.JSONObject;

import java.util.Map;

public class AFApplication extends Application {
    public static final String TEST_APP_SHARED_PREF = "AFTestApp";
    private static final String TAG = "AFApplication";
    private static ConversionCallbackUiHandler handler;
    private static DeeplinkCallbackUiHandler deeplinkHandler;

    public static void setConversionCallbackHandler(ConversionCallbackUiHandler handler) {
        AFApplication.handler = handler;
    }

    public static void setDeeplinkCallbackHandler(DeeplinkCallbackUiHandler deeplinkHandler) {
        AFApplication.deeplinkHandler = deeplinkHandler;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        StrictMode.enableDefaults();
        AppsFlyerLib appsflyer = AppsFlyerLib.getInstance();

        appsflyer.setLogLevel(AFLogger.LogLevel.VERBOSE);

        appsflyer.init("WdpTVAcYwmxsaQ4WeTspmh", getConversionListener(), this);
        appsflyer.startTracking(this);
        appsflyer.setCollectOaid(true);
    }

    private AppsFlyerConversionListener getConversionListener() {
        return new AppsFlyerConversionListener() {
            @Override
            public void onConversionDataSuccess(Map<String, Object> conversionData) {
                Log.d(TAG, "onConversionDataSuccess: " + new JSONObject(conversionData).toString());
                if (handler != null) {
                    handler.onInstallConversionDataLoaded(conversionData);
                } else {
                    String res = "";
                    try {
                        res = new JSONObject(conversionData).toString();
                    } catch (Throwable ignore) {
                    }
                    Log.d(TAG, "onConversionDataSuccess: handler was null.\nCouldn't parse conversion: " + res);
                }
            }

            @Override
            public void onConversionDataFail(String errorMessage) {
                if (handler != null) {
                    handler.onInstallConversionFailure(errorMessage);
                } else {
                    Log.d(TAG, "onConversionDataFail: handler was null.\nCouldn't parse conversion: " + errorMessage);
                }
            }

            @Override
            public void onAppOpenAttribution(Map<String, String> conversionData) {
                if (deeplinkHandler != null) {
                    deeplinkHandler.onAppOpenAttribution(conversionData);
                } else {
                    String res = "";
                    try {
                        res = new JSONObject(conversionData).toString();
                    } catch (Throwable ignore) {
                    }
                    Log.d(TAG, "onAppOpenAttribution: handler was null.\nCouldn't parse conversion: " + res);
                }
            }

            @Override
            public void onAttributionFailure(String errorMessage) {
                Log.d(TAG, "Attribution error: " + errorMessage);
            }
        };
    }

    public interface ConversionCallbackUiHandler {
        void onInstallConversionDataLoaded(Map<String, Object> conversionData);

        void onInstallConversionFailure(String errorMessage);
    }

    public interface DeeplinkCallbackUiHandler {
        void onAppOpenAttribution(Map<String, String> conversionData);
    }

}
