package com.example.memory_mate;
import android.content.Context;
import android.content.SharedPreferences;
public class SharedPreferencesHelper {
    private static final String PREF_NAME = "MyAppPreferences";
    private static final String KEY_USER_NAME = "UserName";

    public static void saveUserName(Context context, String userName) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USER_NAME, userName);
        editor.apply();
    }
    public static String getUserName(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USER_NAME, "");
    }
}
