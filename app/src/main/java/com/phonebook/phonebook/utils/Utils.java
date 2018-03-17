package com.phonebook.phonebook.utils;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.NotificationCompat;

import com.phonebook.phonebook.R;

/**
 * Created by nikola kosmajac on 13-Mar-18.
 */

public class Utils {

    /**
     * Get shared preferences from preference xml
     *
     */

    public static boolean isShowToast(Context c){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(c);
        return  sharedPref.getBoolean("pref_toasts", false);
    }


    /**
     * Show notifications option from prefs
     */

    public static boolean isShowNotifications(Context c){

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(c);
        return sharedPreferences.getBoolean("pref_notifications", false);
    }



    /**
     *
     * Notifications
     *
     */
    public static void messageNotification(Context context, String message, int messageID, int smallIconDrawable){

        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);

        Bitmap bm = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_message_black_24dp);

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        mBuilder.setSound(alarmSound);
        mBuilder.setSmallIcon(smallIconDrawable);
        mBuilder.setContentTitle("Message");
        mBuilder.setContentText(message);


        mBuilder.setLargeIcon(bm);
        mNotificationManager.notify(messageID, mBuilder.build());
    }
}
