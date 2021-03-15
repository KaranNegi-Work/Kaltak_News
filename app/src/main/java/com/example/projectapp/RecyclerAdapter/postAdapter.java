package com.example.projectapp.RecyclerAdapter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectapp.R;
import com.example.projectapp.models.newsResponse;

import java.util.List;

public class postAdapter extends RecyclerView.Adapter<postAdapter.pViewHolder> {
    private List<newsResponse> putpost;
    private onClickPost onClickPost3;
    public postAdapter(List<newsResponse> data,onClickPost onClickPosts){
        this.putpost =data;
        this.onClickPost3=onClickPosts;
    }

    @NonNull
    @Override
    public postAdapter.pViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater lf= LayoutInflater.from(parent.getContext());
        View v= lf.inflate(R.layout.post_templet,parent,false);
        return new postAdapter.pViewHolder(v,onClickPost3);
    }

    @Override
    public void onBindViewHolder(@NonNull pViewHolder holder, int position) {
        newsResponse post= putpost.get(position);
        holder.textView.setText(post.getDiscription());
        holder.icon.setImageBitmap(stringtoimage(post.getImage()));
    }



    @Override
    public int getItemCount() {
        return putpost.size();
    }

    public class pViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView icon;
        TextView textView;
        onClickPost onClickPost2;
        public pViewHolder(@NonNull View itemView, onClickPost onClickPost) {
            super(itemView);
            this.onClickPost2=onClickPost;
            icon= itemView.findViewById(R.id.artilcephoto);
            textView=itemView.findViewById(R.id.articletitle);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            newsResponse forpostID = putpost.get(getAdapterPosition()) ;
         onClickPost2.postClicked(forpostID.getId());

        }
    }
    private Bitmap stringtoimage(String imageString) {
        byte[] imageBytes = Base64.decode(imageString, Base64.DEFAULT);
        Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        return decodedImage;
    }
}
