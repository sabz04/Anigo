package com.example.anigo;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/*import com.wefika.flowlayout.FlowLayout;*/

import org.w3c.dom.Text;

import java.util.ArrayList;

public class AnimeActivity extends AppCompatActivity  implements  AnimeActivityContract.View{

    private AnimeActivityPresenter presenter;

    private String genres = "";

    private String studios = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anime);

        Bundle bundle = getIntent().getExtras();

        int id = bundle.getInt("id");

        Button back_btn = findViewById(R.id.back_btn);

        Button like_btn = findViewById(R.id.like_btn);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });
        like_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateNewContactDialog();
            }
        });

        presenter= new AnimeActivityPresenter(this, getApplicationContext());

        presenter.GetAnime(id);


}

    private void CreateNewContactDialog() {
        AlertDialog.Builder dialog_builder = new AlertDialog.Builder(this);
        View fav_dialog = getLayoutInflater().inflate(R.layout.dialog_add_to_favs, null);
        fav_dialog.setClipToOutline(true);
        dialog_builder.setView(fav_dialog);

        AlertDialog dialog = dialog_builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    @Override
    public void OnSuccess(Anime anime) {
        this.genres = "Жанры: ";
        this.studios = "Студии: ";

       runOnUiThread(new Runnable() {
           @Override
           public void run() {
               ImageView poster = findViewById(R.id.itemPoster);

               poster.setImageBitmap(ImageBitmapHelper.GetImageBitmap(ImageBitmapHelper.GetByteArrayFromString(anime.images[0].original)));

               TextView text= (TextView) findViewById(R.id.itemName);

               text.setText(anime.nameRus);

               TextView desc = (TextView)findViewById(R.id.itemDesc);

               desc.setText(anime.description);

               TextView score = (TextView)findViewById(R.id.itemScore);

               score.setText(String.valueOf(anime.scoreShiki));

               TextView type = (TextView)findViewById(R.id.itemType);

               type.setText(AnimeTypeOrganizer.Organizer(anime.type.name));

               for(Genre genre : anime.genres){
                   genres += genre.nameRus + " ";
               }
               for(Studio studio : anime.studios){
                   studios += studio.name + " ";
               }

               FlowLayout genres_layout = findViewById(R.id.genres_layout);

               FlowLayout studios_layout = findViewById(R.id.studios_layout);
               for(Genre genre : anime.genres){
                   genres_layout.addView(
                           Create_New_TextView_Template(
                                   getApplicationContext(),
                                   genre.nameRus,
                                   AnimeActivity.this)
                   );
               }
               for(Studio studio : anime.studios){
                   studios_layout.addView(
                           Create_New_TextView_Template(
                                   getApplicationContext(),
                                   studio.name,
                                   AnimeActivity.this)
                   );
               }
           }
       });


    }
    public TextView Create_New_TextView_Template(Context context, String text, Activity activity){
        TextView txt = new TextView(context);
        txt.setText(text.toString());



        txt.setPadding(10,5,10,5);




        txt.setTextSize(16);

        txt.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        txt.setTextColor(activity.getResources().getColor(R.color.white));
        txt.setBackground(ContextCompat.getDrawable(activity, R.drawable.rounded_corners));
        return txt;
    }
    /*private void setMargins (View view, int left, int top, int right, int bottom) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            p.setMargins(left, top, right, bottom);
            view.requestLayout();
        }
    }*/
    @Override
    public void OnError(String message) {

    }

    @Override
    public void OnSuccess(Screenshot[] screenshots) {
        int count = screenshots.length;

        LinearLayout screenshots_layout = (LinearLayout) findViewById(R.id.screenshotsLayout);


        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for(Screenshot screen : screenshots){
                    screenshots_layout.addView(ImageBitmapHelper.CreateNewCardViewTemplate(
                            ImageBitmapHelper.GetByteArrayFromString(screen.image),
                            getApplicationContext()

                    ));
                }

            }
        });



    }

    private Bitmap GetImageBitmap(byte[] jsonImage){

        Bitmap bitmap = BitmapFactory.decodeByteArray(jsonImage, 0, jsonImage.length);
        System.out.println(bitmap.getHeight());
        bitmap= bitmap.copy(Bitmap.Config.ARGB_8888, true);
        return bitmap;

    }
}