package sdl_apps.sdlchatapi.managers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;

import sdl_apps.sdlchatapi.models.User;
import sdl_apps.sdlchatapi.ui.LoginActivity;

public class LoginManager {
    public String LOGIN_PK = "pk";
    public String LOGIN_EMAIL = "email";
    public String LOGIN_KEY = "key";
    public String LOGIN_USERNAME = "username";
    public String LOGIN_PASSWORD = "password";
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    Context context;
    String PREFERENCE_NAME = "Login Preference";
    String IS_LOGGED_IN = "IsLoggedIn";
    public String FCM_KEY = "fcm_key";

    public LoginManager(Context ctx) {
        this.context = ctx;
        preferences = context.getSharedPreferences(PREFERENCE_NAME, 0);
        editor = preferences.edit();
    }

    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<>();
        user.put(LOGIN_EMAIL, preferences.getString(LOGIN_EMAIL, null));
        user.put(LOGIN_KEY, preferences.getString(LOGIN_KEY, null));
        user.put(FCM_KEY, preferences.getString(FCM_KEY, null));
        return user;
    }

    public void setFCM_KEY(String key) {
        editor.putString(FCM_KEY, key);
        editor.commit();
    }

    public boolean isLoggedIn() {
        return preferences.getBoolean(IS_LOGGED_IN, false);
    }

    public void logOutUser() {
        editor.clear();
        editor.commit();
        Intent i = new Intent(context, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }

    public void createLoginSession(String email, String key, int pk, String username) {
        editor.putBoolean(IS_LOGGED_IN, true);
        editor.putString(LOGIN_EMAIL, email);
        editor.putString(LOGIN_KEY, key);
        editor.putInt(LOGIN_PK, pk);
        editor.putString(LOGIN_USERNAME, username);
        editor.commit();
    }

    public void createLoginSession(User user) {
        editor.putBoolean(IS_LOGGED_IN, true);
        editor.putString(LOGIN_EMAIL, user.getEmail());
        editor.putString(LOGIN_KEY, user.getKey());
        editor.putInt(LOGIN_PK, user.getPk());
        editor.putString(LOGIN_USERNAME, user.getUsername());
        editor.commit();
    }
}
