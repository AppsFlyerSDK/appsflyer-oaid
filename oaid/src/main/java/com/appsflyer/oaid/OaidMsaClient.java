package com.appsflyer.oaid;

import android.content.Context;

import androidx.annotation.Nullable;

import com.bun.miitmdid.core.InfoCode;
import com.bun.miitmdid.core.MdidSdkHelper;
import com.bun.miitmdid.interfaces.IIdentifierListener;
import com.bun.miitmdid.interfaces.IdSupplier;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

class OaidMsaClient {
    private static boolean isCertInit = false;
    private static final String CER_PATTERN = "%s.cert.pem";
    private static final String MAS_NATIVE_LIB = "msaoaidsec";

    @Nullable
    static OaidClient.Info fetchMsa(Context context, final Logger logger, long timeout, TimeUnit unit) {
        try {
            loadNativeLibrary();
            if (!isCertInit) {
                try {
                    isCertInit = MdidSdkHelper.InitCert(context, loadPemFromAssetFile(context, String.format(CER_PATTERN, context.getPackageName()), logger));
                } catch (Throwable e) {
                    logger.warning(e.getMessage());
                }
                if (!isCertInit) {
                    logger.warning("getDeviceIds: cert init failed");
                }
            }
            final BlockingQueue<OaidClient.Info> oaidHolder = new LinkedBlockingQueue<>();
            int result = MdidSdkHelper.InitSdk(context, logger.getLevel() == null, new IIdentifierListener() {
                @Override
                public void onSupport(IdSupplier supplier) {
                    try {
                        if (supplier != null) {
                            oaidHolder.offer(new OaidClient.Info(supplier.getOAID(), supplier.isLimited()));
                        }
                    } catch (Throwable t) {
                        logger.info(t.getMessage());
                    }
                }
            });
            if (result != 0) {
                String error;
                switch (result) {
                    case InfoCode.INIT_ERROR_DEVICE_NOSUPPORT:
                        error = "Unsupported device";
                        break;
                    case InfoCode.INIT_ERROR_LOAD_CONFIGFILE:
                        error = "Error loading configuration file";
                        break;
                    case InfoCode.INIT_ERROR_MANUFACTURER_NOSUPPORT:
                        error = "Unsupported manufacturer";
                        break;
                    case InfoCode.INIT_INFO_RESULT_DELAY:
                        error = "Callback will be executed in a different thread";
                        break;
                    case InfoCode.INIT_ERROR_SDK_CALL_ERROR:
                        error = "Reflection call error";
                        break;
                    case InfoCode.INIT_INFO_RESULT_OK:
                        error = "result ok (sync)";
                        break;
                    case InfoCode.INIT_ERROR_CERT_ERROR:
                        error = "cert not init or check not pass";
                        break;
                    default:
                        error = String.valueOf(result);
                }
                logger.warning(error);
            }
            return oaidHolder.poll(timeout, unit);
        } catch (Throwable t) {
            logger.info(t.getMessage());
            t.printStackTrace();
            return null;
        }
    }

    protected static void loadNativeLibrary() {
        System.loadLibrary(MAS_NATIVE_LIB);
    }

    private static String loadPemFromAssetFile(Context context, String assetFileName, Logger logger) {
        try {
            InputStream is = context.getAssets().open(assetFileName);
            BufferedReader in = new BufferedReader(new InputStreamReader(is));
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                builder.append(line);
                builder.append('\n');
            }
            return builder.toString();
        } catch (IOException e) {
            logger.warning("loadPemFromAssetFile failed");
            return "";
        }
    }
}