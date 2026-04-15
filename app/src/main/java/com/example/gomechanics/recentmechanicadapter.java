package com.example.gomechanics;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class recentmechanicadapter extends RecyclerView.Adapter<recentmechanicadapter.recentmechanicViewHolder> {
    private Context context;
    private ArrayList<recentmechaniclist> list;

    public recentmechanicadapter(Context context, ArrayList<recentmechaniclist> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public recentmechanicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.recentmechaniclist, parent, false);
        return new recentmechanicViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull recentmechanicViewHolder holder, int position) {
        recentmechaniclist mechanic = list.get(position);

        holder.tvRecentMname.setText(mechanic.getRMname());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class recentmechanicViewHolder extends RecyclerView.ViewHolder {
        TextView tvRecentMname;

        public recentmechanicViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRecentMname = itemView.findViewById(R.id.tvRecentMname);
        }
    }
}
