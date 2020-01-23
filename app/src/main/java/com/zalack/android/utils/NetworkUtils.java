package com.zalack.android.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.zalack.android.R;

public class NetworkUtils {

    private static int TYPE_WIFI = 1;
    private static int TYPE_MOBILE = 2;
    private static int TYPE_NOT_CONNECTED = 0;


    private static int getConnectivityStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm != null) {
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            if (null != activeNetwork) {
                if (activeNetwork.isConnectedOrConnecting() && (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI))
                    return TYPE_WIFI;

                if (activeNetwork.isConnectedOrConnecting() && (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE))
                    return TYPE_MOBILE;
            }
        }
        return TYPE_NOT_CONNECTED;
    }

    public static String getConnectivityStatusString(Context context) {
        int conn = NetworkUtils.getConnectivityStatus(context);
        String status = null;
        if (conn == NetworkUtils.TYPE_WIFI) {
            status = context.getString(R.string.connected);
        } else if (conn == NetworkUtils.TYPE_MOBILE) {
            status = context.getString(R.string.connected);
        } else if (conn == NetworkUtils.TYPE_NOT_CONNECTED) {
            status = context.getString(R.string.not_connected);
        }
        return status;
    }

}
