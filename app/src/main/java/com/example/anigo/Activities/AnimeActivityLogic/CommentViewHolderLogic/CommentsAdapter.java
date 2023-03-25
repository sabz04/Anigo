package com.example.anigo.Activities.AnimeActivityLogic.CommentViewHolderLogic;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.anigo.Activities.NavigationActivityLogic.NavigationActivity;
import com.example.anigo.CommentsActivityLogic.CommentsActivityContract;
import com.example.anigo.Models.AnimeComment;
import com.example.anigo.Models.CommentLiked;
import com.example.anigo.MyApp;
import com.example.anigo.R;
import com.example.anigo.UiHelper.ImageBitmapHelper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class CommentsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static final int VIEW_TYPE_ITEM = 0;
    private static final int VIEW_TYPE_BUTTON = 1;

    ArrayList<AnimeComment> comments;
    ImageView userPoster;
    TextView commentTextView, userNameTextView, commentDateTextView, likedCount;
    Button likeButton, deleteButton, loadButton;
    AnimeComment comment;
    Context context;

    RecyclerView.ViewHolder holder;


    loadMoreButtonItemClickListener loadListener;
    deleteButtonItemClickListener dListener;
    likeButtonItemClickListener lListener;
    likeDeleteItemClickListener likeDeleteItemClickListener;

    int userId = -1;

    boolean isCommented = false;


    @Override
    public int getItemViewType(int position) {
        return (position == getItemCount() - 1) ? VIEW_TYPE_BUTTON : VIEW_TYPE_ITEM;
    }

    public CommentsAdapter(ArrayList<AnimeComment> comments, int userId, Context context) {
        this.comments = comments;
        this.userId = userId;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if(viewType == VIEW_TYPE_ITEM){
            return new RecyclerView.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_grid_item, parent, false)) {
            };
        }
        else if (viewType == VIEW_TYPE_BUTTON){
            return new ButtonViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.load_button_grid_item, parent, false)) {
            };
        }
       return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if(holder instanceof ButtonViewHolder){
            ButtonViewHolder buttonHolder = (ButtonViewHolder) holder;
            buttonHolder.buttonLoadMore.setOnClickListener(v -> {
                loadListener.loadMore();
            });
        }
        else {

            isCommented = false;
            this.holder = holder;
            likeButton = holder.itemView.findViewById(R.id.likeButton);
            deleteButton = holder.itemView.findViewById(R.id.deleteButton);
            userPoster = holder.itemView.findViewById(R.id.user_poster_image_view);
            commentTextView = holder.itemView.findViewById(R.id.comment_text_view);
            userNameTextView = holder.itemView.findViewById(R.id.user_name_text_view) ;
            commentDateTextView = holder.itemView.findViewById(R.id.commentDateTextView);
            likedCount = holder.itemView.findViewById(R.id.likedStatus);
            loadButton = holder.itemView.findViewById(R.id.loadMoreButton);

            comment = comments.get(position);

            Bitmap userPosterBitmap = ImageBitmapHelper
                    .GetImageBitmap(ImageBitmapHelper.GetByteArrayFromString(comment.user.image));
            //конвертация в простейший формат даты
            DateFormat dateTargetFormat = new SimpleDateFormat("dd MMMM yyyy, в HH:mm");
            String parsedDate = dateTargetFormat.format(comment.date);
            commentDateTextView.setText(parsedDate);
            userNameTextView.setText(comment.user.name + "ID " + comment.id);
            commentTextView.setText(comment.comment);
            userPoster.setImageBitmap(userPosterBitmap);
            likedCount.setText(String.valueOf(comment.commentLikeds.length));

            if (comment.userId == userId) {
                deleteButton.setVisibility(View.VISIBLE);
                deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dListener.onItemDeleteClick(holder.getBindingAdapterPosition());
                    }
                });
            } else {
                deleteButton.setVisibility(View.GONE);
            }

            for (CommentLiked like:
                    comment.commentLikeds) {
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
                likeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        lListener.onItemLikeClick(holder.getBindingAdapterPosition(), likeButton);
                    }
                });
                likeButton.setBackgroundResource(R.drawable.baseline_recommend_24);
            }
            else{
                likeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        likeDeleteItemClickListener.likeDelete(holder.getBindingAdapterPosition());
                    }
                });
                likeButton.setBackgroundResource(R.drawable.baseline_recommend_24_green);
            }
        }

    }

    static class ButtonViewHolder extends RecyclerView.ViewHolder {
        Button buttonLoadMore;

        public ButtonViewHolder(View itemView) {
            super(itemView);
            buttonLoadMore = itemView.findViewById(R.id.loadMoreButton);
        }
    }

    public void setDeleteClickListener(deleteButtonItemClickListener dListener){
        this.dListener = dListener;
    }
    public void setLoadClickListener(loadMoreButtonItemClickListener loadListener){
        this.loadListener = loadListener;
    }
    public void setLikeClickListener(likeButtonItemClickListener lListener){
        this.lListener = lListener;
    }
    public void setLikeRemoveClickListener(likeDeleteItemClickListener likeRemoveListener){
        this.likeDeleteItemClickListener = likeRemoveListener;
    }

    public interface likeDeleteItemClickListener{
        void likeDelete(int position);
    }
    public interface loadMoreButtonItemClickListener{
        void loadMore();
    }
    public interface deleteButtonItemClickListener{
        void onItemDeleteClick(int position);
    }
    public interface likeButtonItemClickListener{
        void onItemLikeClick(int position, Button button);
    }
    @Override
    public int getItemCount() {
        return comments.size();
    }

    public ArrayList<AnimeComment> getData(){
        return comments;
    }


}
