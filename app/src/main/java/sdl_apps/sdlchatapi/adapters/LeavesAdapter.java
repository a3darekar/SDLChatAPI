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
import sdl_apps.sdlchatapi.models.Leaves;
import sdl_apps.sdlchatapi.ui.MainActivity;

public class LeavesAdapter extends RecyclerView.Adapter<LeavesAdapter.ViewHolder> {

    private final Context context;
    private ArrayList<Leaves> leaves;

    public LeavesAdapter(Context context, ArrayList<Leaves> leaves, String s) {
        this.context = context;
        this.leaves = leaves;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.leaves_remain_row, parent,false);
        return new ViewHolder(view);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvType.setText(String.format("Type of Leave: %s", String.valueOf(leaves.get(position).getType())));
        holder.tvCount.setText(String.format("Available count: %d", leaves.get(position).getCount()));
    }

    @Override
    public int getItemCount() {
        return leaves.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvCount, tvType;
        public ViewHolder(View itemView) {
            super(itemView);

            tvCount = itemView.findViewById(R.id.tvCount);
            tvType = itemView.findViewById(R.id.tvType);
        }
    }
}
