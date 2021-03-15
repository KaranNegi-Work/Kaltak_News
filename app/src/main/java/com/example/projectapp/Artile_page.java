package com.example.projectapp;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.projectapp.Retrofit.RetrofitClient;
import com.example.projectapp.Retrofit.WebService;
import com.example.projectapp.models.newsResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Artile_page extends AppCompatActivity {
    TextView newsTittle;
    TextView newsDescription;
    ImageView newsImage;

    // HomePage mainActivity;
//    int id;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artile_page);
        newsTittle = findViewById(R.id.newsTittle);
        newsImage = findViewById(R.id.newsImage);
        newsDescription = findViewById(R.id.newsDescription);
        progressBar = findViewById(R.id.progressbar);
        progressBar.setVisibility(View.VISIBLE);
        Bundle bundle = getIntent().getExtras();
        int id = bundle.getInt("id");
        RetrofitClient getclient = new RetrofitClient();
        WebService impClient = getclient.RetrofitClientObject().create(WebService.class);
        Call<newsResponse> postResponse = impClient.fullArticle(id);
        postResponse.enqueue(new Callback<newsResponse>() {
            @Override
            public void onResponse(Call<newsResponse> call, Response<newsResponse> response) {
                if (response.isSuccessful()) {
                    newsResponse fullarticle = response.body();
                    newsImage.setImageBitmap(stringtobitmap(fullarticle.getImage()));
                    newsTittle.setText(fullarticle.getDiscription());
                    newsDescription.setText(fullarticle.getFullInformation());
                    progressBar.setVisibility(View.GONE);
                } else {

                }
            }
            @Override
            public void onFailure(Call<newsResponse> call, Throwable t) {

            }
        });
    }
    private Bitmap stringtobitmap(String imagestring) {
        byte[] imageinbyte = Base64.decode(imagestring, Base64.DEFAULT);
        Bitmap decodeImage = BitmapFactory.decodeByteArray(imageinbyte, 0, imageinbyte.length);
        return decodeImage;
    }
}