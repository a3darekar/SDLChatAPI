package sdl_apps.sdlchatapi.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;

import sdl_apps.sdlchatapi.R;
import sdl_apps.sdlchatapi.managers.LoginManager;

public class StarterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_starter );
        FirebaseApp.initializeApp(this);
        checkLogin();
    }

    private void checkLogin() {
        LoginManager loginManager = new LoginManager(this);
        if (loginManager.isLoggedIn()) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        } else {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        }
        finish();
    }
}
