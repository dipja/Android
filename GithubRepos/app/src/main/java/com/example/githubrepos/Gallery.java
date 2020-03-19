package com.example.githubrepos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Gallery extends AppCompatActivity {

    public ArrayList<Repository> sortedRepositories = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        final RecyclerView recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024 * 100); // 100MB cap
        Network network = new BasicNetwork(new HurlStack());
        final RequestQueue queue = new RequestQueue(cache, network);
        queue.start();

        String url ="https://api.github.com/repositories";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            ObjectMapper mapper = new ObjectMapper();
                            Repository[] RepositoryArr = null;
                            RepositoryArr = mapper.readValue(response, Repository[].class);
                            List<Repository> repositoryList = Arrays.asList(RepositoryArr);

                            int size = repositoryList.size();

//                            repositoryList.parallelStream().forEach(repo -> {
//                                getStarDetails(recyclerView, queue, repo);
//                            });

                            for(int i=0;i<size;i++)
                                getStarDetails(recyclerView, queue, repositoryList.get(i), i, size-1);

                        } catch (JsonParseException e) {
                            e.printStackTrace();
                        } catch (JsonMappingException e) {
                            Toast.makeText(Gallery.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Gallery.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        queue.add(stringRequest);
    }

    private void getStarDetails(final RecyclerView recyclerView, RequestQueue queue, final Repository repository, final int position, final int size) {
        String url = "https://api.github.com/repos/" + repository.getFullName();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        ObjectMapper objectMapper = new ObjectMapper();
                        try {
                            Repository repositoryWithStarDetails = objectMapper.readValue(response, Repository.class);
                            sortedRepositories.add(repositoryWithStarDetails);
                            if(position == size)
                            {
                                Collections.sort(sortedRepositories, new Comparator<Repository>() {
                                    @Override
                                    public int compare(Repository o1, Repository o2) {
                                        if(o1.getStargazersCount()==null && o2.getStargazersCount()==null)
                                            return -1;
                                        else if(o1.getStargazersCount()==null)
                                            return 1;
                                        else if(o2.getStargazersCount()==null)
                                            return -1;

                                        return o2.getStargazersCount()-o1.getStargazersCount();
                                    }
                                });
                                CustomAdapter customAdapter = new CustomAdapter(Gallery.this, sortedRepositories);
                                recyclerView.setAdapter(customAdapter);
                            }
                            Log.d("sorted repositories with count if stars", sortedRepositories.get(0).getStargazersCount().toString());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        queue.add(stringRequest);
    }
}
