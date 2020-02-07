package com.appsflyer.oaid;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.bun.miitmdid.core.ErrorCode;
import com.bun.miitmdid.core.JLibrary;
import com.bun.miitmdid.core.MdidSdkHelper;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * Time to get oaid around 10 - 5000 ms
 */
public final class OaidClient {
    private static final Logger logger =
            Logger.getLogger("AppsFlyerOaid" + BuildConfig.VERSION_NAME);

    static {
        if (!BuildConfig.DEBUG) LogManager.getLogManager().reset();
    }

    @Nullable
    public static String fetch(Context context, long timeout, TimeUnit unit) {
        try {
            long current = System.currentTimeMillis();
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
            logger.info("Fetch " + (System.currentTimeMillis() - current) + " ms");
            return TextUtils.isEmpty(oaid) ? null : oaid;
        } catch (Throwable t) {
            logger.log(Level.SEVERE, "Fetch", t);
            return null;
        }
    }
}