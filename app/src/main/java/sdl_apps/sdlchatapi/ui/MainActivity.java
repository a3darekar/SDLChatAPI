package sdl_apps.sdlchatapi.ui;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
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
    private AlertDialog alertDialog;
    EditText ReasonView, from, to;


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
                apply();
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

    private void apply() {
        final User user = Constants.user;
        final String token = "Token " + userDetails.get(loginManager.LOGIN_KEY);


        final Calendar myCalendar = Calendar.getInstance();


        View view1 = LayoutInflater.from(MainActivity.this).inflate(R.layout.layout_apply, null);
        ReasonView = view1.findViewById(R.id.ReasonView);

        final String submit = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        from = (EditText) view1.findViewById(R.id.from);
        to = (EditText) view1.findViewById(R.id.to);

        from.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, monthOfYear);
                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        from.setText( year + "-" + monthOfYear + "-" + dayOfMonth );
                    }

                };
                new DatePickerDialog(MainActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        to.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, monthOfYear);
                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        to.setText( year + "-" + monthOfYear + "-" + dayOfMonth );
                    }

                };
                new DatePickerDialog(MainActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            if (view1.getParent() != null) {
                ((ViewGroup) view1.getParent()).removeView(view1);
            }
            builder.setView(view1);
            builder.setPositiveButton(getString(R.string.apply), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    {
                        if (ReasonView.getText().toString().isEmpty()) {
                            ReasonView.setError( getString( R.string.cannot_be_empty ) );
                        } else if (from.toString().isEmpty()) {

                        } else if (to.toString().isEmpty()) {

                        } else {
                            RequestBody reason = RequestBody.create( MediaType.parse("text/plain"), ReasonView.getText().toString());
                            RequestBody from_date = RequestBody.create(MediaType.parse("text/plain"), from.getText().toString());
                            RequestBody to_date = RequestBody.create(MediaType.parse("text/plain"), to.getText().toString());
                            RequestBody submit_date = RequestBody.create(MediaType.parse("text/plain"), submit );
                            RequestBody pk = RequestBody.create(MediaType.parse("text/plain"), String.valueOf( user.getPk() ) );

                            RestClient client = RetrofitServiceGenerator.config(RestClient.class);
                            Call<LeaveRecords> call = client.applyLeave(token, reason, from_date, to_date, submit_date, pk);

                            call.enqueue( new Callback<LeaveRecords>() {
                                @Override
                                public void onResponse(Call<LeaveRecords> call, Response<LeaveRecords> response) {
                                    Toast.makeText( MainActivity.this, R.string.apply_success, Toast.LENGTH_SHORT ).show();
                                    initUI( R.id.available );
                                }

                                @Override
                                public void onFailure(Call<LeaveRecords> call, Throwable t) {

                                }
                            } );
                        }
                    }
                }
            });

            builder.setCancelable(false);
            builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    ReasonView.setText("");
                }
            });

            alertDialog = builder.create();
            alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.dismiss),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ReasonView.setText("");
                            if (alertDialog.isShowing()) {
                                alertDialog.dismiss();
                            }
                        }
                    });
            if (!alertDialog.isShowing()) {
                alertDialog.show();
            }
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