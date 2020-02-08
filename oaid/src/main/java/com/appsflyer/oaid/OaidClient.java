package com.appsflyer.oaid;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.bun.miitmdid.core.ErrorCode;
import com.bun.miitmdid.core.JLibrary;
import com.bun.miitmdid.core.MdidSdkHelper;
import com.huawei.hms.ads.identifier.AdvertisingIdClient;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public final class OaidClient {
    private static final Logger logger =
            Logger.getLogger("AppsFlyerOaid" + BuildConfig.VERSION_NAME);

    static {
        if (!BuildConfig.DEBUG) LogManager.getLogManager().reset();
    }

    /**
     * Blocking call. Time to fetch oaid is 10 - 5000 ms.
     */
    @Nullable
    public static Info fetch(Context context, long timeout, TimeUnit unit) {
        try {
            long current = System.currentTimeMillis();
            Info info;
            if (Build.MANUFACTURER.equalsIgnoreCase("huawei")) {
                info = fetchHuawei(context);
                if (info == null) fetchMsa(context, timeout, unit);
            } else {
                info = fetchMsa(context, timeout, unit);
            }
            logger.info("Fetch " + (System.currentTimeMillis() - current) + " ms");
            return info;
        } catch (Throwable t) {
            logger.log(Level.SEVERE, "Fetch", t);
            return null;
        }
    }

    @Nullable
    private static Info fetchMsa(Context context, long timeout, TimeUnit unit) throws
            InterruptedException {
        BlockingQueue<String> oaidHolder = new LinkedBlockingQueue<>();
        JLibrary.InitEntry(context);
        int result = MdidSdkHelper.InitSdk(context, BuildConfig.DEBUG, (support, supplier) -> {
            try {
                if (supplier == null) {
                    oaidHolder.offer("");
                } else {
                    oaidHolder.offer(supplier.getOAID());
                    supplier.shutDown();
                }
            } catch (Throwable t) {
                logger.log(Level.SEVERE, "IIdentifierListener", t);
            }
        });
        if (result != 0) {
            String error;
            switch (result) {
                case ErrorCode.INIT_ERROR_DEVICE_NOSUPPORT:
                    error = "Unsupported device";
                    break;
                case ErrorCode.INIT_ERROR_LOAD_CONFIGFILE:
                    error = "Error loading configuration file";
                    break;
                case ErrorCode.INIT_ERROR_MANUFACTURER_NOSUPPORT:
                    error = "Unsupported manufacturer";
                    break;
                case ErrorCode.INIT_ERROR_RESULT_DELAY:
                    error = "Callback will be executed in a different thread";
                    break;
                case ErrorCode.INIT_HELPER_CALL_ERROR:
                    error = "Reflection call error";
                    break;
                default:
                    error = String.valueOf(result);
            }
            logger.warning(error);
        }
        String oaid = oaidHolder.poll(timeout, unit);
        return TextUtils.isEmpty(oaid) ? null : new Info(oaid);
    }

    @Nullable
    private static Info fetchHuawei(Context context) {
        try {
            if (AdvertisingIdClient.isAdvertisingIdAvailable(context)) {
                AdvertisingIdClient.Info info = AdvertisingIdClient.getAdvertisingIdInfo(context);
                return new Info(info.getId(), info.isLimitAdTrackingEnabled());
            } else {
                return null;
            }
        } catch (Throwable t) {
            logger.log(Level.SEVERE, "Huawei", t);
            return null;
        }
    }

    public static class Info {
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

        @Nullable
        public Boolean getLat() {
            return lat;
        }
    }
}