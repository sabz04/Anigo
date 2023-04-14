package com.example.anigo.Activities.AnimeActivityLogic;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

/*import com.wefika.flowlayout.FlowLayout;*/

import com.example.anigo.Activities.AnimeActivityLogic.CommentViewHolderLogic.CommentsAdapter;
import com.example.anigo.Activities.AnimesFiltredActivityLogic.AnimesFiltredActivity;
import com.example.anigo.Activities.NavigationActivityLogic.NavigationActivity;
import com.example.anigo.AnimeWatchActivity;
import com.example.anigo.CommentsActivityLogic.CommentsActivity;
import com.example.anigo.GridAdaptersLogic.GridAdapter;
import com.example.anigo.Models.Anime;
import com.example.anigo.Models.AnimeComment;
import com.example.anigo.Models.AnimeRate;
import com.example.anigo.Models.AnimeResponseWithCommentCount;
import com.example.anigo.Models.CheckUserAcvitity;
import com.example.anigo.Models.RateResponse;
import com.example.anigo.RequestsHelper.RequestOptions;
import com.example.anigo.UiHelper.FlowLayout;
import com.example.anigo.Models.Genre;
import com.example.anigo.UiHelper.ImageBitmapHelper;
import com.example.anigo.R;
import com.example.anigo.Models.Screenshot;
import com.example.anigo.Models.Studio;
import com.example.anigo.UiHelper.TextViewHelper;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AnimeActivity extends AppCompatActivity  implements  AnimeActivityContract.View{

    private AnimeActivityPresenter presenter;
    private AnimeActivityPresenterAddToFavs presenter_fav;
    private AnimeActivityPresenterCheckIfExist presenter_check;
    private AnimeActivityPresenterDeleteFromFav presenter_delete;
    private AnimeActivityGetFranchizeAnimesPresenter presenterFranchize;
    private AnimeActivityPresenterSetRate presenterSetRate;
    private Context context;

    private AlertDialog favouriteAddDialog;
    private AlertDialog dialog_delete;

    private Button like_btn;
    private Button add_to_fav;
    private Button delete_from_fav_btn;
    private Button _showTextViewBtn;
    private Button oneButton;
    private Button animeWatchButton;
    private Button twoButton;
    private Button threeButton;
    private Button fourButton;
    private Button fiveButton;
    private ArrayList<Button> rateButtonsList = new ArrayList<>();
    private ImageView poster;
    private Button _addCommentBtn;
    private Button openCommentsActivityButton;
    private Button commentButton;
    private RatingBar ratingBar;

    private TextView anigoScore;
    private TextView name_rus_tv;
    private TextView nameEnglishTextView;
    private TextView nameJapaneseTextView;
    private TextView description_tv;
    private TextView date_tv;
    private TextView ratesCount;
    private TextView score_tv;
    private TextView type_tv;
    private TextView favsCountTextView;
    private TextView episodesCountTextView;
    private EditText _commentTextView;
    private TextView commentsCountTextView;

    private FlowLayout genres_layout;
    private FlowLayout studios_layout;
    private LinearLayout screenshots_layout;
    private LinearLayout _posterBackgroundLayout;
    private RecyclerView _commentsGridView;
    private RecyclerView gridViewAnimesFr;
    private AnimeFrAdapter adapterAnimes;

    private int anime_id=-1, userId =-1;
    private Date anime_released_on;
    private final int _maxLines = 3;
    private boolean _isExpanded = false;
    private Bitmap _posterAnime;

    private String _openShowTextViewBtnText = "Скрыть";
    private String _closeShowTextViewBtnText = "Раскрыть";
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
        animeWatchButton = findViewById(R.id.watchButton);
        favsCountTextView = findViewById(R.id.favsCountTextView);
        like_btn        = findViewById(R.id.like_btn);
        poster          = findViewById(R.id.itemPoster);
        name_rus_tv     = findViewById(R.id.itemName);
        description_tv  = findViewById(R.id.itemDesc);
        score_tv        = findViewById(R.id.itemScore);
        type_tv         = findViewById(R.id.itemType);
        date_tv         = findViewById(R.id.item_date);
        genres_layout   = findViewById(R.id.genres_layout);
        studios_layout  = findViewById(R.id.studios_layout);
        context         = getApplicationContext();
        nameEnglishTextView = findViewById(R.id.itemNameEnglish);
        nameJapaneseTextView = findViewById(R.id.itemNameJapanese);
        commentButton = findViewById(R.id.commentButton);
        commentsCountTextView = findViewById(R.id.commentsCountTextView);
        episodesCountTextView = findViewById(R.id.episodesCountTextView);
        gridViewAnimesFr = findViewById(R.id.gridView);
        ratingBar = findViewById(R.id.ratingBar);
        ratesCount = findViewById(R.id.countOfRates);
        anigoScore = findViewById(R.id.anigoScore);
       animeWatchButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent = new Intent(AnimeActivity.this, AnimeWatchActivity.class);
               intent.putExtra("animeId",anime_id);
               startActivity(intent);

               OkHttpClient client = new OkHttpClient();
               Request request = new Request.Builder()

                       .url(String.format(RequestOptions.request_url_add_history,userId, anime_id))
                       .get()
                       .build();
               Call call = client.newCall(request);

               call.enqueue(new Callback() {
                   @Override
                   public void onFailure(Call call, IOException e) {

                   }

                   @Override
                   public void onResponse(Call call, Response response) throws IOException {
                       String json_body = response.body().string();
                       if(response.code() == 201 || response.code() == 200 || response.code() == 204){

                       }
                       else {

                       }

                   }
               });

           }
       });
        //openCommentsActivityButton = findViewById(R.id.commentsActivityShowButton);

        presenter        = new AnimeActivityPresenter(this, context);
        presenter_fav    = new AnimeActivityPresenterAddToFavs(this, context);
        presenter_delete = new AnimeActivityPresenterDeleteFromFav(this,context);
        presenterFranchize = new AnimeActivityGetFranchizeAnimesPresenter(this,this);
        presenterSetRate = new AnimeActivityPresenterSetRate(this, this);

        ratingBar.setMax(5);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                presenterSetRate.SetRating(anime_id, (int)v);
            }
        });

        _posterBackgroundLayout = findViewById(R.id.poster_background_layout);
        _showTextViewBtn = findViewById(R.id.show_btn);
        commentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavigationActivity.CommentsPagination.clear();
                Intent intent = new Intent(AnimeActivity.this, CommentsActivity.class);
                intent.putExtra("animeId", anime_id);
                startActivity(intent);
            }
        });


        description_tv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                int lineCount = description_tv.getLineCount();
                if (lineCount > _maxLines) {
                    description_tv.setMaxLines(_maxLines);
                }
            }
        });

        _showTextViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!_isExpanded) {
                    // развернуть TextView на максимальное количество строк
                    description_tv.setMaxLines(Integer.MAX_VALUE);
                    // обновить флаг
                    _isExpanded = true;
                    _showTextViewBtn.setText(_openShowTextViewBtnText);
                } else {
                    // свернуть TextView до максимального количества строк, которые должны быть видны
                    description_tv.setMaxLines(_maxLines);
                    // обновить флаг
                    _isExpanded = false;
                    _showTextViewBtn.setText(_closeShowTextViewBtnText);
                }
            }
        });

        presenter.GetAnime(id);
    }
    private void setButtonRateColorRed(Button button){
        for (Button btn:
             rateButtonsList) {
            if(btn != button){
                btn.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.ic_baseline_star_24));
            }

        }
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

        favouriteAddDialog = dialog_builder.create();
        favouriteAddDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        favouriteAddDialog.show();
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
    public void OnSuccess(AnimeResponseWithCommentCount animeResponseWithCommentCount, int userId) {
        this.userId = userId;
        Anime anime = animeResponseWithCommentCount.anime;
        this.anime_id   = Integer.valueOf(anime.shikiId);
        presenter_check = new AnimeActivityPresenterCheckIfExist(this, context);
        presenter_check.Check(anime.shikiId);
        presenterFranchize.GetAnimes(anime.franchize.name);
       runOnUiThread(new Runnable() {
           @Override
           public void run() {
               Picasso.with(context).load(RequestOptions.SecondHost+anime.images[0].original).into(poster);
               name_rus_tv.setText(anime.nameRus);
               description_tv.setText(anime.description);
               score_tv.setText(String.valueOf(anime.scoreShiki)+"/10");
               nameEnglishTextView.setText(anime.nameEng);
               String aniScore = average(anime.animeRates) + "/5";
               anigoScore.setText(aniScore);
               ratesCount.setText(anime.animeRates.length + " оценок");
               nameJapaneseTextView.setText(anime.japaneses[0].name);
               type_tv.setText(AnimeTypeOrganizer.Organizer(anime.type.name));
               type_tv.setPaintFlags(type_tv.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
               commentsCountTextView.setText(String.valueOf(animeResponseWithCommentCount.commentsCount));
               episodesCountTextView.setText(anime.episodes + " эп.");
               anime_released_on  = anime.releasedOn;
               if(anime_released_on != null){
                   Calendar calendar = Calendar.getInstance();
                   calendar.setTime(anime_released_on);
                   String year  = String.valueOf(calendar.get(Calendar.YEAR));
                   String month = GetDate(calendar.get(Calendar.MONTH));
                   date_tv.setText(String.format("%s, %s г.", month.toLowerCase(Locale.ROOT), year));
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
                   TextView txt = TextViewHelper.Create_New_TextView_Template(
                           context,
                           studio.name,
                           AnimeActivity.this);
                   studios_layout.addView(txt);
               }
           }
       });
    }
    public static float average(AnimeRate[] arr) {
        int sum = 0;
        for (int i = 0; i < arr.length; i++) {
            sum += arr[i].rate.value;
        }
        return (float) sum / arr.length;
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
                favsCountTextView.setText(message);
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public void OnSuccess(String fav_added) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                favouriteAddDialog.cancel();
                favsCountTextView.setText(fav_added);
                like_btn.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.ic_baseline_bookmark_added_24));
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
                            RequestOptions.SecondHost + screen.image,
                            context
                    ));
                }
            }
        });
    }

    @Override
    public void OnSuccessCheck(CheckUserAcvitity checkUserAcvitity) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                favsCountTextView.setText(String.valueOf(checkUserAcvitity.favsCount));
                ratingBar.setRating(checkUserAcvitity.userRate);
                like_btn.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.ic_baseline_bookmark_added_24));
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
    public void OnErrorCheck(CheckUserAcvitity checkUserAcvitity) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ratingBar.setRating(checkUserAcvitity.userRate);
                favsCountTextView.setText(String.valueOf(checkUserAcvitity.favsCount));
            }
        });
        like_btn.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.ic_baseline_bookmark_border_24));
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
             favsCountTextView.setText(deleted_message);
            }
        });
        like_btn.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.ic_baseline_bookmark_border_24));
        like_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateNewContactDialog_AddToFav();
            }
        });
    }

    @Override
    public void OnErrorDelete(String undeleted_message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                favsCountTextView.setText(undeleted_message);
            }
        });
        like_btn.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.ic_baseline_bookmark_added_24));
        like_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateNewContactDialog_DeleteFromFav();
            }
        });
    }

    @Override
    public void OnSuccessGetLinkedAnimes(Anime[] animes) {
        if(animes.length > 0){
            Anime[] newArray = new Anime[animes.length-1];
            int j=0;
            for (int i = 0; i < animes.length; i++) {
                if (animes[i].shikiId != anime_id) {
                    newArray[j] = animes[i]; // copy the element to the new array
                    j++; // increment the index for the new array
                }
            }
            adapterAnimes = new AnimeFrAdapter(newArray,this, anime_id);

            adapterAnimes.setOnItemClickListener(new AnimeFrAdapter.OnItemClickListener() {
                @Override
                public void OnClick(int position) {
                    Intent to_anime = new Intent(context, AnimeActivity.class);
                    Bundle bundle = new Bundle();
                    int id = newArray[position].shikiId;
                    bundle.putInt("id", id);
                    to_anime.putExtras(bundle);
                    startActivity(to_anime);
                }
            });

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    gridViewAnimesFr.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,true));
                    gridViewAnimesFr.setAdapter(adapterAnimes);

                }
            });
        }


    }

    @Override
    public void OnErrorGetLinkedAnimes(String message) {

    }

    @Override
    public void OnSuccessSetRate(RateResponse rateResponse) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                anigoScore.setText(String.valueOf(rateResponse.averageRate)+ "/5");
                ratesCount.setText(rateResponse.totalRateCount + " оценок");
            }
        });
    }

    @Override
    public void OnErrorSetRate(String message) {

    }


}