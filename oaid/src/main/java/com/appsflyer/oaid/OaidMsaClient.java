package com.appsflyer.oaid;

import android.content.Context;
import androidx.annotation.Nullable;
import android.text.TextUtils;

import com.bun.miitmdid.core.ErrorCode;
import com.bun.miitmdid.core.MdidSdkHelper;
import com.bun.miitmdid.interfaces.IIdentifierListener;
import com.bun.miitmdid.interfaces.IdSupplier;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

class OaidMsaClient {
    @Nullable
    static OaidClient.Info fetchMsa(Context context, final Logger logger, long timeout, TimeUnit unit) {
        try {
            final BlockingQueue<String> oaidHolder = new LinkedBlockingQueue<>();
            int result = MdidSdkHelper.InitSdk(context, logger.getLevel() == null, new IIdentifierListener() {
                @Override
                public void OnSupport(boolean support, IdSupplier supplier) {
                    try {
                        oaidHolder.offer(supplier == null ? "" : supplier.getOAID());
                    } catch (Throwable t) {
                        logger.info(t.getMessage());
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
                    case ErrorCode.INIT_ERROR_BEGIN:
                        error = "Init error begin";
                        break;
                    default:
                        error = String.valueOf(result);
                }
                logger.warning(error);
            }
            String oaid = oaidHolder.poll(timeout, unit);
            return TextUtils.isEmpty(oaid) ? null : new OaidClient.Info(oaid);
        } catch (Throwable t) {
            logger.info(t.getMessage());
            return null;
        }
    }
}