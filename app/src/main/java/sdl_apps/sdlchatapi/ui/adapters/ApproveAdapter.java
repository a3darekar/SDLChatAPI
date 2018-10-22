package sdl_apps.sdlchatapi.ui.adapters;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sdl_apps.sdlchatapi.R;
import sdl_apps.sdlchatapi.managers.LoginManager;
import sdl_apps.sdlchatapi.models.LeaveRecords;
import sdl_apps.sdlchatapi.services.service_configs.ProgressDialogConfig;
import sdl_apps.sdlchatapi.services.service_configs.RetrofitServiceGenerator;
import sdl_apps.sdlchatapi.services.RestClient;

public class ApproveAdapter extends RecyclerView.Adapter<ApproveAdapter.ViewHolder> {

    private Context context;
    private ArrayList<LeaveRecords> leaverecords ;

    public ApproveAdapter(Context context, ArrayList<LeaveRecords> leaverecords , String s) {
        this.context = context;
        this.leaverecords = leaverecords ;
    }

    @Override
    public ApproveAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate( R.layout.leaves_approve_row, parent,false);
        return new ApproveAdapter.ViewHolder(view);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(final ApproveAdapter.ViewHolder holder, final int position) {
        holder.reason.setText("Reason: " + leaverecords.get(position).getReason());
        holder.status.setText("Status: " + leaverecords.get(position).getStatus());
        String status = leaverecords.get(position).getStatus();
        if (status.equals("Pending")) {
            holder.status.setBackgroundResource(R.drawable.eclipse_drawable_warning);

        } else if (status.equals("Approved")) {
            holder.status.setBackgroundResource(R.drawable.eclipse_drawable_success);
        } else {
            holder.status.setBackgroundResource(R.drawable.eclipse_drawable_error);
        }
        holder.tvDate.setText("Submitted Date " + String.valueOf(leaverecords.get(position).getPk()));
        holder.from.setText(" From Date " + String.valueOf(leaverecords.get(position).getFrom_date()));
        holder.to.setText("To Date " + String.valueOf(leaverecords.get(position).getTo_date()));

        holder.approve.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                leaverecords.get(position).setStatus( "approve" );
                LoginManager loginManager = new LoginManager( context );
            HashMap<String, String> userDetails;
            userDetails = loginManager.getUserDetails();
            String token = userDetails.get(loginManager.LOGIN_KEY);
            final ProgressDialog progressDialog = ProgressDialogConfig.config(context, context.getString(R.string.logging_in));

            RestClient client = RetrofitServiceGenerator.config(RestClient.class);
            progressDialog.show();

            Call<LeaveRecords> call = client.approveLeave(leaverecords.get(position), leaverecords.get(position).getPk());
            call.enqueue(new Callback<LeaveRecords>() {
                @Override
                public void onResponse(Call<LeaveRecords> call, Response<LeaveRecords> response) {
                    progressDialog.dismiss();
                    if (response.code() == 200)
                        Toast.makeText( context , "Approved!", Snackbar.LENGTH_LONG ).show();
                    else
                        Toast.makeText( context , "Something Went Wrong! Please Try Again", Snackbar.LENGTH_LONG ).show();
                }

                @Override
                public void onFailure(Call<LeaveRecords> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(context, "Something Went Wrong! Please Try Again", Toast.LENGTH_SHORT).show();
                }
            });

            }
        } );

        holder.disapprove.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                leaverecords.get(position).setStatus( "approve" );
                LoginManager loginManager = new LoginManager( context );
                HashMap<String, String> userDetails;
                userDetails = loginManager.getUserDetails();
                String token = userDetails.get(loginManager.LOGIN_KEY);
                final ProgressDialog progressDialog = ProgressDialogConfig.config(context, context.getString(R.string.logging_in));

                RestClient client = RetrofitServiceGenerator.config(RestClient.class);
                progressDialog.show();

                Call<LeaveRecords> call = client.approveLeave(leaverecords.get(position), leaverecords.get(position).getPk());
                call.enqueue(new Callback<LeaveRecords>() {
                    @Override
                    public void onResponse(Call<LeaveRecords> call, Response<LeaveRecords> response) {
                        progressDialog.dismiss();
                        if (response.code() == 200)
                            Toast.makeText( context , "Approved!", Snackbar.LENGTH_LONG ).show();
                        else
                            Toast.makeText( context , "Something Went Wrong! Please Try Again", Snackbar.LENGTH_LONG ).show();
                    }

                    @Override
                    public void onFailure(Call<LeaveRecords> call, Throwable t) {
                        progressDialog.dismiss();
                        Toast.makeText(context, "Something Went Wrong! Please Try Again", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } );
    }

    @Override
    public int getItemCount() {
        return leaverecords.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView reason, status, tvDate, from, to;
        private Button approve, disapprove;
        public ViewHolder(View itemView) {
            super(itemView);
            reason = itemView.findViewById(R.id.tvReason);
            status = itemView.findViewById(R.id.tvStatus);
            tvDate = itemView.findViewById(R.id.tvdate);
            from = itemView.findViewById( R.id.from_date );
            approve = itemView.findViewById( R.id.approve);
            disapprove = itemView.findViewById( R.id.disapprove);

            to = itemView.findViewById( R.id.to_date );
        }
    }
}
