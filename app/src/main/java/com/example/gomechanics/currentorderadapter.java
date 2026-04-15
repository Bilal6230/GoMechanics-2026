package com.example.gomechanics;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class currentorderadapter extends RecyclerView.Adapter<currentorderadapter.currentorderViewHolder> {

    private Context context;
    private ArrayList<currentorderlist> list;
    private DatabaseReference currentOrdersRef;
    private DatabaseReference completedRecordRef;

    public currentorderadapter(Context context, ArrayList<currentorderlist> list) {
        this.context = context;
        this.list = list;
        currentOrdersRef = FirebaseDatabase.getInstance().getReference("CurrentOrders");
        completedRecordRef = FirebaseDatabase.getInstance().getReference("Completed_Record");
    }

    @NonNull
    @Override
    public currentorderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.currentorderlist, parent, false);
        return new currentorderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull currentorderViewHolder holder, int position) {
        currentorderlist order = list.get(position);

        holder.tvOrderAddress.setText(order.getAddress());
        holder.tvOrderContact.setText(order.getContact());
        holder.tvOrderBudget.setText(order.getMax());
        holder.tvOrderIssues.setText(order.getVehicalIssues());

        holder.btnjobdone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int clickedPosition = holder.getAdapterPosition();
                currentorderlist clickedOrder = list.get(clickedPosition);

                String contact = clickedOrder.getContact();

                // Delete the order from CurrentOrders table
                currentOrdersRef.child(contact).removeValue()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("DeleteSuccess", "Order deleted from CurrentOrders table");

                                // Create a new entry in Completed_Record table
                                completedRecordRef.child(contact).setValue(clickedOrder)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d("CreateSuccess", "Order created in Completed_Record table");

                                                // Start the Completed_Orders activity
                                                Intent intent = new Intent(context, Completed_Orders.class);
                                                context.startActivity(intent);
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.e("CreateFailure", "Failed to create order in Completed_Record table", e);
                                            }
                                        });
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e("DeleteFailure", "Failed to delete order from CurrentOrders table", e);
                            }
                        });
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class currentorderViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderAddress, tvOrderContact, tvOrderBudget, tvOrderIssues;
        Button btnjobdone;

        public currentorderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderAddress = itemView.findViewById(R.id.tvcurrentorderaddress);
            tvOrderContact = itemView.findViewById(R.id.tvcurrentordercontact);
            tvOrderBudget = itemView.findViewById(R.id.tvcurrentorderbudget);
            tvOrderIssues = itemView.findViewById(R.id.tvcurrentorderissues);
            btnjobdone = itemView.findViewById(R.id.tvjobdone);
        }
    }
}
