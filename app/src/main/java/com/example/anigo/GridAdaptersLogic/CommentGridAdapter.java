package com.example.anigo.GridAdaptersLogic;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.anigo.Models.AnimeComment;
import com.example.anigo.Models.Favourite;
import com.example.anigo.R;
import com.example.anigo.UiHelper.ImageBitmapHelper;

import java.util.ArrayList;

public class CommentGridAdapter extends BaseAdapter {

    private Context _context;
    private AnimeComment[] _comments;


    private TextView commentTextView;
    private ImageView userPosterImageView;
    private TextView userNameTextView;


    private LayoutInflater _inflater;

    public CommentGridAdapter(Context context, AnimeComment[] comments) {
        this._context = context;
        this._comments = comments;
    }

    @Override
    public int getCount() {
        return _comments.length;
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

        AnimeComment currentComment = _comments[i];

        Bitmap userPosterBitmap = ImageBitmapHelper
                .GetImageBitmap(ImageBitmapHelper.GetByteArrayFromString(currentComment.user.image));


        _inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = _inflater.inflate(R.layout.comment_grid_item, null);

        userNameTextView = view.findViewById(R.id.user_name_text_view);
        commentTextView = view.findViewById(R.id.comment_text_view);
        userPosterImageView = view.findViewById(R.id.user_poster_image_view);

        userNameTextView.setText(currentComment.user.name);
        commentTextView.setText(currentComment.comment);
        userPosterImageView.setImageBitmap(userPosterBitmap);


        return view;
    }
}
