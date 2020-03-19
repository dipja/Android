package com.example.githubrepos;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {
    ArrayList<Repository> repos;
    Context context;

    public CustomAdapter(Context context, ArrayList<Repository> repos) {
        this.context=context;
        this.repos=repos;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View v = layoutInflater.inflate(R.layout.list_item_layout, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Repository repo = repos.get(position);
        holder.name.setText(repo.getName() + " by " + repo.getOwner().getLogin());
        Glide.with(holder.imgIcon.getContext()).load(repo.getOwner().getAvatarUrl()).into(holder.imgIcon);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(context, repo.getStargazersCount().toString(), Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(context, RepoDetails.class);
                intent.putExtra("ownerImageUrl", repo.getOwner().getAvatarUrl());
                intent.putExtra("repoName", repo.getName());
                intent.putExtra("ownerName", repo.getOwner().getLogin());
                intent.putExtra("starCount", repo.getStargazersCount());
                intent.putExtra("language", repo.getLanguage());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return repos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView imgIcon;
        public MyViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.textTitle);
            imgIcon = itemView.findViewById(R.id.imgIcon);
        }
    }
}
