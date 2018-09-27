package sdl_apps.sdlchatapi.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sdl_apps.sdlchatapi.R;
import sdl_apps.sdlchatapi.adapters.LeaveRecordsAdapter;
import sdl_apps.sdlchatapi.adapters.LeavesAdapter;
import sdl_apps.sdlchatapi.managers.LoginManager;
import sdl_apps.sdlchatapi.models.LeaveRecords;
import sdl_apps.sdlchatapi.models.Leaves;
import sdl_apps.sdlchatapi.models.User;
import sdl_apps.sdlchatapi.service_configs.ProgressDialogConfig;
import sdl_apps.sdlchatapi.service_configs.RetrofitServiceGenerator;
import sdl_apps.sdlchatapi.services.RestClient;
import sdl_apps.sdlchatapi.ui.ChatActivity;
import sdl_apps.sdlchatapi.ui.LoginActivity;
import sdl_apps.sdlchatapi.utils.Constants;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    int pk;
    LoginManager loginManager;
    private String TAG = getClass().getSimpleName();
    NavigationView navigationView;
    HashMap<String, String> userDetails;
    private RecyclerView recyclerView;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
        loginManager = new LoginManager( MainActivity.this );

        initSideBar();
        initUI(R.id.available);
    }

    private void initSideBar() {
        Toolbar toolbar = (Toolbar) findViewById( R.id.toolbar );
        setSupportActionBar( toolbar );

        FloatingActionButton chat = (FloatingActionButton) findViewById( R.id.start_chatting );
        chat.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,ChatActivity.class);
                startActivity(intent);
            }
        } );

        FloatingActionButton apply = (FloatingActionButton) findViewById( R.id.apply );
        apply.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make( navigationView, "Yet to Implement!", Snackbar.LENGTH_LONG ).setAction( "Action", null ).show();
            }
        } );

        DrawerLayout drawer = (DrawerLayout) findViewById( R.id.drawer_layout );
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close );
        drawer.addDrawerListener( toggle );
        toggle.syncState();

        navigationView = findViewById( R.id.nav_view );
        navigationView.setNavigationItemSelectedListener( this );
    }

    private void initUI(int id) {
        //fetch Logged in User
        loginManager.getUserDetails();
        userDetails = loginManager.getUserDetails();

        String token = "Token " + userDetails.get(loginManager.LOGIN_KEY);
        getUser(token, id);
        if (id == R.id.available) {
            getAvailable(token, Constants.user);
        } else if (id == R.id.pending) {
            getPending(token, Constants.user);
        } else if (id == R.id.history) {
            getHistory(token, Constants.user);
        } else if (id == R.id.nav_share) {
            //share();
        } else{
            Snackbar.make( navigationView, "Yet to Implement!", Snackbar.LENGTH_LONG ).setAction( "Action", null ).show();
        }

    }

    private void getUser(final String token, final int id) {
        final ProgressDialog progressDialog = ProgressDialogConfig.config(MainActivity.this, getString(R.string.logging_in));

        RestClient client = RetrofitServiceGenerator.createService(RestClient.class, token);
        progressDialog.show();

        final User[] user = new User[1];
        Call<User> call = client.getUser(token);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                progressDialog.dismiss();
                user[0] = response.body();
                TextView nameView = findViewById( R.id.name );
                TextView emailView = findViewById( R.id.email );
                nameView.setText( user[0].getName() );
                emailView.setText( user[0].getEmail() );
                pk = user[0].getPk();

                Constants.user.setPk(response.body().getPk());
                Constants.user.setEmail(response.body().getEmail());
                Constants.user.setFirst_name(response.body().getFirst_name());
                Constants.user.setUsername(response.body().getUsername());
                Constants.user.setKey(response.body().getKey());


                /*updateUI(user[0], leaves);*/
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(MainActivity.this, "Something Went Wrong! Please Try Again", Toast.LENGTH_SHORT).show();
                Log.d(TAG,t.getMessage(), t);
                loginManager.logOutUser();
            }
        });
    }

    private void getHistory(String token, final User user) {
        final ProgressDialog progressDialog = ProgressDialogConfig.config(MainActivity.this, getString(R.string.fetching_data));

        RestClient client = RetrofitServiceGenerator.createService(RestClient.class, token);
        progressDialog.show();

        final ArrayList<LeaveRecords> leaves = new ArrayList<>();
        Call<List<LeaveRecords>> call = client.getLeaveRecords(token);
        call.enqueue(new Callback<List<LeaveRecords>> () {
            @Override
            public void onResponse(Call<List<LeaveRecords>> call, Response<List<LeaveRecords>> response) {
                progressDialog.dismiss();
                try{
                    for(int i = 0; i < response.body().size(); i++){

                        LeaveRecords leave = (LeaveRecords) response.body().get(i);
                        leaves.add(leave);

                    }
                    if(progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                    recyclerView = findViewById(R.id.RecyclerView);
                    recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                    recyclerView.setAdapter(new LeaveRecordsAdapter(MainActivity.this, leaves, user.getFirst_name() + user.getLast_name()));
                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, getString(R.string.no_response), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<LeaveRecords>> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(MainActivity.this, "Something Went Wrong! Please Try Again", Toast.LENGTH_SHORT).show();
                Log.d(TAG,t.getMessage(), t);
                loginManager.logOutUser();
            }
        });
    }

    private void getPending(String token, final User user) {
        final ProgressDialog progressDialog = ProgressDialogConfig.config(MainActivity.this, getString(R.string.fetching_data));

        RestClient client = RetrofitServiceGenerator.config(RestClient.class);
        progressDialog.show();

        final ArrayList<LeaveRecords> leaves = new ArrayList<>();
        Call<List<LeaveRecords>> call = client.getPendingLeaves(token);
        call.enqueue(new Callback<List<LeaveRecords>> () {
            @Override
            public void onResponse(Call<List<LeaveRecords>> call, Response<List<LeaveRecords>> response) {
                progressDialog.dismiss();
                try{
                    for(int i = 0; i < response.body().size(); i++){

                        LeaveRecords leave = (LeaveRecords) response.body().get(i);
                        leaves.add(leave);

                    }
                    if(progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                    recyclerView = findViewById(R.id.RecyclerView);
                    recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                    recyclerView.setAdapter(new LeaveRecordsAdapter(MainActivity.this, leaves, user.getFirst_name() + user.getLast_name()));
                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, getString(R.string.no_response), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<LeaveRecords>> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(MainActivity.this, "Something Went Wrong! Please Try Again", Toast.LENGTH_SHORT).show();
                Log.d(TAG,t.getMessage(), t);
                loginManager.logOutUser();
            }
        });
    }

    private void getAvailable(String token, final User user) {
        final ProgressDialog progressDialog = ProgressDialogConfig.config(MainActivity.this, getString(R.string.fetching_data));

        RestClient client = RetrofitServiceGenerator.createService(RestClient.class, token);
        progressDialog.show();

        final ArrayList<Leaves> leaves = new ArrayList<>();
        Call<List<Leaves>> call = client.getLeaves(token);
        call.enqueue(new Callback<List<Leaves>> () {
            @Override
            public void onResponse(Call<List<Leaves>> call, Response<List<Leaves>> response) {
                progressDialog.dismiss();
                try{
                    for(int i = 0; i < response.body().size(); i++){

                        Leaves leave = (Leaves) response.body().get(i);
                        leaves.add(leave);

                    }
                    if(progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                    recyclerView = findViewById(R.id.RecyclerView);
                    recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                    recyclerView.setAdapter(new LeavesAdapter(MainActivity.this, leaves, user.getFirst_name() + user.getLast_name()));
                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, getString(R.string.no_response), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Leaves>> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(MainActivity.this, "Something Went Wrong! Please Try Again", Toast.LENGTH_SHORT).show();
                Log.d(TAG,t.getMessage(), t);
                loginManager.logOutUser();
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById( R.id.drawer_layout );
        if (drawer.isDrawerOpen( GravityCompat.START )) {
            drawer.closeDrawer( GravityCompat.START );
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate( R.menu.main, menu );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected( item );
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.logout) {
            loginManager.logOutUser();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity( intent );
        } else {
            initUI(id);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById( R.id.drawer_layout );
        drawer.closeDrawer( GravityCompat.START );
        return true;
    }
}