package com.example.mentalhealthtracker;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class AdapterReadList extends RecyclerView.Adapter<AdapterReadList.MyViewHolder> {

    Context context;
    ArrayList<ModelReadList> modelReadLists;

    public AdapterReadList(Context context, ArrayList<ModelReadList> modelReadLists) {
        this.context = context;
        this.modelReadLists = modelReadLists;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.layout_read_list, parent, false);
        return new AdapterReadList.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ModelReadList model = modelReadLists.get(position);
        String name = model.Name;
        String id = model.UID;
        String url = model.URL;

        holder.ModelReadListName.setText(name);
        Glide.with(context)
                .load(url)
                .placeholder(R.drawable.profile)
                .into(holder.ModelReadDp);

        holder.viewMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent a = new Intent(context, Reads.class);
                a.putExtra("Name", name);
                a.putExtra("URL", url);
                a.putExtra("ID", id);
                context.startActivity(a);
            }
        });
    }

    @Override
    public int getItemCount() {
        return modelReadLists.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView ModelReadListName;
        ImageView ModelReadDp;
        Button viewMore;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ModelReadListName = itemView.findViewById(R.id.ModelReadListName);
            ModelReadDp = itemView.findViewById(R.id.readDp);
            viewMore = itemView.findViewById(R.id.viewMore);
        }
    }
}
