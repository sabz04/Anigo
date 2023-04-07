package com.example.anigo.UiHelper;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.View;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.example.anigo.R;

public class TextViewHelper {
    public static TextView Create_New_TextView_Template(Context context, String text, Activity activity){
        TextView txt = new TextView(context);
        txt.setText(text.toString());
        //txt.setPadding(10,5,10,5);
        txt.setPaintFlags(txt.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        Typeface typeface = ResourcesCompat.getFont(context, R.font.roboto_regular);
        txt.setTypeface(typeface);
        txt.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        txt.setTextColor(activity.getResources().getColor(R.color.nicered));
        return txt;
    }
}
