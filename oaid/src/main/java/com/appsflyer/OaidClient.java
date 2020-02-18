package com.appsflyer;

import android.content.Context;
import android.support.annotation.Nullable;

@Deprecated
class OaidClient {
    @Deprecated
    public Info fetch(Context context) {
        com.appsflyer.oaid.OaidClient.Info info = new com.appsflyer.oaid.OaidClient(context).fetch();
        if (info != null)
            return new Info(info.getId(), info.getLat());
        else
            return null;
    }

    @Deprecated
    static class Info {
        private final Boolean lat;
        private final String id;

        @Deprecated
        public Info(String id, Boolean lat) {
            this.id = id;
            this.lat = lat;
        }

        @Deprecated
        public String getId() {
            return id;
        }

        /**
         * Available only in Huawei
         */
        @Deprecated
        @Nullable
        public Boolean isLat() {
            return lat;
        }
    }
}