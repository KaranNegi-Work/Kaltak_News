package com.example.projectapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projectapp.Retrofit.RetrofitClient;
import com.example.projectapp.Retrofit.WebService;
import com.example.projectapp.fragments.TrendingNews;
import com.example.projectapp.fragments.NewFeeds;
import com.example.projectapp.fragments.NewsUpload;
import com.example.projectapp.models.ProfilePhotoRequest;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomePage extends AppCompatActivity {

    TextView textView;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ImageView iconPhoto;
    private Bitmap bitmap;
    TextView iconName;
    TextView iconEmail;
    String email;
    String name;
    String m;

    //
//    @Override public void onBackPressed() {
//        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frame);
//        if (!(fragment instanceof IOnBackPressed) || !((IOnBackPressed) fragment).onBackPressed()) {
//            super.onBackPressed();
//        }
//    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        navigationView = findViewById(R.id.sideNavigaration);
        drawerLayout = findViewById(R.id.drawer);
        textView = findViewById(R.id.text);
        //For Profile info
        SharedPreferences profileinfo;
        profileinfo = getSharedPreferences("profile", MODE_PRIVATE);
        SharedPreferences.Editor edit = profileinfo.edit();
        //when we are working with ittrated view then we have to get that view from our parent view then we can work on that child view
        View header = navigationView.getHeaderView(0);
        iconPhoto = header.findViewById(R.id.iconphoto);
        iconName = header.findViewById(R.id.iconName);
        iconEmail = header.findViewById(R.id.iconEmail);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        //this is bottom nav bar listner so that we can check which item is clicked
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame, new TrendingNews()).commit();
        //this is for the hiding the bottom naav bar when ever we scroll our recycler view or page
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) bottomNavigationView.getLayoutParams();
        layoutParams.setBehavior(new BottomNavigationViewBehavior());
        email = profileinfo.getString("email", "no data");
        name = profileinfo.getString("name", "no data");
        iconName.setText(name);
        iconEmail.setText(email);



      //CHECK which item is clicked in side nav bar
        navigationView.setNavigationItemSelectedListener(item -> {

            switch (item.getItemId()) {

                case R.id.h:
                    item.setChecked(true);
                    RetrofitClient client2 = new RetrofitClient();
                    WebService s = client2.RetrofitClientObject().create(WebService.class);
                    Call<ProfilePhotoRequest> k = s.fullprofile(email);
                    k.enqueue(new Callback<ProfilePhotoRequest>() {
                        @Override
                        public void onResponse(Call<ProfilePhotoRequest> call, Response<ProfilePhotoRequest> response) {
                            if (response.isSuccessful()) {
                               // iconPhoto.setImageBitmap(stringtobitmap(response.toString()));

                                drawerLayout.closeDrawers();

                            } else {
                                drawerLayout.closeDrawers();
                                Toast.makeText(HomePage.this, "" + response.body(), Toast.LENGTH_SHORT).show();

                            }
                        }

                        @Override
                        public void onFailure(Call<ProfilePhotoRequest> call, Throwable t) {
                            drawerLayout.closeDrawers();
                            Log.e("error",""+t);
                            Toast.makeText(HomePage.this, "" + t, Toast.LENGTH_SHORT).show();
                        }
                    });


                    return true;
                case R.id.m:
                    item.setChecked(true);
                    drawerLayout.closeDrawers();
                    return true;
                case R.id.songs:
                    item.setChecked(true);
                    drawerLayout.closeDrawers();
                    return true;
                case R.id.message:
                    item.setChecked(true);
                    drawerLayout.closeDrawers();
                    return true;
                case R.id.logout:
                    item.setChecked(true);
                    drawerLayout.closeDrawers();
                    SharedPreferences sp;
                    sp = getSharedPreferences("LoginDetail", Context.MODE_PRIVATE);
                    SharedPreferences.Editor check = sp.edit();
                    check.putString("Status", "false");
                    check.commit();
                    profileinfo.edit().clear().commit();
                    startActivity(new Intent(HomePage.this, Login.class));
                    return true;


            }
            return false;

        });
    }

    //CHECK which item is clicked in bottom nav bar
    //OUT OF THE ONCREATE

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.home:
                    selectedFragment = new TrendingNews();
                    break;
                case R.id.message:
                    selectedFragment = new NewFeeds();
                    break;
                case R.id.radio:
                    selectedFragment = new NewsUpload();
                    break;

                default:
                    throw new IllegalStateException("Unexpected value: " + item.getItemId());

            }
            getSupportFragmentManager().beginTransaction().replace(R.id.frame, selectedFragment).commit();
            return true;

        }
    };

    //photo fetching and Uploading process
    public void iconPhoto(View view) {
        getimage();
    }

    private void getimage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 1);
        Toast.makeText(this, "getimage " + intent, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Toast.makeText(this, "onActivityResult", Toast.LENGTH_SHORT).show();

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            Uri path = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getBaseContext().getContentResolver(), path);
                iconPhoto.setImageBitmap(bitmap);
                iconPhoto.setVisibility(View.VISIBLE);
                String imagebitmap = imagetoString();
                uploadimage(imagebitmap);
                Toast.makeText(this, "DONE " + bitmap, Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "" + e, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String imagetoString() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imgByte = byteArrayOutputStream.toByteArray();
        //Toast.makeText(getContext(), ""+Base64.encodeToString(imgByte,Base64.DEFAULT).getBytes(), Toast.LENGTH_SHORT).show();
        return Base64.encodeToString(imgByte, Base64.DEFAULT);
    }

    private Bitmap stringtobitmap(String imagestring) {
        byte[] imageinbyte = Base64.decode(imagestring, Base64.DEFAULT);
        Bitmap decodeImage = BitmapFactory.decodeByteArray(imageinbyte, 0, imageinbyte.length);
        return decodeImage;
    }

    private void uploadimage(String imagebitmap) {
        Log.e("VALUE OF S", " " + imagebitmap);
        Log.e("VALUE OF S", "in UploadImage ");
        //First Step is Make a RetrofitClient and Buld That in this Class or U can Make Seprate Call for That
        //2nd create Interface For Your Get Request Or For Post Request Whatever Your May required
        //Make A client
        RetrofitClient client = new RetrofitClient();
        //Provide the Implements of the interface using Client
        WebService createdClient = client.RetrofitClientObject().create(WebService.class);
        ProfilePhotoRequest photoRequest = new ProfilePhotoRequest(email, imagebitmap);
        //Sending RequestBody tp the Post method in Interface
        Call<String> getResponse = createdClient.setprofile(photoRequest);
       //Now Last Step calling CallBack Method
        getResponse.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String r = response.toString();
                if (response.isSuccessful()) {
                    Log.e("VALUE OF Response", "" + r);
                } else {
                    Log.e("VALUE OF Response", "" + r);

                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }
}