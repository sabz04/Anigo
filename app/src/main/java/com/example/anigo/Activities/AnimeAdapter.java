package com.example.anigo.Activities;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.anigo.Models.Anime;
import com.example.anigo.R;
import com.example.anigo.RequestsHelper.RequestOptions;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AnimeAdapter extends RecyclerView.Adapter<AnimeAdapter.ViewHolder> {
    private Anime[] mAnimeList;
    private static OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public AnimeAdapter(Anime[] animeList) {
        mAnimeList = animeList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.anime_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Anime anime = mAnimeList[position];
        holder.mNameTextView.setText(anime.nameRus);
        Picasso.with(holder.itemView.getContext())
                .load(RequestOptions.SecondHost + anime.images[0].preview)
                .into(holder.mImageView);
    }

    @Override
    public int getItemCount() {
        return mAnimeList.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;
        public TextView mNameTextView;

        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mListener.onItemClick(position);
                    }
                }
            });
            mImageView = view.findViewById(R.id.posterImageView);
            mNameTextView = view.findViewById(R.id.nameRusTextView);
        }
    }
}
