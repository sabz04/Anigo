package com.example.anigo.Activities.AnimeActivityLogic;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.anigo.Models.AnimeComment;
import com.example.anigo.R;
import com.example.anigo.UiHelper.ImageBitmapHelper;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

    Context _context;
    AnimeComment[] _comments;

    public MyAdapter(Context context, AnimeComment[] comments) {
        this._comments = comments;
        this._context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(_context).inflate(R.layout.comment_grid_item, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        AnimeComment currentComment = _comments[position];

        Bitmap userPosterBitmap = ImageBitmapHelper
                .GetImageBitmap(ImageBitmapHelper.GetByteArrayFromString(currentComment.user.image));


        holder.userNameTextView.setText(_comments[position].user.name);
        holder.commentTextView.setText(_comments[position].comment);
        holder.userPoster.setImageBitmap(userPosterBitmap);

    }

    @Override
    public int getItemCount() {
        return _comments.length;
    }
}
