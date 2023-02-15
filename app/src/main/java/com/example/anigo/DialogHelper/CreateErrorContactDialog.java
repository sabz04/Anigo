package com.example.anigo.DialogHelper;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.example.anigo.R;

public class CreateErrorContactDialog {
    private Context context;

    private AlertDialog error_dialog;


    public CreateErrorContactDialog(Context context) {
        this.context = context;

    }

    public void CreateNewDialog(String message) {
        AlertDialog.Builder dialog_builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View error_view = inflater.inflate(R.layout.dialog_error, null);


        TextView error_txt = error_view.findViewById(R.id.error_message);

        error_txt.setText(message);

        error_view.setClipToOutline(true);
        dialog_builder.setView(error_view);


        error_dialog = dialog_builder.create();
        error_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

    }
    public void ShowDialog(){
        if(error_dialog!= null)
            this.error_dialog.show();
    }

}
