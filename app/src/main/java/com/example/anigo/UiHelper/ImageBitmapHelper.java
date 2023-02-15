package com.example.anigo.UiHelper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.cardview.widget.CardView;

public class ImageBitmapHelper {


    public static byte[] GetByteArrayFromString(String arr_str){
        try{
            return  java.util.Base64.getDecoder().decode(arr_str);
        }
       catch (Exception exception){
            return null;
        }
    }

    public static CardView CreateNewCardViewTemplate(byte[] src, Context context){
        ImageView img = new ImageView(context);
        CardView crd = new CardView(context);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        Bitmap current_bitmap = GetImageBitmap(src);
        Bitmap new_bitmap = getResizedBitmap(current_bitmap, 500, 295);
        img.setImageBitmap(new_bitmap);

        lp.setMargins(10, 5, 10, 5);
        crd.setLayoutParams(lp);
        crd.setRadius(25);
        crd.addView(img);
        return crd;
    }
    public static Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }
    public static Bitmap GetImageBitmap(byte[] jsonImage){
        try{
            Bitmap bitmap = BitmapFactory.decodeByteArray(jsonImage, 0, jsonImage.length);
            System.out.println(bitmap.getHeight());
            bitmap= bitmap.copy(Bitmap.Config.ARGB_8888, true);
            return bitmap;
        }
       catch (Exception exception){
            return  null;
       }

    }
}
