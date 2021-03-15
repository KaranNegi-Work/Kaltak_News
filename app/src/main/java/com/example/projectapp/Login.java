package com.example.projectapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projectapp.Retrofit.RetrofitClient;
import com.example.projectapp.Retrofit.WebService;
import com.example.projectapp.models.LoginResponse;
import com.example.projectapp.models.UserLoginRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity {
    EditText email, password;
    Button logIn;
    TextView newUser;
  RelativeLayout r;
    CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        email = findViewById(R.id.name);
        password = findViewById(R.id.password);
        logIn = findViewById(R.id.login);
        newUser = findViewById(R.id.createNewAccount);
        checkBox = findViewById(R.id.checkbox);

        SharedPreferences sp;
        sp = getSharedPreferences("LoginDetail", Context.MODE_PRIVATE);
        SharedPreferences.Editor check = sp.edit();
        //For Profile info
        SharedPreferences profileinfo;
        profileinfo=getSharedPreferences("profile",MODE_PRIVATE);
        SharedPreferences.Editor edit = profileinfo.edit();
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (checkBox.isChecked()) {
                    check.putString("Email", "" + email.getText().toString());
                    check.putString("password", "" + password.getText().toString());
                    check.putString("Status", "true");
                    check.commit();

                } else if (!checkBox.isChecked()) {
                    check.putString("Status", "false");
                    check.commit();
                }
            }
        });


    }

    public void createUser(View v) {

        startActivity(new Intent(Login.this, SignUp.class));
    }

    public void LogIn(View v) {


        if (TextUtils.isEmpty(email.getText().toString())) {
            email.setError("Please Enter Email");
            return;
        }
        if (TextUtils.isEmpty(password.getText().toString())) {
            password.setError("Please Enter Password");
            return;
        }
        SharedPreferences sp;
        sp = getSharedPreferences("LoginDetail", Context.MODE_PRIVATE);
        SharedPreferences.Editor check = sp.edit();
        //For Profile info
        SharedPreferences profileinfo;
        profileinfo=getSharedPreferences("profile",MODE_PRIVATE);
        SharedPreferences.Editor edit = profileinfo.edit();

//        SharedPreferences sp;
//        sp=getSharedPreferences("LoginDetail", Context.MODE_PRIVATE);
//        SharedPreferences.Editor check=sp.edit();
        //First Stet is Make a RetrofitClient and Buld That in this Class or U can Make Seprate Call for That
        //2nd create Interface For Your Get Request Or For Post Request Whatever Your May required
        //Make A client
        RetrofitClient client = new RetrofitClient();
        //Provide the Implements of the interface using Client
        WebService createdClient = client.RetrofitClientObject().create(WebService.class);
        //Making RequestBody
        UserLoginRequest LoginData = new UserLoginRequest(email.getText().toString().trim(), password.getText().toString().trim());
        //Sending RequestBody tp the Post method in Interface
        Call<LoginResponse> getResponse = createdClient.LogInRequest(LoginData);
        //Now Last Step calling CallBack Method
        getResponse.enqueue(new Callback<LoginResponse>() {

            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    LoginResponse l = response.body();
                    if (l.getStatus().equals("Success")) {
                        //For Profile details
                        edit.putString("email",  email.getText().toString().trim());
                        edit.putString("name", l.getName());
                        edit.commit();
                       //String s= profileinfo.getString("email","hello");
                        check.putString("Response", l.getStatus()); //Puting RESPONSE into Sharedpreferences
                        check.commit();
                        String hamail = sp.getString("Response", "did not Get");
                        Toast.makeText(Login.this, "Login Successfull " + hamail, Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Login.this, HomePage.class));
                        finish();
                    } else {
                        checkBox.setChecked(false);
                        Toast.makeText(Login.this, "Check Your Id and Password", Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(Login.this, "" + t, Toast.LENGTH_SHORT).show();

            }
        });
    }
}