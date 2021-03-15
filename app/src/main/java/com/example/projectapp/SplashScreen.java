package com.example.projectapp;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.projectapp.Retrofit.RetrofitClient;
import com.example.projectapp.Retrofit.WebService;
import com.example.projectapp.models.LoginResponse;
import com.example.projectapp.models.UserLoginRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashScreen extends AppCompatActivity {
    public static int time=3000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean connected = false;
//INTERNET CONNECTION MANAGER
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        //  Checking internet conectivity
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;

        }
        else
            connected = false;


        SharedPreferences sp;
        sp=getSharedPreferences("LoginDetail", Context.MODE_PRIVATE);
        if(connected==false){
            Toast.makeText(SplashScreen.this, "You Are Not Connected to Internet", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    finish();
                }
            }, time);

        }

//        Toast.makeText(SplashScreen.this, " "+email, Toast.LENGTH_SHORT).show();
//        Toast.makeText(SplashScreen.this, " "+password, Toast.LENGTH_SHORT).show();
      else if(connected == true&&sp.getString("Status","").equals("true") && sp.getString("Response","").equals("Success")){
          String email=sp.getString("Email","not Found");
        String password=sp.getString("password","not Found");
        RetrofitClient client=new RetrofitClient();
        WebService createdClient=client.RetrofitClientObject().create(WebService.class);
        UserLoginRequest LoginData=new UserLoginRequest(email,password);
        Call<LoginResponse> getResponse=createdClient.LogInRequest(LoginData);
        getResponse.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if(response.isSuccessful()){
                    LoginResponse l=response.body();
                    if(l.getStatus().toString().equals("Success")) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run()
                            {
                                Toast.makeText(SplashScreen.this, "You Are Already Login", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(SplashScreen.this,HomePage.class));

                                finish();
                            }
                        },time);

                    }
                    else
                        Toast.makeText(SplashScreen.this, "Check Your Id and Password", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(SplashScreen.this, ""+t, Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        }
else if(sp.getString("Status","").equals("false")){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(SplashScreen.this, Login.class));
                    finish();
                }
            }, time);
        }
else{
           new Handler().postDelayed(new Runnable() {
               @Override
               public void run() {
                   startActivity(new Intent(SplashScreen.this, Login.class));
                   finish();
               }
           }, time);
       }





}}
