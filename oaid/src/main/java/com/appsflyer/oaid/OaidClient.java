package com.appsflyer.oaid;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.text.TextUtils;

import com.bun.miitmdid.core.ErrorCode;
import com.bun.miitmdid.core.JLibrary;
import com.bun.miitmdid.core.MdidSdkHelper;
import com.bun.supplier.IIdentifierListener;
import com.bun.supplier.IdSupplier;
import com.huawei.hms.ads.identifier.AdvertisingIdClient;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OaidClient {
    private final Logger logger = Logger.getLogger("AppsFlyerOaid" + BuildConfig.VERSION_NAME);
    private final Context context;
    private final long timeout;
    private final TimeUnit unit;

    public OaidClient(Context context, long timeout, TimeUnit unit) {
        this.context = context;
        this.timeout = timeout;
        this.unit = unit;
        logger.setLevel(Level.OFF);
    }

    /**
     * 1 second timeout
     */
    public OaidClient(Context context) {
        this(context, 1, TimeUnit.SECONDS);
    }

    private static boolean isHuawei() {
        try {
            return Build.BRAND.equalsIgnoreCase("huawei") ||
                    (Integer) Class.forName("com.huawei.android.os.BuildEx$VERSION")
                            .getDeclaredField("EMUI_SDK_INT")
                            .get(null) > 0;
        } catch (ClassNotFoundException ignored) {
            return false;
        } catch (NoSuchFieldException ignored) {
            return false;
        } catch (IllegalAccessException ignored) {
            return false;
        }
    }

    /**
     * Blocking call. Time to fetch oaid is 10 - 1000 ms.
     */
    @Nullable
    public Info fetch() {
        try {
            long current = System.currentTimeMillis();
            Info info = isHuawei() ? fetchHuawei() : fetchMsa();
            logger.info("Fetch " + (System.currentTimeMillis() - current) + " ms");
            return info;
        } catch (Throwable t) {
            logger.log(Level.SEVERE, "Fetch", t);
            return null;
        }
    }

    @Nullable
    private Info fetchMsa() throws Exception {
        final BlockingQueue<String> oaidHolder = new LinkedBlockingQueue<>();
        JLibrary.InitEntry(context);
        int result = MdidSdkHelper.InitSdk(context, logger.getLevel() == null, new IIdentifierListener() {
            @Override
            public void OnSupport(boolean support, IdSupplier supplier) {
                try {
                    oaidHolder.offer(supplier == null ? "" : supplier.getOAID());
                } catch (Throwable t) {
                    logger.log(Level.SEVERE, "IIdentifierListener", t);
                }
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
    private Info fetchHuawei() {
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

    public void setLogging(boolean logging) {
        logger.setLevel(logging ? null : Level.OFF);
    }

    public static class Info {
        private final Boolean lat;
        private final String id;

        @VisibleForTesting
        public Info(String id, Boolean lat) {
            this.id = id;
            this.lat = lat;
        }

        @VisibleForTesting
        public Info(String id) {
            this(id, null);
        }

        public String getId() {
            return id;
        }

        /**
         * Available only in Huawei
         */
        @Nullable
        public Boolean getLat() {
            return lat;
        }
    }
}