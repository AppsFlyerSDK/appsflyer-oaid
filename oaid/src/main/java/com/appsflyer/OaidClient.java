package com.appsflyer;

import android.content.Context;

import com.huawei.hms.ads.identifier.AdvertisingIdClient;
import com.miui.deviceid.IdentifierManager;

final class OaidClient {
    static Info fetch(Context context) {
        // Huawei
        try {
            if (AdvertisingIdClient.isAdvertisingIdAvailable(context)) {
                AdvertisingIdClient.Info info = AdvertisingIdClient.getAdvertisingIdInfo(context);
                return new Info(info.getId(), info.isLimitAdTrackingEnabled());
            }
        } catch (Throwable ignored) {
        }
        // Xiaomi
        try {
            Class.forName("com.android.id.impl.IdProviderImpl"); // preventing log from next line
            if (IdentifierManager.isSupported())
                return new Info(IdentifierManager.getOAID(context));
        } catch (Throwable ignored) {
        }
        return null;
    }

    static class Info {
        private final Boolean lat;
        private final String id;

        Info(String id, Boolean lat) {
            this.id = id;
            this.lat = lat;
        }

        Info(String id) {
            this(id, null);
        }

        public String getId() {
            return id;
        }

        public Boolean isLat() {
            return lat;
        }
    }
}