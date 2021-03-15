package com.example.projectapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectapp.Artile_page;
import com.example.projectapp.R;
import com.example.projectapp.RecyclerAdapter.onClickPost;
import com.example.projectapp.RecyclerAdapter.postAdapter;
import com.example.projectapp.Retrofit.RetrofitClient;
import com.example.projectapp.Retrofit.WebService;
import com.example.projectapp.models.newsResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewFeeds extends Fragment implements onClickPost {
    ProgressBar progressBar;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_feeds, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.articleRecyclerView);
       progressBar = view.findViewById(R.id.progressbar1);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        //these two are for reversing the list
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        //getMethod Call
        RetrofitClient getclient = new RetrofitClient();
        WebService impClient = getclient.RetrofitClientObject().create(WebService.class);
        Call<List<newsResponse>> postResponse = impClient.getPost();
        postResponse.enqueue(new Callback<List<newsResponse>>() {
            @Override
            public void onResponse(Call<List<newsResponse>> call, Response<List<newsResponse>> response) {
                if (response.isSuccessful()) {
                    List<newsResponse> posts = response.body();
                   progressBar.setVisibility(view.GONE);
                   recyclerView.setAdapter(new postAdapter(posts, NewFeeds.this));

                }
            }

            @Override
            public void onFailure(Call<List<newsResponse>> call, Throwable t) {

            }
        });

        return view;

    }
    @Override
    public void postClicked(int id) {
        Intent i=new Intent(getActivity(), Artile_page.class);
        i.putExtra("id",id);
        startActivity(i);

    }
}
