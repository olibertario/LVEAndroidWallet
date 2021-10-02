package io.gonative.android;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.onesignal.OSNotification;
import com.onesignal.OSNotificationOpenedResult;
import com.onesignal.OneSignal;

import org.json.JSONObject;

/**
 * Created by weiyin on 2/10/16.
 */
public class OneSignalNotificationHandler implements OneSignal.OSNotificationOpenedHandler {
    private Context context;

    @SuppressWarnings("unused")
    public OneSignalNotificationHandler() {
        // default construct needed to be a broadcast receiver
    }

    OneSignalNotificationHandler(Context context) {
        this.context = context;
    }

    @Override
    public void notificationOpened(OSNotificationOpenedResult openedResult) {
        OSNotification notification = openedResult.getNotification();

        String launchUrl = notification.getLaunchURL();
        if (launchUrl != null && !launchUrl.isEmpty()) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(launchUrl));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(intent);
            return;
        }

        JSONObject additionalData = notification.getAdditionalData();

        String targetUrl = LeanUtils.optString(additionalData, "targetUrl");
        if (targetUrl == null) targetUrl = LeanUtils.optString(additionalData, "u");

        Intent mainIntent = new Intent(context, MainActivity.class);
        mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        if (targetUrl != null && !targetUrl.isEmpty()) {
            mainIntent.putExtra(MainActivity.INTENT_TARGET_URL, targetUrl);
        }

        context.startActivity(mainIntent);
    }
}
