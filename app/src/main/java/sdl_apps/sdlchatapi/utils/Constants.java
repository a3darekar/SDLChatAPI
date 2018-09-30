package sdl_apps.sdlchatapi.utils;

import sdl_apps.sdlchatapi.models.User;

public class Constants {

    public static String FCMToken = new String();
    public static User user = new User();

    public static String getFCMToken() {
        return FCMToken;
    }

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        Constants.user = user;
    }

    public static void setFCMToken(String FCMToken) {
        Constants.FCMToken = FCMToken;
    }

}
