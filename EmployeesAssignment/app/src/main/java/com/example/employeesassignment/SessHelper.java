package com.example.employeesassignment;

import android.content.Context;
import android.content.SharedPreferences;

public class SessHelper {
    private static SharedPreferences SP;
    private static SharedPreferences.Editor editor;
    private static SessHelper instance;

    public SessHelper(Context context) {
        SP = context.getSharedPreferences("AppKey", 0);
        editor = SP.edit();
        editor.apply();
    }

    public synchronized static SessHelper getInstance(Context context) {
        if (instance == null) {
            instance = new SessHelper(context);
        }
        return instance;
    }
    public void login(String username, String email) {
        editor.putString("username", username);
        editor.putString("email", email);
        editor.apply();
    }

    private String getData(String key) {
        String s = SP.getString(key, null);
        return s;
    }

    public String getUsername() {
        return getData("username");
    }

    public String getEmail() {
        return getData("email");
    }

    public void logout() {
        editor.putString("username", "");
        editor.putString("email", "");
    }
}
