package sdl_apps.sdlchatapi.ui.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import sdl_apps.sdlchatapi.R;
import sdl_apps.sdlchatapi.models.LeaveRecords;

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
    public void onBindViewHolder(ApproveAdapter.ViewHolder holder, int position) {
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
        holder.tvDate.setText("Submitted Date " + String.valueOf(leaverecords.get(position).getSubmit_date()));
        holder.from.setText(" From Date " + String.valueOf(leaverecords.get(position).getFrom_date()));
        holder.to.setText("To Date " + String.valueOf(leaverecords.get(position).getTo_date()));

        holder.approve.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText( context , "Approved!", Snackbar.LENGTH_LONG ).show();
            }
        } );

        holder.disapprove.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText( context , "Disapproved!", Snackbar.LENGTH_LONG ).show();
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
