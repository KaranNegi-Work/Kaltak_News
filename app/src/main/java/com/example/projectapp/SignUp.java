package com.example.projectapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.projectapp.Retrofit.RetrofitClient;
import com.example.projectapp.Retrofit.WebService;
import com.example.projectapp.models.DBResponceBack;
import com.example.projectapp.models.UserDataModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static androidx.core.content.ContextCompat.startActivity;

public class SignUp extends AppCompatActivity  {
    EditText name,email,password,phoneno;
    Button create;
ProgressBar sigupprogress;
    String BaseURL="http://10.0.2.2:8080/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//    View view = inflater.inflate(R.layout.activity_sign_up, container, false);
        setContentView(R.layout.activity_sign_up);
//        name= view.findViewById(R.id.name);
//        password= view.findViewById(R.id.password);
//        email= view.findViewById(R.id.email);
//        phoneno = view.findViewById(R.id.phoneNo);
//        return view;
    sigupprogress=findViewById(R.id.sigupprogress);
    name= findViewById(R.id.name);
    create=findViewById(R.id.createNewAccount);
    password= findViewById(R.id.password);
    email= findViewById(R.id.email);
    phoneno = findViewById(R.id.phoneNo);
        sigupprogress.setVisibility(View.GONE);
    }
    public void createNewAccount(View v){

        sigupprogress.setVisibility(View.VISIBLE);

String Name=name.getText().toString().trim();
String Email=email.getText().toString().trim();
String Password=password.getText().toString().trim();
String Phoneno=phoneno.getText().toString().trim();
        if (TextUtils.isEmpty(email.getText().toString())){
            email.setError("Please Enter Email");
            return;
        }
        if (TextUtils.isEmpty(password.getText().toString())){
            password.setError("Please Enter Password");
            return;
        }
        if (TextUtils.isEmpty(name.getText().toString())){
            name.setError("Please Enter Name");
            return;
        }
        if (TextUtils.isEmpty(phoneno.getText().toString())){
            phoneno.setError("Please Enter Phone No");
            return;
        }
        create.setEnabled(false);
        RetrofitClient client=new RetrofitClient();
        WebService createdClient=client.RetrofitClientObject().create(WebService.class);
        UserDataModel newUserData= new UserDataModel(Name,Email,Password,Phoneno);
        Call<DBResponceBack> getResponce=createdClient.CreateUser(newUserData);
        getResponce.enqueue(new Callback<DBResponceBack>() {
            @Override
            public void onResponse(Call<DBResponceBack> call, Response<DBResponceBack> response) {
                if(response.isSuccessful()){
//                startActivity(new Intent(getActivity(),HomePage.class));
                    sigupprogress.setVisibility(View.GONE);
                    startActivity(new Intent(SignUp.this, Login.class));
                    DBResponceBack dbResponceBack=response.body();
             //   Toast.makeText(SignUp.this, ""+dbResponceBack.getMessage()+" With Name "+dbResponceBack.getName(), Toast.LENGTH_SHORT).show();
            }
            else{
                    Toast.makeText(SignUp.this, "Your Email is Already Registered", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DBResponceBack> call, Throwable t) {
                Toast.makeText(SignUp.this, ""+t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}