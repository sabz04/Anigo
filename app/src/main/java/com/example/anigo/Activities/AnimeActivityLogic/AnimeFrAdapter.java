package com.example.anigo.Activities.AnimeActivityLogic;

import static com.example.anigo.UiHelper.ImageBitmapHelper.GetImageBitmap;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.anigo.Models.Anime;
import com.example.anigo.R;
import com.example.anigo.RequestsHelper.RequestOptions;
import com.squareup.picasso.Picasso;

public class AnimeFrAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Anime[] animes;
    int currentAnimeId;
    Context context;

    private OnItemClickListener mListener;

    public AnimeFrAdapter(Anime[] animes, Context context, int animeId) {
        this.animes = animes;
        this.currentAnimeId = animeId;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RecyclerView.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item, parent, false)){

        };
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Anime anime = animes[position];


            CardView cardView = (CardView) holder.itemView.findViewById(R.id.view2);

            TextView textView = (TextView) holder.itemView.findViewById(R.id.item_name);

            ImageView img = (ImageView) holder.itemView.findViewById(R.id.item_image);

            TextView tViewDesc = (TextView) holder.itemView.findViewById(R.id.item_desc);

            TextView tViewScoreType = (TextView) holder.itemView.findViewById(R.id.item_score_type);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.OnClick(holder.getBindingAdapterPosition());
                }
            });
            Picasso.with(context).load(RequestOptions.SecondHost+anime.images[0].preview).into(img);

            textView.setText(anime.nameRus);
            String desc = "";
            if(anime.description != null && anime.description.length()>150)
                desc = anime.description.substring(0, 150) + "...";
            tViewDesc.setText(desc);
            tViewScoreType.setText(anime.scoreShiki + " ~ "+anime.episodes + " эп.");

    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }
    public  interface OnItemClickListener{
        void OnClick(int position);
    }
    @Override
    public int getItemCount() {
        return animes.length;
    }
}
