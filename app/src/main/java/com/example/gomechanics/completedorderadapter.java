package com.example.gomechanics;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class completedorderadapter extends RecyclerView.Adapter<completedorderadapter.completedorderViewHolder> {

    private Context context;
    private DatabaseReference currentOrdersRef;
    private ArrayList<completedorderlist> list;

    public completedorderadapter(Context context, ArrayList<completedorderlist> list) {
        this.context = context;
        this.list = list;
        currentOrdersRef = FirebaseDatabase.getInstance().getReference("CurrentOrders");

    }

    @NonNull
    @Override
    public completedorderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.completedorderlist, parent, false);
        return new completedorderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull completedorderViewHolder holder, int position) {
        completedorderlist order = list.get(position);

        holder.tvOrderAddress.setText(order.getAddress());
        holder.tvOrderContact.setText(order.getContact());
        holder.tvOrderBudget.setText(order.getMax());
        holder.tvOrderIssues.setText(order.getVehicalIssues());

        holder.btnjobcompleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int clickedPosition = holder.getAdapterPosition();
                completedorderlist clickedOrder = list.get(clickedPosition);

                // Delete the order from CurrentOrders table
                currentOrdersRef.child(clickedOrder.getContact()).removeValue();
                Toast.makeText(context, "Congratulations", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class completedorderViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderAddress, tvOrderContact, tvOrderBudget, tvOrderIssues;
        Button btnjobcompleted;

        public completedorderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderAddress = itemView.findViewById(R.id.tvcompletedorderaddress);
            tvOrderContact = itemView.findViewById(R.id.tvcompletedordercontact);
            tvOrderBudget = itemView.findViewById(R.id.tvcompletedorderbudget);
            tvOrderIssues = itemView.findViewById(R.id.tvcompletedorderissues);
            btnjobcompleted = itemView.findViewById(R.id.btnjobcompleted);
        }
    }
}
