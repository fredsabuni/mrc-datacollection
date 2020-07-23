package com.mrc.reports.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

/**
 * Created by fred on 06/09/2017.
 */

public class AccountUtils {

    //available active account user id
    public static final String PREFIX_PREF_ACTIVE_ACCOUNT = "active_account";
    public static final String PREFIX_PREF_TOKEN_SENT = "token_sent";
    public static final String PREFIX_PREF_ROLE = "role";
    public static final String PREFIX_PREF_USER = "user";
    public static final String PREFIX_PREF_PHONE = "phone";
    public static final String PREFIX_PREF_COLLERATION = "colleration";
    public static final String PREFERENCE = "com.mrc.reports";

    private static final String TAG = AccountUtils.class.getSimpleName();

    private static SharedPreferences getSharedPreference(Context context){
        return context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);
    }



    public static String getToken(Context context){
        return getSharedPreference(context).getString(PREFIX_PREF_TOKEN_SENT,null);
    }

    public static String getPhone(Context context){
        return getSharedPreference(context).getString(PREFIX_PREF_PHONE, null);
    }

    public static String getColleration(Context context){
        return getSharedPreference(context).getString(PREFIX_PREF_COLLERATION,null);
    }

    public static String getUser(Context context){
        return getSharedPreference(context).getString(PREFIX_PREF_USER , null);
    }


    /**
     * Returns true if no any active account available for this device
     * @param context
     * @return
     */
    public static boolean hasActiveUserAccount(Context context){
        return TextUtils.isEmpty(getToken(context))?false:true;
    }

    /**
     * Sets up a new application user
     * @param context
     * @param role wakala name(current app user)
     * @param token users access token, this is must be included in any query made to access
     *              remote server data.
     */
    public static void setupUser(Context context, String role, String token , String name , String phone){
        SharedPreferences preferences = getSharedPreference(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PREFIX_PREF_ROLE,role)
                .putString(PREFIX_PREF_TOKEN_SENT,token)
                .putString(PREFIX_PREF_USER , name)
                .putString(PREFIX_PREF_PHONE, phone)
                .commit();
    }


    public static void logoutUser(final Context context) {
        SharedPreferences preferences = getSharedPreference(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(PREFIX_PREF_ROLE)
                .remove(PREFIX_PREF_TOKEN_SENT)
                .remove(PREFIX_PREF_USER)
                .remove(PREFIX_PREF_PHONE)
                .commit();

    }

    public static void setColleration(Context context, String colleration){
        SharedPreferences preferences = getSharedPreference(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PREFIX_PREF_COLLERATION , colleration)
                .commit();
    }

}
