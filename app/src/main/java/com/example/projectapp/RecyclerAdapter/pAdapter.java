package com.example.projectapp.RecyclerAdapter;
    import android.graphics.Bitmap;
    import android.graphics.BitmapFactory;
    import android.util.Base64;
    import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.projectapp.R;
    import com.example.projectapp.models.newsResponse;

    import java.util.List;
    import java.util.zip.Inflater;

    public class pAdapter extends RecyclerView.Adapter<pAdapter.pViewHolder> {
        private List<newsResponse> newsResponse;
        private onClickPost onClickPost;
        public pAdapter(List<newsResponse> newsResponse,onClickPost onClickPost){
            this.newsResponse =newsResponse;
            this.onClickPost=onClickPost;
        }
        @NonNull
        @Override
        public pViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater lf= LayoutInflater.from(parent.getContext());
            View v= lf.inflate(R.layout.post_templet,parent,false);
            return new pViewHolder(v,onClickPost);
        }
        @Override
        public void onBindViewHolder(@NonNull pViewHolder holder, int position) {
            newsResponse post= newsResponse.get(position);
            holder.textView.setText(post.getDiscription());
            holder.icon.setImageBitmap(stringtoimage(post.getImage()));
        }
        @Override
        public int getItemCount() {
            return newsResponse.size();
        }
        public class pViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            ImageView icon;
            TextView textView;
            onClickPost onClickPost2;
            public pViewHolder(@NonNull View itemView, onClickPost onClickPost) {
                super(itemView);
                icon= itemView.findViewById(R.id.artilcephoto);
                textView=itemView.findViewById(R.id.articletitle);
                this.onClickPost2=onClickPost;
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                newsResponse forpostID = newsResponse.get(getAdapterPosition()) ;
                onClickPost2.postClicked(forpostID.getId());
            }
        }
        private Bitmap stringtoimage(String imageString) {
            byte[] imageBytes = Base64.decode(imageString, Base64.DEFAULT);
            Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            return decodedImage;
        }
    }


