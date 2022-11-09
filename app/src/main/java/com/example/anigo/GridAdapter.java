package com.example.anigo;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GridAdapter extends BaseAdapter {

    Context context;

    String[] names;

    int[] ids;

    String[] desc;
    Bitmap[] posters;
    LayoutInflater inflater;

    public GridAdapter(Context context, String[] names, String[] descs, Bitmap[] bitmaps) {
        this.context = context;
        this.names = names;
        this.ids = ids;
        this.desc = descs;
        this.posters = bitmaps;
    }

    public GridAdapter(Context context, String[] names) {
        this.context = context;
        this.names = names;
    }


    @Override
    public int getCount() {
        return names.length;
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

        if(inflater == null)
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(view == null){
            view = inflater.inflate(R.layout.grid_item, null);
        }

        TextView textView = (TextView) view.findViewById(R.id.item_name);

        ImageView img = (ImageView) view.findViewById(R.id.item_image);

        TextView tViewDesc = (TextView) view.findViewById(R.id.item_desc);

        img.setImageBitmap(posters[i]);
        //img.setBackground(view.getResources().getDrawable(R.drawable.curcular));
        textView.setText(names[i]);
        tViewDesc.setText(desc[i]);

        return view;
    }
}
