package com.example.anigo.GridAdaptersLogic;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.example.anigo.UiHelper.ImageBitmapHelper;
import com.example.anigo.Models.Favourite;
import com.example.anigo.R;

import java.util.ArrayList;

public class FragmentLikedGridAdapter extends BaseAdapter {

    Context context;

    private static final String EMPTY_STRING = "";
    private ArrayList<Favourite> favourites;

    LayoutInflater inflater;

    public FragmentLikedGridAdapter(Context context, ArrayList<Favourite> favourites) {
        this.context = context;
        this.favourites = favourites;

    }

    @Override
    public int getCount() {
        return favourites.size();
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

        Favourite fav = favourites.get(i);
        String comment = fav.comment;

        if(inflater == null)
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(view == null){
            view = inflater.inflate(R.layout.fragment_liked_grid_item, null);
        }

        Bitmap poster = ImageBitmapHelper.GetImageBitmap(java.util.Base64.getDecoder().decode(fav.anime.images[0].preview));
        CardView cardView = (CardView) view.findViewById(R.id.view2);

        TextView textView = (TextView) view.findViewById(R.id.item_name);

        ImageView img = (ImageView) view.findViewById(R.id.item_image);

        TextView tViewDesc = (TextView) view.findViewById(R.id.item_desc);

        TextView tViewScoreType = (TextView) view.findViewById(R.id.item_score_type);

        TextView comment_tv = view.findViewById(R.id.comment_tv);

        TextView comment_title = view.findViewById(R.id.comment_title);
        if(fav.comment.isEmpty()){
            comment_tv.setVisibility(View.GONE);
            comment_title.setVisibility(View.GONE);
        }
        else {
            comment_tv.setVisibility(View.VISIBLE);
            comment_title.setVisibility(View.VISIBLE);
        }
        comment_tv.setText(comment);

        img.setImageBitmap(poster);

        textView.setText(fav.anime.nameRus);
        String desc = "";
        if(fav.anime.description != null && fav.anime.description.length()>150)
            desc = fav.anime.description.substring(0, 150) + "...";
        tViewDesc.setText(desc);
        tViewScoreType.setText(fav.anime.scoreShiki + " ~ "+fav.anime.episodes + " эп.");
        return view;
    }
}
