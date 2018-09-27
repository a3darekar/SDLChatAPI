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
import sdl_apps.sdlchatapi.models.Leaves;
import sdl_apps.sdlchatapi.ui.MainActivity;

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
        holder.tvType.setText(String.format("Type of Leave: " + leaverecords.get(position).getReason()));
        holder.tvCount.setText(String.format("Available count: " + leaverecords.get(position).getStatus()));
        holder.tvDate.setText("Date " + String.valueOf(leaverecords.get(position).getDays()));
    }

    @Override
    public int getItemCount() {
        return leaverecords.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvCount, tvType, tvDate;
        public ViewHolder(View itemView) {
            super(itemView);

            tvCount = itemView.findViewById(R.id.tvReason);
            tvType = itemView.findViewById(R.id.tvStatus);
            tvDate = itemView.findViewById(R.id.tvdate);
        }
    }
}
