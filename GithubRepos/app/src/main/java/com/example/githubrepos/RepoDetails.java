package com.example.githubrepos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

public class RepoDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repo_details);

        Intent intent = getIntent();

        String ownerImageUrl = intent.getStringExtra("ownerImageUrl");
        String repoName = intent.getStringExtra("repoName");
        String ownerName = intent.getStringExtra("ownerName");
        int starCount = intent.getIntExtra("starCount", 0);
        String language = intent.getStringExtra("language");

        ImageView imgOwner = findViewById(R.id.ownerImage);
        TextView textRepoName = findViewById(R.id.repoName);
        TextView textOwnerName = findViewById(R.id.ownerName);
        TextView textStarCount = findViewById(R.id.starCount);
        TextView textLanguage = findViewById(R.id.language);

        Toast.makeText(this, ownerName + " " + repoName, Toast.LENGTH_SHORT).show();

        Glide.with(this).load(ownerImageUrl).into(imgOwner);
        textRepoName.setText("Repo Name: " + repoName);
        textOwnerName.setText("Owner Name: " + ownerName);
        textStarCount.setText("Star count: " + starCount);
        textLanguage.setText("Language: " + language);

    }
}
