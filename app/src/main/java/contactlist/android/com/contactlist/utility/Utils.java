package contactlist.android.com.contactlist.utility;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * Created by jade on 9/4/17.
 */

public class Utils {

    /**
     * Check Internet connection is available or not.
     *
     * @param context is the {@link Context} of the {@link Activity}.
     * @return <b>true</b> is Internet connection is available.
     */
    public static boolean isInternetAvailable(Context context) {
        boolean isInternetAvailable = false;

        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager
                    .getActiveNetworkInfo();

            if (networkInfo != null && (networkInfo.isConnected())) {
                isInternetAvailable = true;
            }
        } catch (Exception exception) {
        }

        return isInternetAvailable;
    }
}
