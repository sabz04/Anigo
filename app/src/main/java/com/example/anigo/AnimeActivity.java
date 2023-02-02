package com.example.anigo;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
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

        presenter= new AnimeActivityPresenter(this);

        presenter.GetAnime(anime.id);

}

    @Override
    public void OnSuccess() {

    }

    @Override
    public void OnError() {

    }
}