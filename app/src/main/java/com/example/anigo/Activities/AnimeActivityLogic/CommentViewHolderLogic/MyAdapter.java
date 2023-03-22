package com.example.anigo.Activities.AnimeActivityLogic.CommentViewHolderLogic;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.anigo.Models.AnimeComment;
import com.example.anigo.Models.CommentLiked;
import com.example.anigo.R;
import com.example.anigo.UiHelper.ImageBitmapHelper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import kotlin.collections.SlidingWindowKt;
import okhttp3.OkHttpClient;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

    Context _context;
    ArrayList<AnimeComment> _comments;


    AnimeComment currentComment;

    int userId;
    OkHttpClient client;

    boolean isCommented = false;

    public MyAdapter(Context context, ArrayList<AnimeComment> comments, int userId) {
        this._comments = comments;
        this._context = context;
        client = new OkHttpClient();
        this.userId = userId;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(_context).inflate(R.layout.comment_grid_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        isCommented = false;
        currentComment = _comments.get(position);
        holder.userId = this.userId;
        Bitmap userPosterBitmap = ImageBitmapHelper
                .GetImageBitmap(ImageBitmapHelper.GetByteArrayFromString(currentComment.user.image));
        //конвертация в простейший формат даты
        DateFormat dateTargetFormat = new SimpleDateFormat("dd MMMM yyyy, в HH:mm");
        String parsedDate = dateTargetFormat.format(currentComment.date);
        holder.commentDateTextView.setText(parsedDate);
        holder.context = _context;
        holder.userNameTextView.setText(currentComment.user.name);
        holder.commentTextView.setText(currentComment.comment);
        holder.userPoster.setImageBitmap(userPosterBitmap);
        holder.likedCount.setText(String.valueOf(currentComment.commentLikeds.length));
        holder.comment = currentComment;
        if (currentComment.userId == userId) {
            holder.deleteButton.setVisibility(View.VISIBLE);
        } else {
            holder.deleteButton.setVisibility(View.GONE);
        }
        for (CommentLiked like:
             currentComment.commentLikeds) {
            if(like.userId == userId)
            {
                isCommented = true;
                break;
            }
            else{
                isCommented = false;
            }

        }
        if(!isCommented){
            holder.likeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holder.likePresenter.AddLike(holder.comment.id);
                }
            });
            holder.likeButton.setBackgroundResource(R.drawable.baseline_recommend_24);
        }
        else{
            holder.likeButton.setBackgroundResource(R.drawable.baseline_recommend_24_green);
        }


}

    @Override
    public int getItemCount() {
       return  _comments.size();
    }

}
