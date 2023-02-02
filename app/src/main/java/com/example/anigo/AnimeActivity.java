package com.example.anigo;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class AnimeActivity extends AppCompatActivity  implements  AnimeActivityContract.View{

    private AnimeActivityPresenter presenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anime);

        Bundle bundle = getIntent().getExtras();

        Anime anime =  (Anime)bundle.getSerializable("current_title");



        presenter= new AnimeActivityPresenter(this, getApplicationContext());

        presenter.GetAnime(anime.shikiId);


}

    @Override
    public void OnSuccess(Anime anime) {


       runOnUiThread(new Runnable() {
           @Override
           public void run() {
               ImageView poster = findViewById(R.id.itemPoster);
               poster.setImageBitmap(GetImageBitmap(java.util.Base64.getDecoder().decode(anime.images[0].original)));

               TextView text= (TextView) findViewById(R.id.itemName);

               text.setText(anime.nameRus);

               TextView desc = (TextView)findViewById(R.id.itemDesc);

               desc.setText(anime.description);

               TextView score = (TextView)findViewById(R.id.itemScore);

               score.setText(String.valueOf(anime.scoreShiki));

               TextView type = (TextView)findViewById(R.id.itemType);

               type.setText(anime.type.name);
           }
       });


    }

    @Override
    public void OnError(String message) {

    }
    private Bitmap GetImageBitmap(byte[] jsonImage){

        Bitmap bitmap = BitmapFactory.decodeByteArray(jsonImage, 0, jsonImage.length);
        System.out.println(bitmap.getHeight());
        bitmap= bitmap.copy(Bitmap.Config.ARGB_8888, true);
        return bitmap;

    }
}