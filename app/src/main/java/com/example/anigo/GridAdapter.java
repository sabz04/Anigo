package com.example.anigo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GridAdapter extends BaseAdapter {

    Context context;

    Anime[] animeList;

    LayoutInflater inflater;


    public GridAdapter(Context context, Anime[] animes) {
        this.animeList = animes;
        this.context = context;
    }

    @Override
    public int getCount() {
        return animeList.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }


    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        Anime anime = animeList[i];



        if(inflater == null)
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(view == null){
            view = inflater.inflate(R.layout.grid_item, null);
        }

        Bitmap poster = GetImageBitmap(java.util.Base64.getDecoder().decode(anime.images[0].preview));
        CardView cardView = (CardView) view.findViewById(R.id.view2);

        TextView textView = (TextView) view.findViewById(R.id.item_name);

        ImageView img = (ImageView) view.findViewById(R.id.item_image);

        TextView tViewDesc = (TextView) view.findViewById(R.id.item_desc);

        TextView tViewScoreType = (TextView) view.findViewById(R.id.item_score_type);

        img.setImageBitmap(poster);

        textView.setText(anime.nameRus);
        String desc = "";
        if(anime.description != null && anime.description.length()>150)
            desc = anime.description.substring(0, 150) + "...";
        tViewDesc.setText(desc);
        tViewScoreType.setText(anime.scoreShiki + " ~ "+anime.episodes + " эп.");
        return view;
    }

    private Bitmap GetImageBitmap(byte[] jsonImage){

        Bitmap bitmap = BitmapFactory.decodeByteArray(jsonImage, 0, jsonImage.length);
        System.out.println(bitmap.getHeight());
        bitmap= bitmap.copy(Bitmap.Config.ARGB_8888, true);
        return bitmap;

    }
}
