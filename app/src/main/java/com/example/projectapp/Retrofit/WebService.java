package com.example.projectapp.Retrofit;

import com.example.projectapp.models.DBResponceBack;
import com.example.projectapp.models.LoginResponse;
import com.example.projectapp.models.NewArticalePost;
import com.example.projectapp.models.ProfilePhotoRequest;
import com.example.projectapp.models.UserDataModel;
import com.example.projectapp.models.UserLoginRequest;
import com.example.projectapp.models.newsResponse;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface WebService {
    @POST("home/CreateUser")
    Call<DBResponceBack> CreateUser(@Body UserDataModel data);

    @POST("home/userlogin")
    Call<LoginResponse> LogInRequest(@Body UserLoginRequest pass);

    @POST("home/Addpost")
    Call<LoginResponse> UploadPost(@Body NewArticalePost post);

    @GET("home/news")
    Call<List<newsResponse>> getPost();

    @POST("home/ProfilePhoto")
    Call<String> setprofile(@Body ProfilePhotoRequest profilePhotoRequest);

    @FormUrlEncoded
    @POST("/home/FullArticle")
    Call<newsResponse> fullArticle(@Field("id") int id);

    @FormUrlEncoded
    @POST("/home/FetchProfilePhoto")
    Call<ProfilePhotoRequest> fullprofile(@Field("email") String email);

}
