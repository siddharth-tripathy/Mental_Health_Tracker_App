package com.example.mentalhealthtracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class AdapterPaymentHistory extends RecyclerView.Adapter<AdapterPaymentHistory.MyViewHolder> {

    Context context;
    ArrayList<ModelPayment> modelPayment;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public AdapterPaymentHistory(Context context, ArrayList<ModelPayment> modelPayment) {
        this.context = context;
        this.modelPayment = modelPayment;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.payment_history, parent, false);
        return new AdapterPaymentHistory.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ModelPayment model = modelPayment.get(position);

        String PaidTo = model.PaidTo;
        String PaymentDate = model.PaymentDate;
        String PaymentAmount = model.PaymentAmount;
        String PaymentStatus = model.PaymentStatus;

        if (PaymentStatus.equals("Failure"))
        {
            holder.st.setText("    FAILURE");
        }

        holder.amt.setText(PaymentAmount);
        holder.paymentDate.setText(PaymentDate);
        holder.paidTo.setText(PaidTo);
        //holder.amt.setText(PaymentAmount);
    }

    @Override
    public int getItemCount() {
        return modelPayment.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView amt, paymentDate, paidTo, st;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            amt = itemView.findViewById(R.id.amt);
            paymentDate = itemView.findViewById(R.id.paymentDate);
            paidTo = itemView.findViewById(R.id.paidTo);
            st = itemView.findViewById(R.id.st);
        }
    }
}
