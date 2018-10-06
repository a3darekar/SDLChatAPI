package sdl_apps.sdlchatapi.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import sdl_apps.sdlchatapi.R;
import sdl_apps.sdlchatapi.models.LeaveRecords;

public class LeaveRecordsAdapter extends RecyclerView.Adapter<LeaveRecordsAdapter.ViewHolder> {

    private Context context;
    private ArrayList<LeaveRecords> leaverecords ;

    public LeaveRecordsAdapter(Context context, ArrayList<LeaveRecords> leaverecords , String s) {
        this.context = context;
        this.leaverecords = leaverecords ;
    }

    @Override
    public LeaveRecordsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate( R.layout.leaves_history_row, parent,false);
        return new LeaveRecordsAdapter.ViewHolder(view);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(LeaveRecordsAdapter.ViewHolder holder, int position) {
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
    }

    @Override
    public int getItemCount() {
        return leaverecords.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView reason, status, tvDate, from, to;
        public ViewHolder(View itemView) {
            super(itemView);

            reason = itemView.findViewById(R.id.tvReason);
            status = itemView.findViewById(R.id.tvStatus);
            tvDate = itemView.findViewById(R.id.tvdate);
            from = itemView.findViewById( R.id.from_date );
            to = itemView.findViewById( R.id.todate );
        }
    }
}
