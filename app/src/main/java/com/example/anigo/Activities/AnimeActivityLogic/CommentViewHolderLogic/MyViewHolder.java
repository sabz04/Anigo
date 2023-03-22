package com.example.anigo.Activities.AnimeActivityLogic.CommentViewHolderLogic;

import android.app.Activity;
import android.content.Context;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.anigo.AuthentificationLogic.Authentification;
import com.example.anigo.AuthentificationLogic.AuthentificationInterface;
import com.example.anigo.InnerDatabaseLogic.FeedUserDbHelper;
import com.example.anigo.InnerDatabaseLogic.FeedUserLocal;
import com.example.anigo.Models.AnimeComment;
import com.example.anigo.Models.CommentLikeAddClass;
import com.example.anigo.MyApp;
import com.example.anigo.R;
import com.example.anigo.RequestsHelper.RequestOptions;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MyViewHolder extends RecyclerView.ViewHolder implements ViewHolderContract.View {

    ImageView userPoster;
    TextView commentTextView, userNameTextView, commentDateTextView, likedCount;
    Button likeButton, deleteButton;
    AnimeComment comment;

    CommentLikePresenter likePresenter;
    Context context;

    int userId;
    int likeCount = 0;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        likePresenter = new CommentLikePresenter(this, MyApp.getContext());
        likeButton = itemView.findViewById(R.id.likeButton);
        deleteButton = itemView.findViewById(R.id.deleteButton);
        userPoster = itemView.findViewById(R.id.user_poster_image_view);
        commentTextView = itemView.findViewById(R.id.comment_text_view);
        userNameTextView = itemView.findViewById(R.id.user_name_text_view);
        commentDateTextView = itemView.findViewById(R.id.commentDateTextView);
        likedCount = itemView.findViewById(R.id.likedStatus);


    }
    @Override
    public void OnSuccessAddLike(String message) {
        likeCount = Integer.valueOf(likedCount.getText().toString());
        likeCount++;
        ((Activity)context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                likedCount.setText(String.valueOf(likeCount));
                likeButton.setBackgroundResource(R.drawable.baseline_recommend_24_green);
            }
        });
    }

    @Override
    public void OnErrorAddLike(String message) {

    }

    @Override
    public void OnSuccessRemoveComment(String message) {

    }

    @Override
    public void OnErrorRemoveComment(String message) {

    }
}
