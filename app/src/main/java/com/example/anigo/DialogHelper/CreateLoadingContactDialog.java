package com.example.anigo.DialogHelper;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;

import com.example.anigo.R;

import java.util.logging.LogRecord;

public class CreateLoadingContactDialog {

    private Context context;

    private AlertDialog loading_dialog;


    public CreateLoadingContactDialog(Context context) {
        this.context = context;
        CreateNewContactDialog_AddToFav();
    }

    private void CreateNewContactDialog_AddToFav() {
        AlertDialog.Builder dialog_builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View loading_view = inflater.inflate(R.layout.dialog_loading, null);

        loading_view.setClipToOutline(true);
        dialog_builder.setView(loading_view);


        loading_dialog = dialog_builder.create();
        loading_dialog.setCancelable(false);
        loading_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

    }
    public void ShowDialog(){
        if(loading_dialog!= null)
            this.loading_dialog.show();
    }
    public void DeleteDialog(){
        Handler hnd = new Handler();
            if(loading_dialog!= null)
                    loading_dialog.cancel();
    }
}
