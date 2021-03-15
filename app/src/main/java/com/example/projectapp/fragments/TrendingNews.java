package com.example.projectapp.fragments;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectapp.Artile_page;
import com.example.projectapp.R;
import com.example.projectapp.RecyclerAdapter.onClickPost;
import com.example.projectapp.RecyclerAdapter.pAdapter;
import com.example.projectapp.Retrofit.RetrofitClient;
import com.example.projectapp.Retrofit.WebService;
import com.example.projectapp.models.newsResponse;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrendingNews extends Fragment implements onClickPost {
    ProgressBar pb;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home, container, false);
        pb = view.findViewById(R.id.progressbar);
        pb.setVisibility(View.VISIBLE);
        RecyclerView recyclerView = view.findViewById(R.id.homerecyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        RetrofitClient getclient = new RetrofitClient();
        WebService impClient = getclient.RetrofitClientObject().create(WebService.class);
        Call<List<newsResponse>> postResponse = impClient.getPost();
        postResponse.enqueue(new Callback<List<newsResponse>>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<List<newsResponse>> call, Response<List<newsResponse>> response) {
                if (response.isSuccessful()) {
                   Log.e("RESPONSE", " "+response.toString()+"\n" );
//                    Log.e("RESPONSE", " "+response.body()+"\n" );
                    List<newsResponse> posts = response.body();

                    List<newsResponse> sortedPosts = posts.stream().sorted(Comparator.comparingInt(newsResponse::getRate).reversed()).collect(Collectors.toList());
                    recyclerView.setAdapter(new pAdapter(sortedPosts, TrendingNews.this::postClicked));
                    pb.setVisibility(View.GONE);


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

        Toast.makeText(getContext(), ""+id, Toast.LENGTH_SHORT).show();
        Intent i=new Intent(getActivity(), Artile_page.class);
        i.putExtra("id",id);
        startActivity(i);

    }

}
