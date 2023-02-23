package com.example.anigo.Activities.AnimeActivityLogic;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.anigo.R;

public class MyViewHolder extends RecyclerView.ViewHolder {

    ImageView userPoster;
    TextView commentTextView, userNameTextView;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        userPoster = itemView.findViewById(R.id.user_poster_image_view);
        commentTextView = itemView.findViewById(R.id.comment_text_view);
        userNameTextView = itemView.findViewById(R.id.user_name_text_view);
    }
}
