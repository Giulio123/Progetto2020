package it.guaraldi.to_dotaskmanager.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public final class NetworkUtil {
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager mConnectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mActiveNetwork = mConnectivityManager.getActiveNetworkInfo();
        return mActiveNetwork != null && mActiveNetwork.isConnectedOrConnecting();
    }
}