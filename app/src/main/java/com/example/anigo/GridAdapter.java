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
        Bitmap poster = GetImageBitmap(java.util.Base64.getDecoder().decode(anime.images[0].preview));

        if(inflater == null)
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(view == null){
            view = inflater.inflate(R.layout.grid_item, null);
        }

        CardView cardView = (CardView) view.findViewById(R.id.view2);

        TextView textView = (TextView) view.findViewById(R.id.item_name);

        ImageView img = (ImageView) view.findViewById(R.id.item_image);

        TextView tViewDesc = (TextView) view.findViewById(R.id.item_desc);

        TextView tViewScoreType = (TextView) view.findViewById(R.id.item_score_type);




        img.setImageBitmap(poster);

        //img.setBackground(view.getResources().getDrawable(R.drawable.curcular));
        textView.setText(anime.nameRus);
        String desc = "";
        if(anime.description != null && anime.description.length()>150)
            desc = anime.description.substring(0, 150) + "...";
        tViewDesc.setText(desc);
        tViewScoreType.setText(anime.scoreShiki + " ~ "+anime.episodes + " эп.");

       // System.out.println(poster.getWidth());








            /*int baseWidth = poster.getWidth();
        int baseHeight = poster.getHeight();
        cardView.setCardBackgroundColor(NetWodminantColor(Bitmap.createScaledBitmap(poster, baseWidth, baseHeight, true)));*/


//        Bitmap newBitmap = Bitmap.createScaledBitmap(posters[i], baseWidth / 25, 5, true);
//        cardView.setBackground(new BitmapDrawable(view.getResources(), newBitmap));

        return view;
    }
    public static int getDominantColor(Bitmap bitmap) {
        Bitmap newBitmap = Bitmap.createScaledBitmap(bitmap, 1, 1, false);
        final int color = newBitmap.getPixel(0, 0);
        newBitmap.recycle();
        return color;
    }

    public  int NetWodminantColor(Bitmap bitmap){
        if (bitmap == null)
            throw new NullPointerException();

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int size = width * height;
        int pixels[] = new int[size];

        Bitmap bitmap2 = bitmap.copy(Bitmap.Config.ARGB_4444, false);

        bitmap2.getPixels(pixels, 0, width, 0, 0, width, height);

        HashMap<Integer, Integer> colorMap = new HashMap<Integer, Integer>();


        int color = 0;
        int r = 0;
        int g = 0;
        int b = 0;
        Integer rC, gC, bC;
        for (int i = 0; i < pixels.length; i++) {
            int mod = 20;
            r = (Color.red(color) / mod)*mod;
            g = (Color.green(color) / mod)*mod;
            b = (Color.blue(color) / mod)*mod;


            if(colorMap.containsKey(pixels[i]))
            {
                int z = colorMap.get(pixels[i]);
                /*if(z == Color.WHITE || z == Color.BLACK || z == Color.GRAY)
                    colorMap.put(pixels[i], z+10);*/
                colorMap.put(pixels[i], z+1);
            }
            else
                colorMap.put(Color.rgb(r, g, b), 0);
        }

        int max = 0;
        int colorss = bitmap2.getPixel(0, 0);

        for (Map.Entry<Integer, Integer> entry : colorMap.entrySet()) {
            if (entry.getValue() > max) {
                max = entry.getValue();
                colorss = entry.getKey();
            }
        }

        return colorss;
    }

    public int getDominantColor1(Bitmap bitmap) {

        if (bitmap == null)
            throw new NullPointerException();

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int size = width * height;
        int pixels[] = new int[size];

        Bitmap bitmap2 = bitmap.copy(Bitmap.Config.ARGB_4444, false);

        bitmap2.getPixels(pixels, 0, width, 0, 0, width, height);

        final List<HashMap<Integer, Integer>> colorMap = new ArrayList<HashMap<Integer, Integer>>();
        colorMap.add(new HashMap<Integer, Integer>());
        colorMap.add(new HashMap<Integer, Integer>());
        colorMap.add(new HashMap<Integer, Integer>());

        int color = 0;
        int r = 0;
        int g = 0;
        int b = 0;
        Integer rC, gC, bC;
        for (int i = 0; i < pixels.length; i++) {
            color = pixels[i];

            r = Color.red(color);
            g = Color.green(color);
            b = Color.blue(color);

            rC = colorMap.get(0).get(r);
            if (rC == null)
                rC = 0;
            colorMap.get(0).put(r, ++rC);

            gC = colorMap.get(1).get(g);
            if (gC == null)
                gC = 0;
            colorMap.get(1).put(g, ++gC);

            bC = colorMap.get(2).get(b);
            if (bC == null)
                bC = 0;
            colorMap.get(2).put(b, ++bC);
        }

        int[] rgb = new int[3];
        for (int i = 0; i < 3; i++) {
            int max = 0;
            int val = 0;
            for (Map.Entry<Integer, Integer> entry : colorMap.get(i).entrySet()) {
                if (entry.getValue() > max) {
                    max = entry.getValue();
                    val = entry.getKey();
                }
            }
            rgb[i] = val;
        }

        int dominantColor = Color.rgb(rgb[0], rgb[1], rgb[2]);

        return dominantColor;
    }
    private Bitmap GetImageBitmap(byte[] jsonImage){

        Bitmap bitmap = BitmapFactory.decodeByteArray(jsonImage, 0, jsonImage.length);
        System.out.println(bitmap.getHeight());
        bitmap= bitmap.copy(Bitmap.Config.ARGB_8888, true);
        return bitmap;

    }
}
