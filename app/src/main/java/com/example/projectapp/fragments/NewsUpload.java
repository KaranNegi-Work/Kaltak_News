package com.example.projectapp.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.projectapp.HomePage;
import com.example.projectapp.R;
import com.example.projectapp.Retrofit.RetrofitClient;
import com.example.projectapp.Retrofit.WebService;
import com.example.projectapp.models.LoginResponse;
import com.example.projectapp.models.NewArticalePost;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class NewsUpload extends Fragment {
    ImageView image;
    Button upload;
    Button choosephoto;
    EditText title;
    EditText article;
    Bitmap bitmap;
    ProgressBar progressBar;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.radio, container, false);
        ;
        image = v.findViewById(R.id.imageview);
        upload = v.findViewById(R.id.upload);
        choosephoto = v.findViewById(R.id.choosephoto);
        title = v.findViewById(R.id.title);
        article = v.findViewById(R.id.article);
        progressBar=v.findViewById(R.id.progressbar);
        progressBar.setVisibility(View.GONE);

//choosephoto.setOnClickListener(new View.OnClickListener() {
//    @Override
//    public void onClick(View v) {
//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        startActivityForResult(intent, 1);
//        Toast.makeText(getContext(), "image"+intent, Toast.LENGTH_SHORT).show();
//    }
//});
        choosephoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getimage();
                Toast.makeText(getContext(), "getimage", Toast.LENGTH_SHORT).show();
                }
        });
upload.setOnClickListener(new View.OnClickListener() {

    @Override
    public void onClick(View v) {
        progressBar.setVisibility(View.VISIBLE);
        uploadArticle();
    }
});
        return v;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1&&resultCode==RESULT_OK&&data!=null){
            Uri path=data.getData();
            try {
                bitmap= MediaStore.Images.Media.getBitmap(getContext().getContentResolver(),path);
                image.setImageBitmap(bitmap);
                image.setVisibility(View.VISIBLE);
                choosephoto.setVisibility(View.GONE);
                Toast.makeText(getContext(), ""+bitmap, Toast.LENGTH_SHORT).show();
            }
            catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getContext(), ""+e, Toast.LENGTH_SHORT).show();
            }
        }}

        private String imagetoString(){
            ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
            byte[] imgByte=byteArrayOutputStream.toByteArray();
            //Toast.makeText(getContext(), ""+Base64.encodeToString(imgByte,Base64.DEFAULT).getBytes(), Toast.LENGTH_SHORT).show();
            return Base64.encodeToString(imgByte,Base64.DEFAULT);
        }
    private void getimage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 1);
        Toast.makeText(getContext(), "image"+intent, Toast.LENGTH_SHORT).show();
    }
    private void uploadArticle() {
        if (image.getDrawable() == null){
            Toast.makeText(getContext(), "Please Select Image", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(title.getText().toString())){
            title.setError("Please Enter Title");
            return;
        }
        if (TextUtils.isEmpty(article.getText().toString())){
            article.setError("Please Enter Full Description");
            return;
        }
       // Toast.makeText(getContext(), "image"+imagetoString(), Toast.LENGTH_SHORT).show();
        //First Step is Make a RetrofitClient and Buld That in this Class or U can Make Seprate Call for That
        //2nd create Interface For Your Get Request Or For Post Request Whatever Your May required
        //Make A client
        RetrofitClient client=new RetrofitClient();
        //Provide the Implements of the interface using Client
        WebService createdClient=client.RetrofitClientObject().create(WebService.class);
        //Making RequestBody
        NewArticalePost newArticalePost=new NewArticalePost(imagetoString(),title.getText().toString(),article.getText().toString());
        //Sending RequestBody tp the Post method in Interface
        Call<LoginResponse> getResponse=createdClient.UploadPost(newArticalePost);
//Now Last Step calling CallBack Method
        getResponse.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if(response.isSuccessful()){
                    progressBar.setVisibility(View.GONE);
                    DialogueBox();
                    Toast.makeText(getContext(), "Wait For Your News To Be Approved", Toast.LENGTH_SHORT).show();

                }
                else{
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "error"+response.body(), Toast.LENGTH_SHORT).show();
                }

            }
            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), "onFaulure"+t.toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }
    private  void DialogueBox() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext(), R.style.AlertDialogCustom);
        alertDialogBuilder.setMessage("Your News Is Posted, Redirecting to Home");
        alertDialogBuilder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        Toast.makeText(getContext(), "You clicked OK button", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getContext(), HomePage.class));
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}


