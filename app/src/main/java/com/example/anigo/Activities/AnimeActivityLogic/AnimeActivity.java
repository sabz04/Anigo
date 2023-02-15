package com.example.anigo.Activities.AnimeActivityLogic;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/*import com.wefika.flowlayout.FlowLayout;*/

import com.example.anigo.Models.Anime;
import com.example.anigo.Models.Image;
import com.example.anigo.UiHelper.FlowLayout;
import com.example.anigo.Models.Genre;
import com.example.anigo.UiHelper.ImageBitmapHelper;
import com.example.anigo.R;
import com.example.anigo.Models.Screenshot;
import com.example.anigo.Models.Studio;
import com.example.anigo.UiHelper.TextViewHelper;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AnimeActivity extends AppCompatActivity  implements  AnimeActivityContract.View{

    private AnimeActivityPresenter presenter;
    private AnimeActivityPresenterAddToFavs presenter_fav;
    private AnimeActivityPresenterCheckIfExist presenter_check;
    private AnimeActivityPresenterDeleteFromFav presenter_delete;
    private Context context;

    private AlertDialog dialog_fav;
    private AlertDialog dialog_delete;

    private Button like_btn;
    private Button add_to_fav;
    private Button delete_from_fav_btn;
    private ImageView poster;

    private TextView name_rus_tv;
    private TextView name_eng_tv;
    private TextView description_tv;
    private TextView date_tv;
    private TextView score_tv;
    private TextView type_tv;

    private FlowLayout genres_layout;
    private FlowLayout studios_layout;
    private LinearLayout screenshots_layout;
    private int anime_id=-1;
    private Date anime_released_on;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anime);
        Bundle bundle = getIntent().getExtras();
        int id = bundle.getInt("id");
        Button back_btn = findViewById(R.id.back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });

        like_btn        = findViewById(R.id.like_btn);
        poster          = findViewById(R.id.itemPoster);
        name_rus_tv     = (TextView) findViewById(R.id.itemName);
        description_tv  = (TextView)findViewById(R.id.itemDesc);
        score_tv        = (TextView)findViewById(R.id.itemScore);
        type_tv         = (TextView)findViewById(R.id.itemType);
        date_tv         = (TextView)findViewById(R.id.item_date);
        genres_layout   = findViewById(R.id.genres_layout);
        studios_layout  = findViewById(R.id.studios_layout);
        context         = getApplicationContext();
        presenter        = new AnimeActivityPresenter(this, context);
        presenter_fav    = new AnimeActivityPresenterAddToFavs(this, context);
        presenter_delete = new AnimeActivityPresenterDeleteFromFav(this,context);

        presenter.GetAnime(id);
    }
    private void CreateNewContactDialog_AddToFav() {
        AlertDialog.Builder dialog_builder = new AlertDialog.Builder(this);
        View fav_dialog = getLayoutInflater().inflate(R.layout.dialog_add_to_favs, null);

        add_to_fav = fav_dialog.findViewById(R.id.add_to_fav_btn);
        EditText comment_fav_tv = fav_dialog.findViewById(R.id.item_fav_comment);

        add_to_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            add_to_fav.setEnabled(false);
            presenter_fav.FavsAdd(comment_fav_tv.getText().toString(), anime_id);

            }
        });
        fav_dialog.setClipToOutline(true);
        dialog_builder.setView(fav_dialog);

        dialog_fav = dialog_builder.create();
        dialog_fav.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog_fav.show();
    }
    private void CreateNewContactDialog_DeleteFromFav() {
        AlertDialog.Builder dialog_builder = new AlertDialog.Builder(this);
        View delete_dialog = getLayoutInflater().inflate(R.layout.dialog_delete_from_favs, null);

        delete_from_fav_btn = delete_dialog.findViewById(R.id.delete_btn);

        delete_from_fav_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter_delete.Delete(anime_id);
            }
        });
        delete_dialog.setClipToOutline(true);
        dialog_builder.setView(delete_dialog);

        dialog_delete = dialog_builder.create();
        dialog_delete.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog_delete.show();
    }
    @Override
    public void OnSuccess(Anime anime) {
        this.anime_id   = anime.shikiId;
        presenter_check = new AnimeActivityPresenterCheckIfExist(this, context);
        presenter_check.Check(anime.shikiId);
       runOnUiThread(new Runnable() {
           @Override
           public void run() {
               poster.setImageBitmap(ImageBitmapHelper.GetImageBitmap(ImageBitmapHelper.GetByteArrayFromString(anime.images[0].original)));
               name_rus_tv.setText(anime.nameRus);
               description_tv.setText(anime.description);
               score_tv.setText(String.valueOf(anime.scoreShiki));
               type_tv.setText(AnimeTypeOrganizer.Organizer(anime.type.name));
               anime_released_on  = anime.releasedOn;
               if(anime_released_on != null){
                   Calendar calendar = Calendar.getInstance();
                   calendar.setTime(anime_released_on);
                   String year  = String.valueOf(calendar.get(Calendar.YEAR));
                   String month = GetDate(calendar.get(Calendar.MONTH));
                   String day   = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
                   date_tv.setText(String.format("%s %s %s г.", day, month.toLowerCase(Locale.ROOT), year));
               }
               for(Genre genre : anime.genres){
                   genres_layout.addView(
                           TextViewHelper.Create_New_TextView_Template(
                                   context,
                                   genre.nameRus,
                                   AnimeActivity.this)
                   );
               }
               for(Studio studio : anime.studios){
                   studios_layout.addView(
                           TextViewHelper.Create_New_TextView_Template(
                                   context,
                                   studio.name,
                                   AnimeActivity.this)
                   );
               }
           }
       });
    }
    private String GetDate(int month){
        if(month == 1){
            return "Январь";
        }
        if(month == 2){
            return "Февраль";
        }
        if(month == 3){
            return "Март";
        }
        if(month == 4){
            return "Май";
        }
        if(month == 5){
            return "Апрель";
        }
        if(month == 6){
            return "Июнь";
        }
        if(month == 7){
            return "Июль";
        }
        if(month == 8){
            return "Август";
        }
        if(month == 9){
            return "Сентябрь";
        }
        if(month == 10){
            return "Октябрь";
        }
        if(month == 11){
            return "Ноябрь";
        }
        if(month == 12){
            return "Декабрь";
        }
        return "none";
    }
    @Override
    public void OnError(String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                add_to_fav.setEnabled(true);
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public void OnSuccess(String fav_added) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dialog_fav.cancel();
                like_btn.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.liked));
                like_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CreateNewContactDialog_DeleteFromFav();
                    }
                });
            }
        });
    }

    @Override
    public void OnSuccess(Screenshot[] screenshots) {
        screenshots_layout = (LinearLayout) findViewById(R.id.screenshotsLayout);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for(Screenshot screen : screenshots){
                    screenshots_layout.addView(ImageBitmapHelper.CreateNewCardViewTemplate(
                            ImageBitmapHelper.GetByteArrayFromString(screen.image),
                            context
                    ));
                }
            }
        });
    }

    @Override
    public void OnSuccessCheck(String msg_is_has) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                like_btn.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.liked));
                like_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CreateNewContactDialog_DeleteFromFav();
                    }
                });
            }
        });
    }

    @Override
    public void OnErrorCheck(String msg_is_has) {
        like_btn.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.like));
        like_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateNewContactDialog_AddToFav();
            }
        });
    }

    @Override
    public void OnSuccessDelete(String deleted_message) {
        dialog_delete.cancel();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

            }
        });
        like_btn.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.like));
        like_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateNewContactDialog_AddToFav();
            }
        });
    }

    @Override
    public void OnErrorDelete(String undeleted_message) {

        like_btn.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.liked));
        like_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateNewContactDialog_DeleteFromFav();
            }
        });
    }
}