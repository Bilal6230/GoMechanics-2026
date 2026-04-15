package com.example.gomechanics;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class neworderadapter extends RecyclerView.Adapter<neworderadapter.neworderViewHolder> {

    private Context context;
    private ArrayList<neworderslist> list;

    public neworderadapter(Context context, ArrayList<neworderslist> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public neworderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.neworderlist, parent, false);
        return new neworderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull neworderViewHolder holder, int position) {
        neworderslist order = list.get(position);

        holder.tvOrderAddress.setText(order.getOrderaddress());
        holder.tvOrderContact.setText(order.getOrdercontact());
        holder.tvOrderBudget.setText(order.getOrderbudget());
        holder.tvOrderIssues.setText(order.getOrderissues());
        holder.btnViewDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Mechaincs_Order_Details.class);
                intent.putExtra("orderAddress", order.getOrderaddress());
                intent.putExtra("orderContact", order.getOrdercontact());
                intent.putExtra("orderBudget", order.getOrderbudget());
                intent.putExtra("orderIssues", order.getOrderissues());
                intent.putExtra("mcontact", order.getContact());
                context.startActivity(intent);


            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class neworderViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderAddress, tvOrderContact, tvOrderBudget, tvOrderIssues , contact;
        Button btnViewDetails;

        public neworderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderAddress = itemView.findViewById(R.id.tvorderaddress);
            tvOrderContact = itemView.findViewById(R.id.tvordercontact);
            tvOrderBudget = itemView.findViewById(R.id.tvorderbudget);
            tvOrderIssues = itemView.findViewById(R.id.tvorderissues);
            btnViewDetails = itemView.findViewById(R.id.btnviewdetails);
        }
    }
}
