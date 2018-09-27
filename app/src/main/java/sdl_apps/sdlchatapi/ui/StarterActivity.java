package sdl_apps.sdlchatapi.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import sdl_apps.sdlchatapi.R;
import sdl_apps.sdlchatapi.managers.LoginManager;

public class StarterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_starter );
        checklogin();
    }

    private void checklogin() {
        try {
            Thread.sleep( 2000 );
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        LoginManager loginManager = new LoginManager(this);
        if (loginManager.isLoggedIn()) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        } else {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        }
        finish();
    }
}
